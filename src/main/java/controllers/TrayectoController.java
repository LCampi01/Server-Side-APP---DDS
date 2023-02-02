package controllers;

import helpers.UsuarioHelper;
import models.entities.organizaciones.Organizacion;
import models.entities.personas.MiembroDeOrganizacion;
import models.entities.personas.Trayecto;
import models.repositorios.RepositorioDeMiembroDeOrganizacion;
import models.repositorios.RepositorioDeOrganizacion;
import models.repositorios.RepositorioDeTrayecto;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class TrayectoController {
    private RepositorioDeTrayecto repositorioDeTrayecto;
    private RepositorioDeMiembroDeOrganizacion repositorioDeMiembroDeOrganizacion;
    private RepositorioDeOrganizacion repositorioDeOrganizacion;

    public TrayectoController() {
        this.repositorioDeTrayecto = new RepositorioDeTrayecto();
        this.repositorioDeMiembroDeOrganizacion = new RepositorioDeMiembroDeOrganizacion();
        this.repositorioDeOrganizacion = new RepositorioDeOrganizacion();
    }

    public ModelAndView mostrarTrayectosPorMiembro(Request request, Response response) {
        MiembroDeOrganizacion miembro = UsuarioHelper.obtenerMiembro(request);
        List<Trayecto> trayectos = this.repositorioDeTrayecto.buscarTodos(miembro.getId());
        return new ModelAndView(new HashMap<String, Object>(){{
            put("trayectos", trayectos);
            put("nombre", miembro.getNombre());
        }}, "miembros/trayectos.hbs");
    }

    public Response agregarTrayecto(Request request, Response response) throws InterruptedException {
        Organizacion organizacion = this.repositorioDeOrganizacion.buscar(Integer.valueOf(request.queryParams("organizacion")));

        DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
        LocalDate date = LocalDate.parse(request.queryParams("fecha").replace("-",""), formatter);

        Trayecto trayecto = new Trayecto(organizacion, request.queryParams("descripcion"),Integer.valueOf(request.queryParams("periodicidad")),date);

        MiembroDeOrganizacion miembroDeOrganizacion = this.repositorioDeMiembroDeOrganizacion.buscar(request.session().attribute("idMiembroDeOrganizacion"));
        miembroDeOrganizacion.agregarTrayectos(trayecto);
        this.repositorioDeTrayecto.guardar(trayecto,miembroDeOrganizacion);
        response.redirect("/miembros/trayectos");
        return response;
    }

    public ModelAndView agregarTrayectoMiembroVista(Request request, Response response) {
        MiembroDeOrganizacion miembro = UsuarioHelper.obtenerMiembro(request);
        List<Organizacion> organizaciones =  this.repositorioDeMiembroDeOrganizacion.buscarOrganizaciones(miembro.getId());

        return new ModelAndView(new HashMap<String, Object>(){{
            put("organizaciones", organizaciones);
            put("nombre", miembro.getNombre());
        }}, "miembros/agregar_trayecto.hbs");
    }

    public Response eliminar(Request request, Response response) {
        this.repositorioDeTrayecto.eliminar(Integer.parseInt(request.params("id")));
//        response.redirect("/miembros/trayectos");
        return response;
    }
}
