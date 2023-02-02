package models.repositorios;

import db.EntityManagerHelper;
import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.SectorOrganizacion;
import models.entities.sectores.AgenteSectorial;

import java.util.List;

public class RepositorioDeAgenteSectorial {

    public AgenteSectorial buscar(Integer id) {
        return EntityManagerHelper
                .getEntityManager()
                .find(AgenteSectorial.class, id);
    }

    public List<Organizacion> buscarOrganizaciones(int idAgente) {
        List<Organizacion> organizacions = EntityManagerHelper
                .createQuery("SELECT o FROM " + AgenteSectorial.class.getName()
                        + " a JOIN a.sector s JOIN s.organizaciones o WHERE a.id = " + idAgente)
                .getResultList();
        return  organizacions;
    }
}
