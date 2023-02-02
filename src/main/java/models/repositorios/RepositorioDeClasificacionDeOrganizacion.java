package models.repositorios;

import db.EntityManagerHelper;
import models.entities.organizaciones.ClasificacionDeOrganizacion;

import java.util.List;

public class RepositorioDeClasificacionDeOrganizacion {

    public List<ClasificacionDeOrganizacion> buscarTodos() {
        return EntityManagerHelper
                .getEntityManager()
                .createQuery("from " + ClasificacionDeOrganizacion.class.getName())
                .getResultList();
    }
}
