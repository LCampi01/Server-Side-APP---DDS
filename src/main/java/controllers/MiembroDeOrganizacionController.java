package controllers;

import controllers.transformadosParaVistas.SectorOrganizacionTransformadoParaVista;
import helpers.UsuarioHelper;
import models.entities.huellaDeCarbono.HuellaDeCarbono;
import models.entities.organizaciones.ClasificacionDeOrganizacion;
import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.SectorOrganizacion;
import models.entities.reportes.ReportesHCTotal.HCTotalPorClasificacionDeOrganizacion;
import models.entities.reportes.ReportesHCTotal.HCTotalSegunAlgo;
import models.entities.reportes.reportesComposicion.ComposicionHCOrganizacion;
import models.entities.reportes.reportesComposicion.PorcentajeHC;
import models.entities.reportes.reportesEvolucion.EvolucionOrganizacion;
import models.entities.reportes.reportesEvolucion.HCAnual;
import models.repositorios.RepositorioDeClasificacionDeOrganizacion;
import models.repositorios.RepositorioDeMiembroDeOrganizacion;
import models.repositorios.RepositorioDeOrganizacion;
import models.entities.personas.MiembroDeOrganizacion;
import models.repositorios.RepositorioDeSectorOrganizacion;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MiembroDeOrganizacionController {
    private RepositorioDeMiembroDeOrganizacion repositorioDeMiembroDeOrganizacion;
    private RepositorioDeOrganizacion repositorioDeOrganizacion;
    private RepositorioDeSectorOrganizacion repositorioDeSectorOrganizacion;

    private RepositorioDeClasificacionDeOrganizacion repositorioDeClasificacionDeOrganizacion;

    public MiembroDeOrganizacionController() {
        this.repositorioDeMiembroDeOrganizacion = new RepositorioDeMiembroDeOrganizacion();
        this.repositorioDeOrganizacion = new RepositorioDeOrganizacion();
        this.repositorioDeSectorOrganizacion = new RepositorioDeSectorOrganizacion();
    }

    public ModelAndView mostrarInicio(Request request, Response response) {
        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombre", UsuarioHelper.obtenerMiembro(request).getNombre());
        }}, "miembros/inicio.hbs");
    }

    //////////////////// huella ////////////////////
    public String obtenerHuellaAnual(Request request, Response response) {
        MiembroDeOrganizacion miembro = UsuarioHelper.obtenerMiembro(request);
        HuellaDeCarbono huellaDeCarbono = miembro.huellaDeCarbonoAnual(request.params("anio"));
        String json = "{\"message\": \"La huella de carbono anual es: <strong><font size='+2'>" + huellaDeCarbono.getValor() + "</font></strong> "+ huellaDeCarbono.getUnidadHuellaCarbono()+ "\"}";
        return json;
    }

    public String obtenerHuellaMensual(Request request, Response response) {
        MiembroDeOrganizacion miembro = UsuarioHelper.obtenerMiembro(request);
        HuellaDeCarbono huellaDeCarbono = miembro.huellaDeCarbonoMensual(request.params("mes"),request.params("anio"));
        String json = "{\"message\": \"La huella de carbono mensual es: <strong><font size='+2'>" + huellaDeCarbono.getValor() + "</font></strong> "+ huellaDeCarbono.getUnidadHuellaCarbono()+ "\"}";
        return json;
    }


    //////////////////// reportes ////////////////////
    public ModelAndView mostrarReportes(Request request, Response response) {
        MiembroDeOrganizacion miembro = UsuarioHelper.obtenerMiembro(request);
        List<Organizacion> organizaciones = repositorioDeMiembroDeOrganizacion.buscarOrganizaciones(miembro.getId());

        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombre", miembro.getNombre());
            put("organizaciones", organizaciones);
        }}, "miembros/reportes.hbs");
    }

    public ModelAndView mostrarReporteComposicionOrganizacionVista(Request request, Response response) {
        Organizacion organizacion = repositorioDeOrganizacion.buscar(Integer.parseInt(request.queryParams("organizacion")));

        ComposicionHCOrganizacion generadorReporte = new ComposicionHCOrganizacion();
        List<PorcentajeHC> reporte;

        if(request.queryParams("mes") == null) {
            reporte = generadorReporte.reporteComposicionOrganizacionAnual(organizacion, Integer.parseInt(request.queryParams("anio")));
        } else {
            reporte = generadorReporte.reporteComposicionOrganizacionMensual(
                            organizacion,
                            Integer.parseInt(request.queryParams("mes")),
                            Integer.parseInt(request.queryParams("anio"))
                    );
        }

        List<Double> porcentajes = new ArrayList<>();
        porcentajes.add(reporte.get(0).getPorcentaje());
        porcentajes.add(reporte.get(1).getPorcentaje());

        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombre", UsuarioHelper.obtenerMiembro(request).getNombre());
            put("razonSocial", organizacion.getRazonSocial());
            put("porcentajes", porcentajes);
        }}, "miembros/reporte_composicion_organizacion.hbs");
    }

    public ModelAndView mostrarReporteHCSegunClasificacion(Request request, Response response) {
        List<ClasificacionDeOrganizacion> clasificaciones = repositorioDeClasificacionDeOrganizacion.buscarTodos();
        List<Organizacion> organizaciones = repositorioDeOrganizacion.buscarTodos();


        HCTotalPorClasificacionDeOrganizacion generadorReporte = new HCTotalPorClasificacionDeOrganizacion();
        List<HCTotalSegunAlgo> reporte;

        if (request.queryParams("mes") == null) {
            reporte = generadorReporte.reporteHCTotalAnual(clasificaciones, organizaciones, Integer.parseInt(request.queryParams("anio")));
        } else {
            reporte = generadorReporte.reporteHCTotalMensual(
                    clasificaciones, organizaciones,
                    Integer.parseInt(request.queryParams("mes")),
                    Integer.parseInt(request.queryParams("anio"))
            );
        }
        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombre", UsuarioHelper.obtenerMiembro(request).getNombre());
            put("organizaciones", organizaciones);
            put("clasificaciones", clasificaciones);
        }}, "miembros/reporte_hc.hbs");
    }

    public ModelAndView mostrarReporteEvolucionOrganizacionVista(Request request, Response response) {
        MiembroDeOrganizacion miembro = UsuarioHelper.obtenerMiembro(request);
        Organizacion organizacion = repositorioDeOrganizacion.buscar(Integer.parseInt(request.queryParams("organizacion")));

        EvolucionOrganizacion generadorReporte = new EvolucionOrganizacion(organizacion);
        List<HCAnual> reporte = generadorReporte.reporteEvolucion(Integer.parseInt(request.queryParams("anio")));

        List<Integer> anios = new ArrayList<>();
        List<Double> valores = new ArrayList<>();
        for (HCAnual hcAnual : reporte) {
            anios.add(hcAnual.getAnio());
            valores.add(hcAnual.getValorHC());
        }
        String unidad = reporte.get(0).getUnidad().toString();

        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombre", miembro.getNombre());
            put("razonSocial", organizacion.getRazonSocial());
            put("anios", anios);
            put("valores", valores);
            put("unidad", unidad);
        }}, "miembros/reporte_evolucion_organizacion.hbs");
    }

    //////////////////// mis organizaciones ////////////////////
    public ModelAndView mostrarOrganizaciones(Request request, Response response) {
        MiembroDeOrganizacion miembroDeOrganizacion = UsuarioHelper.obtenerMiembro(request);

        List<SectorOrganizacionTransformadoParaVista> sectoresOrganizaciones = new ArrayList<>();

        List<SectorOrganizacion> sectoresPendientes = this.repositorioDeMiembroDeOrganizacion.buscarSectoresPendientes(miembroDeOrganizacion.getId());
        sectoresPendientes.forEach(sectorOrganizacion -> {
            Organizacion organizacion = this.repositorioDeOrganizacion.buscarOrganizacionDeSector(sectorOrganizacion.getId());
            sectoresOrganizaciones.add(new SectorOrganizacionTransformadoParaVista(sectorOrganizacion.getNombre(),organizacion.getRazonSocial(),"Pendiente"));
        });

        List<SectorOrganizacion> sectoresAceptados = this.repositorioDeMiembroDeOrganizacion.buscarSectores(miembroDeOrganizacion.getId());
        sectoresAceptados.forEach(sectorOrganizacion -> {
            Organizacion organizacion = this.repositorioDeOrganizacion.buscarOrganizacionDeSector(sectorOrganizacion.getId());
            sectoresOrganizaciones.add(new SectorOrganizacionTransformadoParaVista(sectorOrganizacion.getNombre(),organizacion.getRazonSocial(),"Aceptado"));
        });

        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombre", miembroDeOrganizacion.getNombre());
            put("sectoresOrganizaciones", sectoresOrganizaciones);
        }}, "miembros/organizaciones.hbs");
    }


    public ModelAndView mostrarPantallaHuellaAnual(Request request, Response response) {
        MiembroDeOrganizacion miembroDeOrganizacion = UsuarioHelper.obtenerMiembro(request);
        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombre", miembroDeOrganizacion.getNombre());
        }}, "miembros/huella-anual.hbs");
    }

    public ModelAndView mostrarPantallaHuellaMensual(Request request, Response response) {
        MiembroDeOrganizacion miembroDeOrganizacion = UsuarioHelper.obtenerMiembro(request);
        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombre", miembroDeOrganizacion.getNombre());
        }}, "miembros/huella-mensual.hbs");
    }

    public ModelAndView agregarOrganizacionVista(Request request, Response response) {
        List<Organizacion> organizaciones = this.repositorioDeOrganizacion.buscarTodos();
        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombre", UsuarioHelper.obtenerMiembro(request).getNombre());
            put("organizaciones", organizaciones);
        }}, "miembros/seleccionar_organizacion.hbs");
    }


    public ModelAndView agregarOrganizacionSectoresVista(Request request, Response response) {
        List<SectorOrganizacion> sectores = this.repositorioDeOrganizacion.buscarSectoresDeOrganizacion(Integer.parseInt(request.queryParams("organizacion")));
        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombre", UsuarioHelper.obtenerMiembro(request).getNombre());
            put("sectores", sectores);
        }}, "miembros/seleccionar_sector.hbs");
    }

    public Response  agregarOrganizacion(Request request, Response response) throws IOException {
        MiembroDeOrganizacion miembroDeOrganizacion = UsuarioHelper.obtenerMiembro(request);
        SectorOrganizacion sectorOrganizacion = this.repositorioDeSectorOrganizacion.buscar(Integer.valueOf(request.queryParams("sector")));
        Organizacion organizacion = this.repositorioDeOrganizacion.buscarOrganizacionDeSector(sectorOrganizacion.getId());
        miembroDeOrganizacion.solicitarVinculacion(organizacion,sectorOrganizacion);
        this.repositorioDeMiembroDeOrganizacion.guardar(miembroDeOrganizacion);
        response.redirect("/miembros/organizaciones");
        return response;
    }

    public ModelAndView agregarTramoVista(Request request, Response response) {
        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombre", UsuarioHelper.obtenerMiembro(request).getNombre());
            put("idTrayecto", request.params("id"));
        }}, "miembros/agregar_tramo.hbs");
    }


    public ModelAndView mostrarAyudaVista(Request request, Response response) {
        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombre", UsuarioHelper.obtenerMiembro(request).getNombre());
        }}, "miembros/ayuda.hbs");
    }
}
