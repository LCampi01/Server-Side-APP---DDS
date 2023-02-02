package persistenciaDb;

import db.EntityManagerHelper;
import models.entities.organizaciones.ClasificacionDeOrganizacion;
import models.entities.organizaciones.Organizacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class recuperarDatosTest {

    @Test //SIRVE PARA RECUPERAR COSAS DE LA DB, UNA VEZ YA PERSISTIDAS
    public void recuperarCosas() throws Exception {
        EntityManagerHelper.beginTransaction();
        List<ClasificacionDeOrganizacion> clasificacionesOrg = new ArrayList<>();
        Integer id = 1;
        ClasificacionDeOrganizacion clasificacion = EntityManagerHelper.getEntityManager().find(ClasificacionDeOrganizacion.class, id);
        //creo q hay maneras más faciles de hacerlo sin el find
        while (clasificacion != null){
            clasificacionesOrg.add(clasificacion);
            id++;
            clasificacion = EntityManagerHelper.getEntityManager().find(ClasificacionDeOrganizacion.class, id);
        }
        EntityManagerHelper.commit();

        clasificacionesOrg.forEach(c -> System.out.println(c.getNombre()));

    /*
        EntityManagerHelper.beginTransaction();
        List<Sector> sectores = new ArrayList<>();
        Integer idS = 1;
        Sector sector = EntityManagerHelper.getEntityManager().find(Sector.class,new Integer(idS));
        while (sector != null){
            sectores.add(sector);
            id++;
            sector = EntityManagerHelper.getEntityManager().find(Sector.class,new Integer(idS));
        }
        EntityManagerHelper.commit();
        */

    }

    @Test
    public void recuperarMiembros() throws Exception {
        Organizacion coto = (Organizacion) EntityManagerHelper.createQuery("from Organizacion where razon_social='Coto SA'").getSingleResult();
        Integer cantidadMiembros=coto.getSectoresOrganizacion().stream().mapToInt(sectorOrganizacion -> sectorOrganizacion.getMiembros().size()).sum();

        Assertions.assertEquals(cantidadMiembros,4);

    }
}
