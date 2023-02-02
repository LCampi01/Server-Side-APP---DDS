package entities.organizaciones;

import models.entities.organizaciones.CargadorDeMediciones;
import models.entities.organizaciones.ClasificacionDeOrganizacion;
import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.TipoDeOrganizacion;
import models.entities.organizaciones.datoDeActividad.Actividad;
import models.entities.organizaciones.datoDeActividad.Alcance;
import models.entities.organizaciones.datoDeActividad.TipoDeConsumo;
import models.entities.sectores.BuscadorDeSectores;
import models.entities.sectores.Sector;
import models.entities.ubicaciones.territorio.Departamento;
import models.entities.ubicaciones.ubicacion.Direccion;
import models.entities.ubicaciones.territorio.Provincia;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CargaDeActividadesTest {

    static Provincia bsAs = new Provincia("Buenos Aires");
    static Departamento quilmes = new Departamento("Quilmes",bsAs);

    static Sector sectorProvinciaBsAs = new Sector("provincia Bs As", bsAs);
    static Sector sectorDepartamentoQuilmes = new Sector("departamento Quilmes de Bs As", quilmes);

    ClasificacionDeOrganizacion supermercado = new ClasificacionDeOrganizacion("Supermercados");
    Direccion direccion = new Direccion(quilmes,"San Martin",455);

    Organizacion coto = new Organizacion("Coto SA", TipoDeOrganizacion.EMPRESA, supermercado, direccion);

    static Actividad combustionFija = new Actividad("Combustion fija", Alcance.EMISIONES_DIRECTAS);
    static TipoDeConsumo gasNatural = new TipoDeConsumo("Gas Natural", "m3");
    static TipoDeConsumo dieselGasoil = new TipoDeConsumo("Dieses/Gasoil", "lt");
    static TipoDeConsumo kerosene = new TipoDeConsumo("Kerosene", "lt");

    static Actividad combustionMovil = new Actividad("Combustion movil", Alcance.EMISIONES_DIRECTAS);
    static TipoDeConsumo gasoil = new TipoDeConsumo("Combustible consumido - Gasoil", "lts");
    static TipoDeConsumo gnc = new TipoDeConsumo("Combustible consumido - GNC", "lts");
    static TipoDeConsumo nafta = new TipoDeConsumo("Combustible consumido - Nafta", "lts");

    static CargadorDeMediciones lectorExcel;

    @BeforeAll
    static void init() {
        combustionFija.agregarTiposDeConsumo(gasNatural,dieselGasoil,kerosene);
        combustionMovil.agregarTiposDeConsumo(gasoil,gnc,nafta);

        lectorExcel = CargadorDeMediciones.instancia();
        lectorExcel.agregarActividades(combustionFija,combustionMovil);

        BuscadorDeSectores.instancia().agregarSectores(sectorProvinciaBsAs, sectorDepartamentoQuilmes);
    }

    @Test
    public void cotoCargaActividadesValidas() throws IOException {
        String path = "src/main/resources/excelActividades/excelActividades.xlsx";
        lectorExcel.cargarDatosActividadesEnOrganizacion(path, coto);

        assertEquals(3, coto.getDatosDeActividad().size());
    }

    @Test
    public void cotoCargaActividadInexistente() throws IOException {
        String path = "src/main/resources/excelActividades/excelConActividadInexistente.xlsx";

        Exception exception = assertThrows(
            IOException.class,
            () -> lectorExcel.cargarDatosActividadesEnOrganizacion(path, coto)
        );

        assertEquals("La actividad ingresada no es valida", exception.getMessage());
    }

    @Test
    public void cotoCargaTipoDeConsumoInexistente() throws IOException {
        String path = "src/main/resources/excelActividades/excelConTipoDeConsumoInexistente.xlsx";

        Exception exception = assertThrows(
                IOException.class,
                () -> lectorExcel.cargarDatosActividadesEnOrganizacion(path, coto)
        );

        assertEquals("El tipo de consumo ingresado no existe o no corresponde a la actividad", exception.getMessage());
    }

    @Test
    public void cotoCargaTipoDeConsumoDeOtraActividad() throws IOException {
        String path = "src/main/resources/excelActividades/excelConTipoDeConsumoDeOtraActividad.xlsx";

        Exception exception = assertThrows(
                IOException.class,
                () -> lectorExcel.cargarDatosActividadesEnOrganizacion(path, coto)
        );

        assertEquals("El tipo de consumo ingresado no existe o no corresponde a la actividad", exception.getMessage());
    }
}
