package persistenciaDb.reportes;

import models.entities.organizaciones.ClasificacionDeOrganizacion;
import models.entities.organizaciones.Organizacion;
import models.entities.reportes.ReportesHCTotal.HCTotalPorClasificacionDeOrganizacion;
import models.entities.reportes.ReportesHCTotal.HCTotalPorSectorTerritorial;
import models.entities.reportes.ReportesHCTotal.HCTotalSegunAlgo;
import models.entities.sectores.Sector;
import models.repositorios.RepositorioDeClasificacionDeOrganizacion;
import models.repositorios.RepositorioDeSectorTerritorial;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReportesHCTotalTest {
    private RepositorioDeSectorTerritorial repositorioDeSectorTerritorial = new RepositorioDeSectorTerritorial();

    @Test
    public void HCTotalPorSectorTerritorial() {
        List<Sector> sectoresTerritoriales = repositorioDeSectorTerritorial.buscarTodos();

        HCTotalPorSectorTerritorial generadorReporte = new HCTotalPorSectorTerritorial();

        System.out.println("\n---- HC Anual 2022 por Sector territorial ----");
        List<HCTotalSegunAlgo> reporteAnual = generadorReporte.reporteHCTotalAnual(sectoresTerritoriales, 2022);
        reporteAnual.forEach(r -> System.out.println(r.getAlgo()+" -> "+ r.getValorHC()+" "+r.getUnidad()));

        System.out.println("\n---- HC Mensual Mayo 2022 por Sector territorial ----");
        List<HCTotalSegunAlgo> reporteMensual = generadorReporte.reporteHCTotalMensual(sectoresTerritoriales,5, 2022);
        reporteMensual.forEach(r -> System.out.println(r.getAlgo()+" -> "+ r.getValorHC()+" "+r.getUnidad()));
    }

    @Test
    public void HCTotalPorTipoDeOrganizacion() {
        List<Organizacion> organizaciones = repositorioDeSectorTerritorial.buscarOrganizacionesDeSector(1);

        RepositorioDeClasificacionDeOrganizacion repositorioDeClasificacionDeOrganizacion = new RepositorioDeClasificacionDeOrganizacion();
        List<ClasificacionDeOrganizacion> clasificaciones = repositorioDeClasificacionDeOrganizacion.buscarTodos();

        HCTotalPorClasificacionDeOrganizacion generadorReporte = new HCTotalPorClasificacionDeOrganizacion();

        System.out.println("\n---- HC Anual 2022 segun Clasificacion de la organizacion ----");
        List<HCTotalSegunAlgo> reporteAnual = generadorReporte.reporteHCTotalAnual(clasificaciones, organizaciones,2022);
        reporteAnual.forEach(r -> System.out.println(r.getAlgo()+" -> "+ r.getValorHC()+" "+r.getUnidad()));

        System.out.println("\n\n---- HC Mensual Mayo 2022 segun Clasificacion de la organizacion ----");
        List<HCTotalSegunAlgo> reporteMensual = generadorReporte.reporteHCTotalMensual(clasificaciones, organizaciones,5,2022);
        reporteMensual.forEach(r -> System.out.println(r.getAlgo()+" -> "+ r.getValorHC()+" "+r.getUnidad()));
    }
}
