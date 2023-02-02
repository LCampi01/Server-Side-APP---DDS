package models.repositorios;

import db.EntityManagerHelper;
import models.entities.personas.MiembroDeOrganizacion;
import models.entities.personas.Trayecto;

import java.util.List;

public class RepositorioDeTrayecto {

    public Trayecto buscar(int id) {
        return EntityManagerHelper
                .getEntityManager()
                .find(Trayecto.class, id);
    }

    public List<Trayecto> buscarTodos(int idMiembro) {
        return EntityManagerHelper
                .createQuery("FROM " + Trayecto.class.getName() + " WHERE miembro_id = " + idMiembro)
                .getResultList();
    }

    public void guardar(Trayecto trayecto, MiembroDeOrganizacion miembroDeOrganizacion) {
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper
                .getEntityManager()
                .persist(trayecto);
        EntityManagerHelper
                .getEntityManager()
                .persist(miembroDeOrganizacion);
        EntityManagerHelper.commit();
    }

    public void eliminar(int id) {
        EntityManagerHelper.beginTransaction();
        Trayecto trayectoAEliminar = this.buscar(id);
        EntityManagerHelper
                .getEntityManager()
                .remove(trayectoAEliminar);
        EntityManagerHelper.commit();
    }

}
