package entities.organizaciones;

import models.entities.organizaciones.ClasificacionDeOrganizacion;
import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.SectorOrganizacion;
import models.entities.organizaciones.TipoDeOrganizacion;
import models.entities.personas.MiembroDeOrganizacion;
import models.entities.personas.TipoDocumento;
import models.entities.sectores.BuscadorDeSectores;
import models.entities.sectores.Sector;
import models.entities.ubicaciones.territorio.Departamento;
import models.entities.ubicaciones.ubicacion.Direccion;
import models.entities.ubicaciones.territorio.Provincia;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VinculacionMiembroTest {

    Provincia bsAs = new Provincia("Buenos Aires");
    Departamento quilmes = new Departamento("Quilmes",bsAs);

    Sector sectorProvinciaBsAs = new Sector("provincia Bs As", bsAs);
    Sector sectorDepartamentoQuilmes = new Sector("departamento Quilmes de Bs As", quilmes);

    ClasificacionDeOrganizacion supermercado = new ClasificacionDeOrganizacion("Supermercados");
    Direccion ubicacionGenerica = new Direccion(quilmes,"San Martin",455);
    
    Organizacion coto = new Organizacion("Coto SA", TipoDeOrganizacion.EMPRESA, supermercado, ubicacionGenerica);

    SectorOrganizacion caja = new SectorOrganizacion("Caja");
    SectorOrganizacion marketing = new SectorOrganizacion("Marketing");

    MiembroDeOrganizacion miembro1 = new MiembroDeOrganizacion(TipoDocumento.DNI, "11111111");
    MiembroDeOrganizacion miembro2 = new MiembroDeOrganizacion(TipoDocumento.DNI, "22222222");

    @BeforeEach
    public void init () {
        BuscadorDeSectores.instancia().agregarSectores(sectorProvinciaBsAs, sectorDepartamentoQuilmes);
    }


    @Test
    public void cantidadSectoresDeOrganizacion() {
        coto.agregarSectoresOrganizacion(caja, marketing);
        Assertions.assertEquals(2,coto.getSectoresOrganizacion().size());
    }

    @Test
    public void solicitarVinculacionAOrganizacion() throws IOException {
        coto.agregarSectoresOrganizacion(marketing);
        miembro1.solicitarVinculacion(coto, marketing);
        miembro2.solicitarVinculacion(coto, marketing);
        Assertions.assertEquals(2, marketing.getMiembrosPendientes().size());
    }

    @Test
    public void solicitarVinculacionAOrganizacionConSectorNoPertenecienteAOrganizacion() throws IOException {
        coto.agregarSectoresOrganizacion(marketing);

        Exception exception = Assertions.assertThrows(
                IOException.class,
                () -> { miembro1.solicitarVinculacion(coto, caja); }
        );

        assertEquals("El sector ingresado no pertenece a esta organizacion", exception.getMessage());
        Assertions.assertEquals(0, caja.getMiembrosPendientes().size());
    }

    @Test
    public void organizacionAceptaUnaVinculacion() throws IOException {
        coto.agregarSectoresOrganizacion(marketing);
        miembro1.solicitarVinculacion(coto, marketing);
        miembro2.solicitarVinculacion(coto, marketing);
        coto.aceptarVinculacion(miembro1, marketing);

        Assertions.assertEquals(1, marketing.getMiembros().size());
        Assertions.assertEquals(1, marketing.getMiembrosPendientes().size());
    }

    @Test
    public void organizacionNoAceptaVinculacionDeSectorQueNoLePertenece() throws IOException {
        coto.agregarSectoresOrganizacion(marketing);
        miembro1.solicitarVinculacion(coto, marketing);

        Exception exception = Assertions.assertThrows(
                IOException.class,
                () -> { coto.aceptarVinculacion(miembro1, caja); }
        );

        assertEquals("El sector ingresado no pertenece a esta organizacion", exception.getMessage());

        Assertions.assertEquals(0, marketing.getMiembros().size());
        Assertions.assertEquals(1, marketing.getMiembrosPendientes().size());

        Assertions.assertEquals(0, caja.getMiembros().size());
        Assertions.assertEquals(0, caja.getMiembrosPendientes().size());
    }

    @Test
    public void organizacionNoAceptaVinculacionNoSolicitada() throws IOException {
        coto.agregarSectoresOrganizacion(marketing, caja);
        miembro1.solicitarVinculacion(coto, marketing);

        Exception exception = Assertions.assertThrows(
                IOException.class,
                () -> { coto.aceptarVinculacion(miembro1, caja); }
        );

        assertEquals("No se puede agregar un miembro no pendiente a la organizacion", exception.getMessage());

        Assertions.assertEquals(0, marketing.getMiembros().size());
        Assertions.assertEquals(1, marketing.getMiembrosPendientes().size());

        Assertions.assertEquals(0, caja.getMiembros().size());
        Assertions.assertEquals(0, caja.getMiembrosPendientes().size());
    }

}
