package models.repositorios;

import db.EntityManagerHelper;
import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.datoDeActividad.Actividad;
import models.entities.ubicaciones.territorio.Departamento;

import java.util.List;

public class RepositorioDeActividad {

    public List<Actividad> buscarTodos() {
        List<Actividad> actividads = EntityManagerHelper
                .createQuery("FROM " + Actividad.class.getName())
                .getResultList();
        return actividads;
    }

}
