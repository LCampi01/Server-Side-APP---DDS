package controllers;

import com.google.gson.Gson;
import controllers.transformadosParaVistas.TramoTransformadoParaVista;
import helpers.UsuarioHelper;
import models.entities.personas.MiembroDeOrganizacion;
import models.entities.personas.Tramo;
import models.entities.personas.Trayecto;
import models.entities.transportes.*;
import models.entities.ubicaciones.territorio.Departamento;
import models.entities.ubicaciones.ubicacion.Direccion;
import models.entities.ubicaciones.ubicacion.Parada;
import models.repositorios.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TramoController {
    private RepositorioDeTramo repositorioDeTramo;
    private RepositorioDeMiembroDeOrganizacion repositorioDeMiembroDeOrganizacion;
    private RepositorioDeDepartamento repositorioDeDepartamento;
    private RepositorioDeTrayecto repositorioDeTrayecto;
    private RepositorioDeTransportes repositorioDeTransportes;

    public TramoController() {
        this.repositorioDeTramo = new RepositorioDeTramo();
        this.repositorioDeMiembroDeOrganizacion = new RepositorioDeMiembroDeOrganizacion();
        this.repositorioDeDepartamento = new RepositorioDeDepartamento();
        this.repositorioDeTrayecto = new RepositorioDeTrayecto();
        this.repositorioDeTransportes = new RepositorioDeTransportes();
    }

    public ModelAndView mostrarTramosPorTrayecto(Request request, Response response) {
        String idTrayecto = request.params("id");
        MiembroDeOrganizacion miembro = UsuarioHelper.obtenerMiembro(request);
        List<Tramo> tramos = this.repositorioDeTramo.buscarTodos(Integer.parseInt(idTrayecto));

        List<TramoTransformadoParaVista> tramosTransformados = new ArrayList<>();
        for (Tramo t : tramos) {
            String nombreMedioTransporte = t.getMedioDeTransporte().getClass().getName();
            switch(nombreMedioTransporte.split("\\.")[3]) {
                case "TransportePublico":
                    Parada paradaPartida = (Parada) t.getPuntoDePartida();
                    Parada paradaLlegada = (Parada) t.getPuntoDeLlegada();
                    tramosTransformados.add(
                        new TramoTransformadoParaVista(
                            t.getId(),
                            "Transporte público",
                            paradaPartida.getDescripcion(),
                            paradaLlegada.getDescripcion()
                        )
                    );
                    break;
                case "VehiculoParticular":
                    tramosTransformados.add(
                        new TramoTransformadoParaVista(
                            t.getId(),
                            "Vehículo particular",
                            t.getPuntoDePartida().getCalle() + " " + t.getPuntoDePartida().getAltura(),
                            t.getPuntoDeLlegada().getCalle() + " " + t.getPuntoDeLlegada().getAltura()
                        )
                    );
                    break;
                case "ServicioContratado":
                    tramosTransformados.add(
                        new TramoTransformadoParaVista(
                            t.getId(),
                            "Servicio contratado",
                            t.getPuntoDePartida().getCalle() + " " + t.getPuntoDePartida().getAltura(),
                            t.getPuntoDeLlegada().getCalle() + " " + t.getPuntoDeLlegada().getAltura()
                        )
                    );
                    break;
                case "SinVehiculo":
                    tramosTransformados.add(
                        new TramoTransformadoParaVista(
                            t.getId(),
                            "Sin vehículo",
                            t.getPuntoDePartida().getCalle() + " " + t.getPuntoDePartida().getAltura(),
                            t.getPuntoDeLlegada().getCalle() + " " + t.getPuntoDeLlegada().getAltura()
                        )
                    );
                    break;
                default:
                    System.out.println("default");
            }
        }

        return new ModelAndView(new HashMap<String, Object>(){{
            put("tramos", tramosTransformados);
            put("nombre", miembro.getNombre());
            put("idTrayecto", idTrayecto);
        }}, "miembros/trayecto.hbs");
    }

    public Response eliminar(Request request, Response response) {
        //no tendría que eliminar el tramo xq puede ser compartido. Solo tendría que borrar la entrada de la tabla intermedia.
        this.repositorioDeTramo.eliminar(Integer.parseInt(request.params("idTramo")), Integer.parseInt(request.params("id")));
        return response;
    }

    public ModelAndView agregarTramoServicioContratadoVista(Request request, Response response) {
        List<ServicioContratado> serviciosContratados = this.repositorioDeTransportes.buscarTodosServiciosContratados();
        List<Departamento> departamentos = this.repositorioDeDepartamento.buscarTodos();

        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombre", UsuarioHelper.obtenerMiembro(request).getNombre());
            put("idTrayecto", request.params("id"));
            put("serviciosContratados", serviciosContratados);
            put("departamentos", departamentos);
        }}, "miembros/tramos_transportes/agregar_servicio_contratado.hbs");
    }

    public Response agregarTramoServicioContratado(Request request, Response response) throws InterruptedException {
        Departamento departamentoPartida = this.repositorioDeDepartamento.buscar(Integer.parseInt(request.queryParams("departamentoPartida")));
        Departamento departamentoLlegada = this.repositorioDeDepartamento.buscar(Integer.parseInt(request.queryParams("departamentoLlegada")));

        Direccion direccionDePartida = new Direccion(departamentoPartida,
                request.queryParams("callePartida"), Integer.valueOf(request.queryParams("alturaPartida")));
        Direccion direccionDeLlegada = new Direccion(departamentoLlegada,
                request.queryParams("calleLlegada"), Integer.valueOf(request.queryParams("alturaLlegada")));

        ServicioContratado servicioContratado = repositorioDeTransportes.buscarServicioContratado(request.queryParams("medioInvolucrado"));
        Tramo tramo = new Tramo(servicioContratado, direccionDePartida, direccionDeLlegada);
        Double distancia = tramo.distancia();

        Trayecto trayecto = this.repositorioDeTrayecto.buscar(Integer.parseInt(request.params("id")));
        trayecto.agregarTramo(tramo);
        this.repositorioDeTramo.guardar(tramo, trayecto);

        response.redirect("/miembros/trayectos/" + request.params("id") + "/tramos");
        return response;
    }

    public ModelAndView agregarTramoTransportePublicoVista(Request request, Response response) {
        List<TipoTransportePublico> tipoTransportes = this.repositorioDeTransportes.buscarTipoDeTransporte();

        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombre", UsuarioHelper.obtenerMiembro(request).getNombre());
            put("idTrayecto", request.params("id"));
            put("tipoTransportes", tipoTransportes);
        }}, "miembros/tramos_transportes/agregar_transporte.hbs");
    }

    public Response agregarTramoTransportePublico(Request request, Response response) throws InterruptedException {
        Parada paradaInicial = this.repositorioDeTransportes.buscarParada(Integer.parseInt(request.queryParams("paradaInicial")));
        Parada paradaFinal = this.repositorioDeTransportes.buscarParada(Integer.parseInt(request.queryParams("paradaFinal")));

        TransportePublico transporte = this.repositorioDeTransportes.buscarTransportePublico(request.queryParams("tipoTransporte"), request.queryParams("lineasTransporte"));
        Tramo tramo = new Tramo(transporte, paradaInicial, paradaFinal);
        Double distancia = tramo.distancia();

        Trayecto trayecto = this.repositorioDeTrayecto.buscar(Integer.parseInt(request.params("id")));
        trayecto.agregarTramo(tramo);
        this.repositorioDeTramo.guardar(tramo, trayecto);

        response.redirect("/miembros/trayectos/" + request.params("id") + "/tramos");
        return response;

    }

    public ModelAndView agregarTramoSinVehiculoVista(Request request, Response response) {
        List<Departamento> departamentos = this.repositorioDeDepartamento.buscarTodos();

        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombre", UsuarioHelper.obtenerMiembro(request).getNombre());
            put("idTrayecto", request.params("id"));
            put("departamentos", departamentos);
        }}, "miembros/tramos_transportes/agregar_sin_vehiculo.hbs");
    }

    public Response agregarTramoSinVehiculo(Request request, Response response) throws InterruptedException {
        Departamento departamentoPartida = this.repositorioDeDepartamento.buscar(Integer.parseInt(request.queryParams("departamentoPartida")));
        Departamento departamentoLlegada = this.repositorioDeDepartamento.buscar(Integer.parseInt(request.queryParams("departamentoLlegada")));

        Direccion direccionDePartida = new Direccion(departamentoPartida,
                request.queryParams("callePartida"), Integer.valueOf(request.queryParams("alturaPartida")));
        Direccion direccionDeLlegada = new Direccion(departamentoLlegada,
                request.queryParams("calleLlegada"), Integer.valueOf(request.queryParams("alturaLlegada")));

        SinVehiculo sinVehiculo = repositorioDeTransportes.buscarSinVehiculo();
        Tramo tramo = new Tramo(sinVehiculo, direccionDePartida, direccionDeLlegada);
        Double distancia = tramo.distancia();

        Trayecto trayecto = this.repositorioDeTrayecto.buscar(Integer.parseInt(request.params("id")));
        trayecto.agregarTramo(tramo);
        this.repositorioDeTramo.guardar(tramo, trayecto);

        response.redirect("/miembros/trayectos/" + request.params("id") + "/tramos");
        return response;
    }

    public ModelAndView agregarTramoVehiculoParticularVista(Request request, Response response) {
        List<Departamento> departamentos = this.repositorioDeDepartamento.buscarTodos();

        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombre", UsuarioHelper.obtenerMiembro(request).getNombre());
            put("idTrayecto", request.params("id"));
            put("departamentos", departamentos);
        }}, "miembros/tramos_transportes/agregar_vehiculo_particular.hbs");
    }

    public Response agregarTramoVehiculoParticular(Request request, Response response) throws InterruptedException {
        Departamento departamentoPartida = this.repositorioDeDepartamento.buscar(Integer.parseInt(request.queryParams("departamentoPartida")));
        Departamento departamentoLlegada = this.repositorioDeDepartamento.buscar(Integer.parseInt(request.queryParams("departamentoLlegada")));

        Direccion direccionDePartida = new Direccion(departamentoPartida,
                request.queryParams("callePartida"), Integer.valueOf(request.queryParams("alturaPartida")));
        Direccion direccionDeLlegada = new Direccion(departamentoLlegada,
                request.queryParams("calleLlegada"), Integer.valueOf(request.queryParams("alturaLlegada")));

        VehiculoParticular vehiculoParticular = repositorioDeTransportes
                .buscarVehiculoParticular(request.queryParams("tipoCombustible"), request.queryParams("tipoVehiculo"));
        Tramo tramo = new Tramo(vehiculoParticular, direccionDePartida, direccionDeLlegada);
        Double distancia = tramo.distancia();

        Trayecto trayecto = this.repositorioDeTrayecto.buscar(Integer.parseInt(request.params("id")));
        trayecto.agregarTramo(tramo);
        this.repositorioDeTramo.guardar(tramo, trayecto);

        response.redirect("/miembros/trayectos/" + request.params("id") + "/tramos");
        return response;
    }

    public String traerLineas(Request request, Response response) {
        List<TransportePublico> transportes = this.repositorioDeTransportes.buscarLineas(request.params("tipoDeTransporte"));
        List<String> lineas = new ArrayList<>();
        for(TransportePublico transporte : transportes) {
            lineas.add(transporte.getLineaUtilizada());
        }
        String json = new Gson().toJson(lineas);
        return json;
    }

    public String traerParadas(Request request, Response reponse) {
        TransportePublico transporte = this.repositorioDeTransportes.buscarTransportePublico(request.params("tipoDeTransporte"), request.params("linea"));
        List<Parada> paradas = this.repositorioDeTransportes.buscarParadas(transporte.getId());
        List<HashMap<String, String>> listaParadas = new ArrayList<>();
        for(Parada parada : paradas) {
            HashMap<String, String> HashMapParadas = new HashMap<String, String>();
            HashMapParadas.put("id", Integer.toString(parada.getId()));
            HashMapParadas.put("descripcion", parada.getDescripcion());
            listaParadas.add(HashMapParadas);
        }
        String json = new Gson().toJson(listaParadas);
        return json;
    }

}
