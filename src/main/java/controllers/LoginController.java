package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import db.EntityManagerHelper;
import models.entities.organizaciones.Organizacion;
import models.entities.personas.MiembroDeOrganizacion;
import models.entities.personas.TipoDocumento;
import models.entities.sectores.AgenteSectorial;
import models.entities.usuarios.RolEnum;
import models.entities.usuarios.Usuario;
import models.repositorios.RepositorioDeMiembroDeOrganizacion;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import static models.entities.usuarios.RolEnum.MIEMBRO_ORGANIZACION;

public class LoginController {

    public String parsearString(String string) {
        return string.substring(0,string.length()-1).substring(1);
    }
    public ModelAndView pantallaDeLogin(Request request, Response response) {
        System.out.println("req pantallaDeLogin");
        return new ModelAndView(null, "index.hbs");
    }

    public ModelAndView pantallaDeRegistro(Request request, Response response) {
        System.out.println("req pantallaRegistro");
        return new ModelAndView(null, "registro.hbs");
    }

    public String registro(Request request, Response response) {
        String json = "";
        JsonObject body = new Gson().fromJson(request.body(), JsonObject.class);
        System.out.println("VERIFICANDO EMAIL");

        String email = this.parsearString( body.get("email").toString());
        String contrasenia = this.parsearString(body.get("contrasenia").toString());
        String repetirContrasenia = this.parsearString(body.get("repetirContrasenia").toString());
        String nombre = this.parsearString(body.get("nombre").toString());
        String apellido = this.parsearString(body.get("apellido").toString());
        TipoDocumento tipoDoc = TipoDocumento.valueOf(this.parsearString(body.get("tipoDoc").toString()));
        String documento = this.parsearString(body.get("documento").toString());
        RolEnum rol = MIEMBRO_ORGANIZACION;

        String query = "from "
                + Usuario.class.getName()
                +" WHERE email='"
                + email
                +"'";

        System.out.println(query);

        Usuario usuario = null;
        try {
            usuario = (Usuario) EntityManagerHelper
                    .getEntityManager()
                    .createQuery(query)
                    .getSingleResult();

        }  catch (Exception exception) {
            System.out.println("No existe el usuario");
        }

        if(usuario != null) {
            json = new Gson().toJson("emailInvalido");
            return json;
        }

        if(!contrasenia.equals(repetirContrasenia)) {
            System.out.println(contrasenia);
            System.out.println(repetirContrasenia);
            json = new Gson().toJson("contraseniaInvalida");
            return json;
        }

        Usuario nuevoUsuario = new Usuario(nombre, apellido, email, contrasenia, rol);
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper
                .getEntityManager()
                .persist(nuevoUsuario);
        EntityManagerHelper
                .getEntityManager()
                .persist(nuevoUsuario);
        EntityManagerHelper.commit();

        MiembroDeOrganizacion miembroDeOrganizacion = new MiembroDeOrganizacion(nombre, apellido, tipoDoc, documento);
        miembroDeOrganizacion.setUsuario(nuevoUsuario);
        RepositorioDeMiembroDeOrganizacion repoMiembro = new RepositorioDeMiembroDeOrganizacion();
        repoMiembro.guardar(miembroDeOrganizacion);

        System.out.println(nuevoUsuario);
        json = new Gson().toJson("registroExitoso");
        return json;
    }

    public ModelAndView pantallaDeOlvideContrasena(Request request, Response response) {
        System.out.println("req olvide contra");
        return new ModelAndView(null, "olvide-contrasenia.hbs");
    }
    public Response login(Request request, Response response) {
        try {
            System.out.println("VERIFICANDO LOGIN");
            String query = "from "
                    + Usuario.class.getName()
                    +" WHERE email='"
                    + request.queryParams("email")
                    +"' AND contrasenia='"
                    + request.queryParams("contrasenia")
                    +"'";

            System.out.println(query);
            Usuario usuario = (Usuario) EntityManagerHelper
                    .getEntityManager()
                    .createQuery(query)
                    .getSingleResult();

            System.out.println("sali de la db");
            if(usuario != null) {
                request.session(true);
                request.session().attribute("id", usuario.getId());
                switch (usuario.getRol()){
                    case ORGANIZACION:
                        String queryOrganizacion = "from "
                                + Organizacion.class.getName()
                                +" WHERE usuario_id='"
                                + usuario.getId()
                                +"'";
                        Organizacion organizacion = (Organizacion) EntityManagerHelper
                                .getEntityManager()
                                .createQuery(queryOrganizacion)
                                .getSingleResult();

                        request.session().attribute("idOrganizacion", organizacion.getId());
                        response.redirect("/organizaciones");
                        break;
                    case AGENTE_SECTORIAL:
                        String queryAgente = "from "
                                + AgenteSectorial.class.getName()
                                +" WHERE usuario_id='"
                                + usuario.getId()
                                +"'";
                        AgenteSectorial agenteSectorial = (AgenteSectorial) EntityManagerHelper
                                .getEntityManager()
                                .createQuery(queryAgente)
                                .getSingleResult();

                        request.session().attribute("idAgenteSectorial", agenteSectorial.getId());
                        response.redirect("/agentes-sectoriales");
                        break;
                    case MIEMBRO_ORGANIZACION:
                        System.out.println("MIEMBRO_ORGANIZACION");
                        String queryMiembro = "from "
                                + MiembroDeOrganizacion.class.getName()
                                +" WHERE usuario_id='"
                                + usuario.getId()
                                +"'";
                        MiembroDeOrganizacion miembroDeOrganizacion = (MiembroDeOrganizacion) EntityManagerHelper
                                .getEntityManager()
                                .createQuery(queryMiembro)
                                .getSingleResult();
                        System.out.println(miembroDeOrganizacion.getNombre());

                        request.session().attribute("idMiembroDeOrganizacion", miembroDeOrganizacion.getId());
                        response.redirect("/miembros");
                        break;
                    default:
                        System.out.println("no matcheo rol");
                        response.redirect("/login");
                }
            }

            else {
                response.redirect("/login");
            }
        }
        catch (Exception exception) {
            System.out.println("algo salio mal :(" + exception.getMessage());
            response.redirect("/login");
        }
        return response;
    }

    public Response logout(Request request, Response response) {
        request.session().invalidate();
        response.redirect("/login");
        return response;
    }

    public ModelAndView prohibido(Request request, Response response) {
        return new ModelAndView(null, "prohibido.hbs");
    }


    public ModelAndView notFound(Request request, Response response) {
        return new ModelAndView(null, "404-copy.hbs");
    }
}
