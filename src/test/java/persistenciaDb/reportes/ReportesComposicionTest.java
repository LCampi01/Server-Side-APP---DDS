package persistenciaDb.reportes;

import db.EntityManagerHelper;
import models.entities.organizaciones.Organizacion;
import models.entities.reportes.reportesComposicion.ComposicionHCOrganizacion;
import models.entities.reportes.reportesComposicion.ComposicionHCPorPais;
import models.entities.reportes.reportesComposicion.ComposicionHCSectorTerritorial;
import models.entities.reportes.reportesComposicion.PorcentajeHC;
import models.entities.sectores.Sector;
import models.entities.ubicaciones.territorio.Territorio;
import models.repositorios.RepositorioDeOrganizacion;
import models.repositorios.RepositorioDeSectorTerritorial;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.List;

public class ReportesComposicionTest {
    //para truncar en dos decimales
    DecimalFormat df = new DecimalFormat("#.00");
    RepositorioDeSectorTerritorial repositorioDeSectorTerritorial = new RepositorioDeSectorTerritorial();
    RepositorioDeOrganizacion repositorioDeOrganizacion = new RepositorioDeOrganizacion();

    @Test
    public void ComposicionHCDeSectorTerritorial() {
        Sector sectorParticular = repositorioDeSectorTerritorial.buscar(1);

        ComposicionHCSectorTerritorial generadorReporte = new ComposicionHCSectorTerritorial();

        System.out.println("\n---- Composicion de HC Anual 2022 para el sector territorial " + sectorParticular.getNombre() + " ----");
        List<PorcentajeHC> reporteAnual = generadorReporte.reporteComposicionSectorTerritorialAnual(sectorParticular, 2022);
        reporteAnual.forEach(r -> System.out.println(r.getNombre()+" -> "+ df.format(r.getPorcentaje()) +"%"));

        System.out.println("\n\n---- Composicion de HC Mensual Mayo 2022 para el sector territorial " + sectorParticular.getNombre() + " ----");
        List<PorcentajeHC> reporteMensual = generadorReporte.reporteComposicionSectorTerritorialMensual(sectorParticular,5, 2022);
        reporteMensual.forEach(r -> System.out.println(r.getNombre()+" -> "+ df.format(r.getPorcentaje()) +"%"));
    }

    @Test
    public void ComposicionHCPais() {
        List<Sector> sectores = repositorioDeSectorTerritorial.buscarSectoresDeProvincias();

        ComposicionHCPorPais generadorReporte = new ComposicionHCPorPais();

        System.out.println("\n---- Composicion de HC Anual 2022 para Argentina ----");
        List<PorcentajeHC> reporteAnual = generadorReporte.reporteComposicionPaisAnual(sectores, 2022);
        reporteAnual.forEach(r -> System.out.println(r.getNombre()+" -> "+ df.format(r.getPorcentaje()) +"%"));

        System.out.println("\n\n---- Composicion de HC Mensual Mayo 2022 para Argentina ----");
        List<PorcentajeHC> reporteMensual = generadorReporte.reporteComposicionPaisMensual(sectores,5, 2022);
        reporteMensual.forEach(r -> System.out.println(r.getNombre()+" -> "+ df.format(r.getPorcentaje()) +"%"));
    }

    @Test
    public void ComposicionHCOrganizacion() {
        Organizacion coto = repositorioDeOrganizacion.buscar(1);

        ComposicionHCOrganizacion generadorReporte = new ComposicionHCOrganizacion();

        System.out.println("\n---- Composicion de HC Anual 2022 de Coto ----");
        List<PorcentajeHC> reporteAnual = generadorReporte.reporteComposicionOrganizacionAnual(coto,2022);
        reporteAnual.forEach(r -> System.out.println(r.getNombre()+" -> "+ df.format(r.getPorcentaje()) +"%"));

        System.out.println("\n---- Composicion de HC Mensual Mayo 2022 de Coto ----");
        List<PorcentajeHC> reporteMensual = generadorReporte.reporteComposicionOrganizacionMensual(coto,5,2022);
        reporteMensual.forEach(r -> System.out.println(r.getNombre()+" -> "+ df.format(r.getPorcentaje()) +"%"));
    }

}

