package persistenciaDb;

import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.SectorOrganizacion;
import models.entities.personas.MiembroDeOrganizacion;
import models.entities.personas.Tramo;
import models.entities.personas.Trayecto;
import models.repositorios.*;
import org.junit.jupiter.api.Test;

import java.util.List;

public class reposTest {

    RepositorioDeOrganizacion repoOrga = new RepositorioDeOrganizacion();
    RepositorioDeSectorOrganizacion repoSector = new RepositorioDeSectorOrganizacion();
    RepositorioDeMiembroDeOrganizacion repoMiembro = new RepositorioDeMiembroDeOrganizacion();
    RepositorioDeTrayecto repoTrayecto = new RepositorioDeTrayecto();
    RepositorioDeTramo repoTramo = new RepositorioDeTramo();

    /////////////////// repo organizacion /////////////////////
    @Test
    public void repoOrgaBuscarSectoresDeOrganizacion() { //anda bien
        List<SectorOrganizacion> sectores = repoOrga.buscarSectoresDeOrganizacion(1);
        sectores.forEach(s -> System.out.println(s.getNombre() + " id: " + s.getId()));
    }

    @Test
    public void repoOrgaBuscarOrganizacionDeSector() { //anda bien
        Organizacion organizacion = repoOrga.buscarOrganizacionDeSector(2);
        System.out.println(organizacion.getRazonSocial() + " id: " + organizacion.getId());
    }

    /////////////////// repo sector /////////////////////
    @Test
    public void repoSectorBuscarMiembrosDeSector() {  //anda bien
        List<MiembroDeOrganizacion> miembros = repoSector.buscarMiembrosDeSector(1);
        miembros.forEach(s -> System.out.println(s.getNombre() + " id: " + s.getId()));
    }

    /////////////////// repo miembro /////////////////////
    @Test
    public void repoMiembroBuscarSectores() { //anda bien
        List<SectorOrganizacion> sectores = repoMiembro.buscarSectores(5);
        sectores.forEach(s -> System.out.println(s.getNombre() + " - id: " + s.getId()));
    }

    @Test
    public void repoMiembroBuscarSectoresPendientes() { //anda bien
        List<SectorOrganizacion> sectores = repoMiembro.buscarSectoresPendientes(4);
        sectores.forEach(s -> System.out.println(s.getNombre() + " - id: " + s.getId()));
    }

    @Test
    public void repoMiembroBuscarTodosLosSectores() { //anda bien
        List<SectorOrganizacion> sectores = repoMiembro.buscarTodosLosSectores(4);
        sectores.forEach(s -> System.out.println(s.getNombre() + " - id: " + s.getId()));
    }


    @Test
    public void repoMiembroBuscarOrganizaciones() { //anda bien
        List<Organizacion> orgas = repoMiembro.buscarOrganizaciones(5);
        orgas.forEach(o -> System.out.println(o.getRazonSocial() + " - id: " + o.getId()));
    }

    @Test
    public void repoMiembroBuscarOrganizacionesPendientes() { //anda bien
        List<Organizacion> orgas = repoMiembro.buscarOrganizacionesPendientes(4);
        orgas.forEach(o -> System.out.println(o.getRazonSocial() + " - id: " + o.getId()));
    }

    @Test
    public void repoMiembroBuscarTodasLasOrganizaciones() { //anda bien
        List<Organizacion> orgas = repoMiembro.buscarTodasLasOrganizaciones(4);
        orgas.forEach(o -> System.out.println(o.getRazonSocial() + " - id: " + o.getId()));
    }


    /////////////////// repo trayecto /////////////////////
    @Test
    public void repoTrayectoBuscarTodos() { //anda bien
        List<Trayecto> trayectos = repoTrayecto.buscarTodos(5);
        trayectos.forEach(t -> System.out.println(t.getDescripcion() + " - id: " + t.getId()));
    }

    @Test
    public void repoTrayectoEliminar() { //anda bien
        repoTrayecto.eliminar(1);
    }


    /////////////////// repo tramo /////////////////////
    @Test
    public void repoTramoBuscarTodos() {
        List<Tramo> tramos = repoTramo.buscarTodos(5);
        tramos.forEach(t -> System.out.println("tramo id: " + t.getId()));
    }

    @Test
    public void repoTramoEliminar() { //anda bien
        repoTramo.eliminar(6, 5);
    }

}
