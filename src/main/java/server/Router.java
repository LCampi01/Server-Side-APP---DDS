package server;


import controllers.*;
import helpers.UsuarioHelper;
import middlewares.AuthMiddleware;
import models.entities.usuarios.RolEnum;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;
import spark.utils.BooleanHelper;
import spark.utils.HandlebarsTemplateEngineBuilder;

public class Router {
    private static HandlebarsTemplateEngine engine;

    private static void initEngine() {
        Router.engine = HandlebarsTemplateEngineBuilder
                .create()
                .withDefaultHelpers()
                .withHelper("isTrue", BooleanHelper.isTrue)
                .build();
    }

    public static void init() {
        Router.initEngine();
        Spark.staticFileLocation("/public");
        Router.configure();
    }

    private static void configure(){
        LoginController loginController = new LoginController();
        MiembroDeOrganizacionController miembroDeOrganizacionController = new MiembroDeOrganizacionController();
        OrganizacionController organizacionController = new OrganizacionController();
        InicioController inicioController = new InicioController();
        TrayectoController trayectoController = new TrayectoController();
        TramoController tramoController = new TramoController();
        AgenteSectorialController agenteSectorialController = new AgenteSectorialController();

        Spark.path("/login", () -> {
            Spark.get("", loginController::pantallaDeLogin, engine);
            Spark.post("", loginController::login);
        });

        Spark.path("/registro", () -> {
            Spark.get("", loginController::pantallaDeRegistro, engine);
            Spark.post("", loginController::registro);
        });

        Spark.path("/olvide-contrasena", () -> {
            Spark.get("", loginController::pantallaDeOlvideContrasena, engine);
        });

        Spark.path("/logout", () -> {
            Spark.post("", loginController::logout);
        });
        /*
        Spark.path("/inicio", () -> {
            Spark.get("", inicioController::mostrarInicio, engine);
        });
    */
        Spark.path("/prohibido", () -> {
            Spark.get("", loginController::prohibido);
        });

        Spark.path("/transporte-publico", () -> {
            Spark.get("/:tipoDeTransporte", tramoController::traerLineas);
            Spark.get("/:tipoDeTransporte/:linea", tramoController::traerParadas);

        });

        Spark.path("/miembros", () ->{
            Spark.before("", AuthMiddleware::verificarSesion);
            Spark.before("/*", AuthMiddleware::verificarSesion);

            Spark.before("", ((request, response) -> {
                if(UsuarioHelper.usuarioLogueado(request).getRol() != RolEnum.MIEMBRO_ORGANIZACION) {
                    response.redirect("/prohibido");
                    Spark.halt();
                }
            }));

            Spark.get("",miembroDeOrganizacionController::mostrarInicio, engine);

            Spark.path("/huella-mensual", () -> {
                Spark.get("",miembroDeOrganizacionController::mostrarPantallaHuellaMensual, engine);
                Spark.get("/:mes/:anio",miembroDeOrganizacionController::obtenerHuellaMensual);
            });

            Spark.path("/huella-anual", () -> {
                Spark.get("",miembroDeOrganizacionController::mostrarPantallaHuellaAnual, engine);
                Spark.get("/:anio",miembroDeOrganizacionController::obtenerHuellaAnual);
            });

            Spark.path("/reportes", () -> {
                Spark.get("",miembroDeOrganizacionController::mostrarReportes, engine);
                Spark.get("/composicion-organizacion",miembroDeOrganizacionController::mostrarReporteComposicionOrganizacionVista, engine);
                Spark.get("/evolucion-organizacion",miembroDeOrganizacionController::mostrarReporteEvolucionOrganizacionVista, engine);
            });

            Spark.path("/organizaciones", () -> {
                Spark.get("",miembroDeOrganizacionController::mostrarOrganizaciones, engine);
                Spark.get("/agregar",miembroDeOrganizacionController::agregarOrganizacionVista, engine);
                Spark.get("/agregar/sectores",miembroDeOrganizacionController::agregarOrganizacionSectoresVista, engine);
                Spark.post("",miembroDeOrganizacionController::agregarOrganizacion);
            });

            Spark.path("/trayectos", () -> {
                Spark.get("",trayectoController::mostrarTrayectosPorMiembro, engine);

                Spark.get("/agregar",trayectoController::agregarTrayectoMiembroVista, engine);
                Spark.post("",trayectoController::agregarTrayecto);
                Spark.delete("/:id",trayectoController::eliminar);

//            Spark.get("/:id/tramos",miembroDeOrganizacionController::mostrarTrayecto, engine);

                Spark.path("/:id/tramos", () -> {
                    Spark.get("", tramoController::mostrarTramosPorTrayecto, engine);
                    Spark.get("/agregar",miembroDeOrganizacionController::agregarTramoVista,engine);

                    Spark.path("/agregar", () -> {
                        Spark.get("/servicio-contratado",tramoController::agregarTramoServicioContratadoVista,engine);
                        Spark.get("/transporte-publico",tramoController::agregarTramoTransportePublicoVista,engine);
                        Spark.get("/sin-vehiculo",tramoController::agregarTramoSinVehiculoVista,engine);
                        Spark.get("/vehiculo-particular",tramoController::agregarTramoVehiculoParticularVista,engine);
                        Spark.post("/servicio-contratado",tramoController::agregarTramoServicioContratado);
                        Spark.post("/transporte-publico",tramoController::agregarTramoTransportePublico);
                        Spark.post("/sin-vehiculo",tramoController::agregarTramoSinVehiculo);
                        Spark.post("/vehiculo-particular",tramoController::agregarTramoVehiculoParticular);
                    });

                    Spark.delete("/:idTramo",tramoController::eliminar);
                });
            });

            Spark.get("/ayuda",miembroDeOrganizacionController::mostrarAyudaVista, engine);

        });

        Spark.path("/organizaciones", () -> {
            Spark.before("", AuthMiddleware::verificarSesion);
            Spark.before("/*", AuthMiddleware::verificarSesion);

            Spark.before("", ((request, response) -> {
                if(UsuarioHelper.usuarioLogueado(request).getRol() != RolEnum.ORGANIZACION) {
                    response.redirect("/prohibido");
                    Spark.halt();
                }
            }));

            Spark.get("",organizacionController::mostrarInicio, engine);

            Spark.path("/huella-mensual", () -> {
                Spark.get("",organizacionController::mostrarHuellaMensualVista, engine);
                Spark.get("/:mes/:anio",organizacionController::obtenerHuellaMensual);
            });
            Spark.path("/huella-anual", () -> {
                Spark.get("",organizacionController::mostrarHuellaAnualVista, engine);
                Spark.get("/:anio",organizacionController::obtenerHuellaAnual);
            });

            Spark.path("/reportes", () -> {
                Spark.get("",organizacionController::mostrarReportes, engine);
                Spark.get("/composicion-organizacion",organizacionController::mostrarReporteComposicionOrganizacionVista, engine);
                Spark.get("/evolucion-organizacion",organizacionController::mostrarReporteEvolucionOrganizacionVista, engine);
            });

            Spark.path("/mi-organizacion", () -> {
                Spark.get("",organizacionController::mostrarSectoresConMiembros, engine);
                Spark.delete("/:idSector/:idMiembro",organizacionController::eliminarMiembro);
            });

            Spark.path("/miembros-pendientes", () -> {
                Spark.get("",organizacionController::mostrarMiembrosPendientes, engine);
                Spark.post("/:idSector/:idMiembro",organizacionController::aceptarMiembro);
                Spark.delete("/:idSector/:idMiembro",organizacionController::rechazarMiembro);
            });

            Spark.path("/registrar-mediciones", () -> {
                Spark.get("",organizacionController::mostrarRegistrarMedicionesVista, engine);
                Spark.post("",organizacionController::cargarMediciones);
            });

            Spark.get("/recomendaciones",organizacionController::mostrarRecomendaciones, engine);

            Spark.get("/ayuda",organizacionController::mostrarAyudaVista, engine);
        });


        Spark.path("/agentes-sectoriales", () -> {
            Spark.before("", AuthMiddleware::verificarSesion);
            Spark.before("/*", AuthMiddleware::verificarSesion);

            Spark.before("", ((request, response) -> {
                if(UsuarioHelper.usuarioLogueado(request).getRol() != RolEnum.AGENTE_SECTORIAL) {
                    response.redirect("/prohibido");
                    Spark.halt();
                }
            }));

            Spark.get("",agenteSectorialController::mostrarInicio, engine);

            Spark.path("/reportes", () ->{
                Spark.get("",agenteSectorialController::mostrarReportes, engine);
                Spark.get("/huella-total-territorial",agenteSectorialController::mostrarReporteHuellaTotalTerritorial, engine);
                Spark.get("/huella-total-tipo-organizacion",agenteSectorialController::mostrarReporteHuellaTotalPorTipoOrganizacion, engine);
                Spark.get("/composicion-huella-mi-sector",agenteSectorialController::mostrarReporteComposicionHuellaMiSector, engine);
                Spark.get("/composicion-huella-pais",agenteSectorialController::mostrarReporteComposicionPais, engine);
                Spark.get("/composicion-huella-organizacion",agenteSectorialController::mostrarReporteComposicionOrganizacion, engine);
                Spark.get("/evolucion-mi-sector", agenteSectorialController::mostrarReporteEvolucionMiSector, engine);
                Spark.get("/evolucion-organizacion",agenteSectorialController::mostrarReporteEvolucionOrganizacionVista, engine);

            });

            Spark.get("/recomendaciones",agenteSectorialController::mostrarRecomendaciones, engine);

            Spark.get("/ayuda",agenteSectorialController::mostrarAyudaVista, engine);

        });

        Spark.get("*",loginController::notFound, engine);

    }
}
