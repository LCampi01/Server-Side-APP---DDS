package models.repositorios;

import db.EntityManagerHelper;
import models.entities.organizaciones.SectorOrganizacion;
import models.entities.personas.MiembroDeOrganizacion;

import java.util.List;

public class RepositorioDeSectorOrganizacion {
    RepositorioDeMiembroDeOrganizacion repositorioDeMiembroDeOrganizacion = new RepositorioDeMiembroDeOrganizacion();

    public SectorOrganizacion buscar(Integer id) {
        return EntityManagerHelper
                .getEntityManager()
                .find(SectorOrganizacion.class, id);
    }

    public void guardar(SectorOrganizacion sectorOrganizacion) {
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper
                .getEntityManager()
                .persist(sectorOrganizacion);
        EntityManagerHelper.commit();
    }


    public List<MiembroDeOrganizacion> buscarMiembrosDeSector(int idSector) {
        return EntityManagerHelper
                .createQuery("SELECT m FROM " + SectorOrganizacion.class.getName() + " s JOIN s.miembros m WHERE s.id = " + idSector)
                .getResultList();
    }

    public void eliminarMiembroDeSector(int idMiembro, int idSector) {
        //solo es eliminarlo del sector para eliminarlo de la organizacion
        EntityManagerHelper.beginTransaction();

        MiembroDeOrganizacion miembro = this.repositorioDeMiembroDeOrganizacion.buscar(idMiembro);
        SectorOrganizacion sector = this.buscar(idSector);
        sector.eliminarMiembro(miembro);

        //lo pongo como para hacer tiempo porque sino a veces fallaba este eliminar,
        // imagino q porque el eliminar en java era más lento q en sql ponele
        this.buscarMiembrosDeSector(idSector);

        EntityManagerHelper
                .getEntityManager()
                .persist(sector);
        EntityManagerHelper.commit();
    }

    public void eliminarMiembroPendienteDeSector(int idMiembro, int idSector) {
        EntityManagerHelper.beginTransaction();

        MiembroDeOrganizacion miembro = this.repositorioDeMiembroDeOrganizacion.buscar(idMiembro);
        SectorOrganizacion sector = this.buscar(idSector);
        sector.eliminarMiembroPendiente(miembro);

        //lo pongo como para hacer tiempo porque sino a veces fallaba este eliminar,
        // imagino q porque el eliminar en java era más lento q en sql ponele
        this.buscarMiembrosDeSector(idSector);

        EntityManagerHelper
                .getEntityManager()
                .persist(sector);
        EntityManagerHelper.commit();
    }
}
