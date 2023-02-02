package helpers;

import db.EntityManagerHelper;
import models.entities.organizaciones.Organizacion;
import models.entities.personas.MiembroDeOrganizacion;
import models.entities.sectores.AgenteSectorial;
import models.entities.usuarios.Usuario;
import models.repositorios.RepositorioDeAgenteSectorial;
import models.repositorios.RepositorioDeMiembroDeOrganizacion;
import models.repositorios.RepositorioDeOrganizacion;
import spark.Request;

public class UsuarioHelper {

    public static Usuario usuarioLogueado(Request request) {
        return EntityManagerHelper
                .getEntityManager()
                .find(Usuario.class, request.session().attribute("id"));
    }

    public static MiembroDeOrganizacion obtenerMiembro(Request request){
        Integer idMiembro = request.session().attribute("idMiembroDeOrganizacion");
        return new RepositorioDeMiembroDeOrganizacion().buscar(idMiembro);
    }

    public static Organizacion obtenerOrganizacion(Request request){
        Integer idOrganizacion = request.session().attribute("idOrganizacion");
        return new RepositorioDeOrganizacion().buscar(idOrganizacion);
    }

    public static AgenteSectorial obtenerAgenteSectorial(Request request){
        Integer idAgenteSectorial = request.session().attribute("idAgenteSectorial");
        return new RepositorioDeAgenteSectorial().buscar(idAgenteSectorial);
    }
}
