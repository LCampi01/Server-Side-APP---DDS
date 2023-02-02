package models.repositorios;

import db.EntityManagerHelper;
import models.entities.ubicaciones.territorio.Departamento;

import java.util.List;

public class RepositorioDeDepartamento {
    public Departamento buscar(int id) {
        return EntityManagerHelper
                .getEntityManager()
                .find(Departamento.class, id);
    }

    public List<Departamento> buscarTodos() {
        return EntityManagerHelper
                .createQuery("FROM " + Departamento.class.getName())
                .getResultList();
    }

    public void guardar(Departamento departamento) {
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper
                .getEntityManager()
                .persist(departamento);
        EntityManagerHelper.commit();
    }

}
