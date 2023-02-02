package models.repositorios;

import db.EntityManagerHelper;
import models.entities.personas.Tramo;
import models.entities.personas.Trayecto;

import java.util.List;

public class RepositorioDeTramo {
    RepositorioDeTrayecto repositorioDeTrayecto = new RepositorioDeTrayecto();

    public Tramo buscar(int id) {
        return EntityManagerHelper
                .getEntityManager()
                .find(Tramo.class, id);
    }

    public List<Tramo> buscarTodos(int idTrayecto) {
        return EntityManagerHelper
                .createQuery("SELECT tram FROM " + Trayecto.class.getName() + " tray JOIN tray.tramos tram WHERE tray.id = " + idTrayecto)
                .getResultList();
    }

    public void guardar(Tramo tramo, Trayecto trayecto) {
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper
                .getEntityManager()
                .persist(tramo);
        EntityManagerHelper
                .getEntityManager()
                .persist(trayecto);
        EntityManagerHelper.commit();
    }

    public void eliminar(int idTramo, int idTrayecto) {
        EntityManagerHelper.beginTransaction();

        Tramo tramoAEliminar = this.buscar(idTramo);

        Trayecto trayectoDelTramo = repositorioDeTrayecto.buscar(idTrayecto);
        trayectoDelTramo.eliminarTramo(tramoAEliminar);

        EntityManagerHelper
                .getEntityManager()
                .persist(trayectoDelTramo);

        EntityManagerHelper.commit();
    }
}
