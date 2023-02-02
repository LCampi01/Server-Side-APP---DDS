package models.repositorios;

import db.EntityManagerHelper;
import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.SectorOrganizacion;
import models.entities.organizaciones.datoDeActividad.DatoDeActividad;
import models.entities.personas.MiembroDeOrganizacion;

import java.util.List;

public class RepositorioDeOrganizacion {

    public Organizacion buscar(Integer id) {
        return EntityManagerHelper
                .getEntityManager()
                .find(Organizacion.class, id);
    }

    public List<Organizacion> buscarTodos() { //creo q nunca se usa
        return EntityManagerHelper
                .getEntityManager()
                .createQuery("from " + Organizacion.class.getName())
                .getResultList();
    }

    public void guardar(Organizacion organizacion) {
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper
                .getEntityManager()
                .persist(organizacion);
        EntityManagerHelper.commit();
    }

    public void guardarDatosDeActividad(Organizacion organizacion, List<DatoDeActividad> datosActividades) {
        Integer organizacionId = organizacion.getId();

        for(DatoDeActividad dato : datosActividades){
            EntityManagerHelper.beginTransaction();
            EntityManagerHelper.getEntityManager().persist(dato);

            EntityManagerHelper
                    .createQuery("update " + DatoDeActividad.class.getName()
                            + " set organizacion_id = " + organizacionId
                            + " where id = " + dato.getId()
                    ).executeUpdate();
            EntityManagerHelper.commit();
        }
    }

    public void eliminar(int idOrg) {
        EntityManagerHelper.beginTransaction();
        Organizacion orgAEliminar = this.buscar(idOrg);
        EntityManagerHelper
                .getEntityManager()
                .remove(orgAEliminar);
        EntityManagerHelper.commit();
    }


    public List<SectorOrganizacion> buscarSectoresDeOrganizacion(int idOrg) {
        return EntityManagerHelper
                .createQuery("from " + SectorOrganizacion.class.getName() + " where organizacion_id = " + idOrg)
                .getResultList();
    }

    public Organizacion buscarOrganizacionDeSector(int idSector){
        //devolver la organizacion de ese sector
        return (Organizacion) EntityManagerHelper
                .createQuery("SELECT o FROM " + Organizacion.class.getName() + " o JOIN o.sectoresOrganizacion s where s.id = " + idSector)
                .getSingleResult();
    }

}
