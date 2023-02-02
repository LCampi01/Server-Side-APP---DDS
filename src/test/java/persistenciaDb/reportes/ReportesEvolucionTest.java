package persistenciaDb.reportes;

import db.EntityManagerHelper;
import models.entities.organizaciones.Organizacion;
import models.entities.reportes.reportesEvolucion.EvolucionOrganizacion;
import models.entities.reportes.reportesEvolucion.EvolucionSectorTerritorial;
import models.entities.reportes.reportesEvolucion.HCAnual;
import models.entities.sectores.Sector;
import models.repositorios.RepositorioDeOrganizacion;
import models.repositorios.RepositorioDeSectorTerritorial;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReportesEvolucionTest {

    @Test
    public void EvolucionHCSectorTerritorial() {
        RepositorioDeSectorTerritorial repositorioDeSectorTerritorial = new RepositorioDeSectorTerritorial();
        Sector sectorTerritorialBsAs = repositorioDeSectorTerritorial.buscar(1);

        System.out.println("\n---- Evolucion de HC para el sector territorial ----");
        EvolucionSectorTerritorial generadorReporte = new EvolucionSectorTerritorial(sectorTerritorialBsAs);
        List<HCAnual> reporte = generadorReporte.reporteEvolucion(2017);

        Assertions.assertEquals(reporte.size(), 6);
        reporte.forEach(r -> System.out.println(r.getAnio()+" -> "+ r.getValorHC()+" "+r.getUnidad()));
    }

    @Test
    public void EvolucionHCOrganizacion() {
        RepositorioDeOrganizacion repositorioDeOrganizacion = new RepositorioDeOrganizacion();
        Organizacion coto = repositorioDeOrganizacion.buscar(1);

        System.out.println("\n---- Evolucion de HC para una organizacion ----");
        EvolucionOrganizacion generadorReporte = new EvolucionOrganizacion(coto);
        List<HCAnual> reporte = generadorReporte.reporteEvolucion(2017);

        Assertions.assertEquals(reporte.size(), 6);
        reporte.forEach(r -> System.out.println(r.getAnio()+" -> "+ r.getValorHC()+" "+r.getUnidad()));
    }
}
