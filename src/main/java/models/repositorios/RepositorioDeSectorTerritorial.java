package models.repositorios;

import db.EntityManagerHelper;
import models.entities.organizaciones.Organizacion;
import models.entities.sectores.Sector;

import java.util.List;

public class RepositorioDeSectorTerritorial {

    public Sector buscar(Integer id) {
        return EntityManagerHelper
                .getEntityManager()
                .find(Sector.class, id);
    }

    public List<Sector> buscarTodos() {
        return EntityManagerHelper
                .getEntityManager()
                .createQuery("from " + Sector.class.getName())
                .getResultList();
    }

    public List<Organizacion> buscarOrganizacionesDeSector(int idSector) {
        return EntityManagerHelper
                .createQuery("SELECT o FROM " + Sector.class.getName() + " s JOIN s.organizaciones o WHERE s.id = " + idSector)
                .getResultList();
    }

    public List<Sector> buscarSectoresDeProvincias() {
        return EntityManagerHelper
                .createQuery("SELECT s FROM " + Sector.class.getName()
                        + " s JOIN s.territorio t WHERE tipo_territorio = 'provincia'")
                .getResultList();
    }

}
