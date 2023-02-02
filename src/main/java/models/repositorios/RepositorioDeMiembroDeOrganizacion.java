package models.repositorios;

import db.EntityManagerHelper;
import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.SectorOrganizacion;
import models.entities.personas.MiembroDeOrganizacion;

import java.util.List;

public class RepositorioDeMiembroDeOrganizacion {

    public MiembroDeOrganizacion buscar(int id) {
        return EntityManagerHelper
                .getEntityManager()
                .find(MiembroDeOrganizacion.class, id);
    }

    public List<MiembroDeOrganizacion> buscarTodos(int idSector) {
        return EntityManagerHelper
                .createQuery("select m FROM " + SectorOrganizacion.class.getName() + " s JOIN s.miembros m WHERE s.id = " + idSector)
                .getResultList();
    }

    public void guardar(MiembroDeOrganizacion miembro) {
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper
                .getEntityManager()
                .persist(miembro);
        EntityManagerHelper.commit();
    }

    public void eliminar(int idMiembro) {
        EntityManagerHelper.beginTransaction();
        MiembroDeOrganizacion miembroAEliminar = this.buscar(idMiembro);
        EntityManagerHelper
                .getEntityManager()
                .remove(miembroAEliminar);
        EntityManagerHelper.commit();
    }


    public List<SectorOrganizacion> buscarSectores(int idMiembro) {
        return EntityManagerHelper
                .createQuery("SELECT s FROM " + SectorOrganizacion.class.getName() + " s JOIN s.miembros m WHERE m.id = " + idMiembro)
                .getResultList();
    }

    public List<SectorOrganizacion> buscarSectoresPendientes(int idMiembro) {
        return EntityManagerHelper
                .createQuery("SELECT s FROM " + SectorOrganizacion.class.getName() + " s JOIN s.miembrosPendientes m WHERE m.id = " + idMiembro)
                .getResultList();
    }


    public List<SectorOrganizacion> buscarTodosLosSectores(int idMiembro) {
        List<SectorOrganizacion> sectores = this.buscarSectores(idMiembro);
        sectores.addAll(this.buscarSectoresPendientes(idMiembro));
        return sectores;
    }


    public List<Organizacion> buscarOrganizaciones(int idMiembro) {
        return EntityManagerHelper
                .createQuery("SELECT o FROM " + Organizacion.class.getName()
                        + " o JOIN o.sectoresOrganizacion s JOIN s.miembros m WHERE m.id = " + idMiembro)
                .getResultList();
    }

    public List<Organizacion> buscarOrganizacionesPendientes(int idMiembro) {
        return EntityManagerHelper
                .createQuery("SELECT o FROM " + Organizacion.class.getName()
                        + " o JOIN o.sectoresOrganizacion s JOIN s.miembrosPendientes m WHERE m.id = " + idMiembro)
                .getResultList();
    }

    public List<Organizacion> buscarTodasLasOrganizaciones(int idMiembro) {
        List<Organizacion> organizaciones = this.buscarOrganizaciones(idMiembro);
        organizaciones.addAll(this.buscarOrganizacionesPendientes(idMiembro));
        return organizaciones;
    }
}
