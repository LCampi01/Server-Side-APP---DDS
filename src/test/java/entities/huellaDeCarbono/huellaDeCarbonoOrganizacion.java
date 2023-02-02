package entities.huellaDeCarbono;

import models.entities.factorDeEmision.BuscadorDeFactorDeEmision;
import models.entities.factorDeEmision.FactorDeEmision;
import models.entities.organizaciones.*;
import models.entities.organizaciones.datoDeActividad.Actividad;
import models.entities.organizaciones.datoDeActividad.Alcance;
import models.entities.organizaciones.datoDeActividad.TipoDeConsumo;
import models.entities.personas.MiembroDeOrganizacion;
import models.entities.personas.TipoDocumento;
import models.entities.personas.Tramo;
import models.entities.personas.Trayecto;
import models.entities.sectores.BuscadorDeSectores;
import models.entities.sectores.Sector;
import models.entities.transportes.*;
import models.entities.ubicaciones.territorio.Departamento;
import models.entities.ubicaciones.ubicacion.Direccion;
import models.entities.ubicaciones.ubicacion.Parada;
import models.entities.ubicaciones.territorio.Provincia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class huellaDeCarbonoOrganizacion {
    IAdapterCalculadoraDistancia adapterMock;

    static Provincia bsAs = new Provincia("Buenos Aires");
    static Departamento quilmes = new Departamento("Quilmes",bsAs);
    static Departamento lanus = new Departamento("Lanus",bsAs);

    Sector sector1 = new Sector("provincia Bs As", bsAs);
    Sector sector2 = new Sector("departamento Lanus de Bs As", lanus);
    Sector sector3 = new Sector("departamento Quilmes de Bs As", quilmes);

    ClasificacionDeOrganizacion supermercado = new ClasificacionDeOrganizacion("Supermercados");
    Direccion direccion = new Direccion(quilmes,"Colon",65);

    Organizacion coto = new Organizacion("Coto SA", TipoDeOrganizacion.EMPRESA, supermercado, direccion);

    MiembroDeOrganizacion miembro1 = new MiembroDeOrganizacion(TipoDocumento.DNI, "4565654");

    MiembroDeOrganizacion miembro2 = new MiembroDeOrganizacion(TipoDocumento.DNI, "2525526");

    VehiculoParticular vehiculoParticular = new VehiculoParticular(TipoDeVehiculo.CAMIONETA, TipoDeCombustible.GNC);
    SinVehiculo sinVehiculo = new SinVehiculo();

    SectorOrganizacion sectorOrganizacion1 = new SectorOrganizacion("marketing");


    TransportePublico subteA = new TransportePublico(TipoTransportePublico.SUBTE, "A");
    Parada paradaA = new Parada("paradaA", 0.0, 7.0);
    Parada paradaB = new Parada("paradaB", 7.0, 10.0);
    Parada paradaC = new Parada("paradaC", 10.0, 0.0);

    FactorDeEmision feVP = new FactorDeEmision(22.0, "kgCO2eq/km");
    FactorDeEmision feSubte = new FactorDeEmision(45.0, "kgCO2eq/km");

    Tramo tramoA = new Tramo(sinVehiculo, new Direccion(quilmes,"San Martin",45),new Direccion(quilmes,"Colon",65));
    Tramo tramoB = new Tramo(vehiculoParticular, new Direccion(lanus,"San Martin",45),new Direccion(lanus,"Colon",500));
    Tramo tramoC = new Tramo(subteA, paradaA,paradaC);

    LocalDate date = LocalDate.now();

    Trayecto trayecto1 = new Trayecto(coto,"Ir a coto",10,date.withYear(2019).withMonth(2));
    Trayecto trayecto2 = new Trayecto(coto,"Ir a coto",5,date.withYear(2019).withMonth(2));



    Actividad combustionFija = new Actividad("Combustion fija", Alcance.EMISIONES_DIRECTAS);
    TipoDeConsumo gasNatural = new TipoDeConsumo("Gas Natural", "m3");
    TipoDeConsumo dieselGasoil = new TipoDeConsumo("Dieses/Gasoil", "lt");
    TipoDeConsumo kerosene = new TipoDeConsumo("Kerosene", "lt");

    Actividad combustionMovil = new Actividad("Combustion movil", Alcance.EMISIONES_DIRECTAS);
    TipoDeConsumo gasoil = new TipoDeConsumo("Combustible consumido - Gasoil", "lts");
    TipoDeConsumo gnc = new TipoDeConsumo("Combustible consumido - GNC", "lts");
    TipoDeConsumo nafta = new TipoDeConsumo("Combustible consumido - Nafta", "lts");

    Actividad electricidadAdquirida = new Actividad("Electricidad adquirida y consumida", Alcance.EMISIONES_INDIRECTAS_ELECTRICIDAD);
    TipoDeConsumo electricidad = new TipoDeConsumo("Electricidad", "Kwh");

    CargadorDeMediciones lectorExcel;


    @BeforeEach
    public void init () throws IOException {
        this.adapterMock = mock(IAdapterCalculadoraDistancia.class);
        this.vehiculoParticular.setAdapter(this.adapterMock);
        this.sinVehiculo.setAdapter(this.adapterMock);

        BuscadorDeSectores.instancia().agregarSectores(sector1, sector2, sector3);
        coto.cargarSectores(); //creo q no hace falta pq new Organizacion ya llama a cargarSectores()

        coto.agregarSectoresOrganizacion(sectorOrganizacion1);

        sectorOrganizacion1.agregarMiembrosPendientes(miembro1, miembro2);

        sectorOrganizacion1.agregarMiembro(miembro1);
        sectorOrganizacion1.agregarMiembro(miembro2);

        subteA.agregarParadas(paradaA,paradaB,paradaC);

        trayecto1.agregarTramo(tramoA);
        trayecto1.agregarTramo(tramoB);
        trayecto2.agregarTramo(tramoB);
        trayecto2.agregarTramo(tramoC);

        vehiculoParticular.setFactorDeEmision(feVP);
        subteA.setFactorDeEmision(feSubte);

        combustionFija.agregarTiposDeConsumo(gasNatural,dieselGasoil,kerosene);
        combustionMovil.agregarTiposDeConsumo(gasoil,gnc,nafta);
        electricidadAdquirida.agregarTiposDeConsumo(electricidad);
        lectorExcel = CargadorDeMediciones.instancia();
        lectorExcel.agregarActividades(combustionFija,combustionMovil,electricidadAdquirida);


        FactorDeEmision feElectricidad = new FactorDeEmision(15.0,electricidad);
        FactorDeEmision feNafta = new FactorDeEmision(23.0,nafta);
        FactorDeEmision feKerosene = new FactorDeEmision(56.0,kerosene);
        BuscadorDeFactorDeEmision.instancia().agregarFactores(feElectricidad,feKerosene,feNafta);
    }

    @Test
    public void calculoHuellaCarbonoMensualMiembro() throws IOException {
        miembro1.agregarTrayectos(trayecto1); //trayecto con dos tramos: uno sinvehiculo, otro vehiculo particular
        when(this.vehiculoParticular.distancia(tramoB)).thenReturn(25.0);
        /*
        huella de carbono de ese miembro = hc tramo a  + hc tramo b
        tramo a hc = 0
        tramo b hc = 25 *  22 = 550
         */
        assertEquals(2750.0,miembro1.huellaDeCarbonoMensual("2","2022").getValor());
    }

    @Test
    public void calculoHuellaCarbonoMensualTramoCompartido() throws IOException {
        miembro1.agregarTrayectos(trayecto1); //trayecto con dos tramos: uno sinvehiculo, otro vehiculo particular
        miembro2.agregarTrayectos(trayecto2); // trayecto ccon dos tramos, un subte un vp
        when(this.vehiculoParticular.distancia(tramoB)).thenReturn(25.0);
        /*
        huella de carbono de miembro 1= hc tramo a  + hc tramo b
        tramo a hc = 0
        tramo b hc = 25 *  22 = 550 pero como compartido 275

        huella de carbono de miembro 2= hc tramo c  + hc tramo b
        tramo c hc = 17(dist de paradas) * 45(fe) = 765
        tramo b hc = 25 *  22 = 550 pero como compartido 275
         */
        assertEquals(2750.0,miembro1.huellaDeCarbonoMensual("2","2022").getValor());

        assertEquals(5200.0,miembro2.huellaDeCarbonoMensual("2","2022").getValor());
    }

    @Test
    public void calculoHuellaCarbonoOrganizacionMensual() throws IOException {
        miembro1.agregarTrayectos(trayecto1); //trayecto con dos tramos: uno sinvehiculo, otro vehiculo particular
        miembro2.agregarTrayectos(trayecto2); // trayecto ccon dos tramos, un subte un vp
        when(this.vehiculoParticular.distancia(tramoB)).thenReturn(25.0);
        /*
        huella de carbono de los trayectos de los miembros = 1315
         */

        String path = "src/main/resources/excelActividades/excelActividadesTestHC.xlsx";
        lectorExcel.cargarDatosActividadesEnOrganizacion(path, coto);
        /*
        huella de carbono de los DA
        electricidad es del anio = 15 * 50 / 12 = 62.5
        nafta es del mes = 100 * 23 = 2300
        kerosene es de otro periodo
         */
        assertEquals(10312.5,coto.huellaDeCarbonoMensual("2","2020").getValor());

    }

    @Test
    public void calculoHuellaCarbonoOrganizacionAnual() throws IOException {
        miembro1.agregarTrayectos(trayecto1); //trayecto con dos tramos: uno sinvehiculo, otro vehiculo particular
        miembro2.agregarTrayectos(trayecto2); // trayecto ccon dos tramos, un subte un vp
        when(this.vehiculoParticular.distancia(tramoB)).thenReturn(25.0);
        /*
        huella de carbono de los trayectos de los miembros = 1315
         */

        String path = "src/main/resources/excelActividades/excelActividadesTestHC.xlsx";
        lectorExcel.cargarDatosActividadesEnOrganizacion(path, coto);
        /*
        huella de carbono de los DA
        electricidad es del anio = 15 * 50 = 750
        nafta es del mes = 100 * 23 = 2300
        kerosene es de otro periodo
         */
        assertEquals(98450.0,coto.huellaDeCarbonoAnual("2020").getValor());

    }

    @Test
    public void huellaTrayectoMensualOrganizacional() throws IOException {
        miembro1.agregarTrayectos(trayecto1); //trayecto con dos tramos: uno sinvehiculo, otro vehiculo particular
        miembro2.agregarTrayectos(trayecto2); // trayecto ccon dos tramos, un subte un vp
        when(this.vehiculoParticular.distancia(tramoB)).thenReturn(25.0);
        /*
        huella de carbono de los trayectos de los miembros = 1315
         */
        assertEquals(7950.0,coto.huellaDeCarbonoTrayectosMensual("2","2022"));

    }

    @Test
    public void indicadorMensualDeUnSector() throws IOException {
        miembro1.agregarTrayectos(trayecto1); //trayecto con dos tramos: uno sinvehiculo, otro vehiculo particular
        miembro2.agregarTrayectos(trayecto2); // trayecto ccon dos tramos, un subte un vp
        when(this.vehiculoParticular.distancia(tramoB)).thenReturn(25.0);
        assertEquals(3975.0,coto.indicadorHCPorMiembrosMensual(sectorOrganizacion1,"2","2022"));

    }

    @Test
    public void impactoPersonalEnOrganizacionMensual() throws IOException {
        miembro1.agregarTrayectos(trayecto1); //trayecto con dos tramos: uno sinvehiculo, otro vehiculo particular
        miembro2.agregarTrayectos(trayecto2); // trayecto con dos tramos, un subte un vp
        when(this.vehiculoParticular.distancia(tramoB)).thenReturn(25.0);

        assertEquals(34.59119496855346,miembro1.impactoPersonalEnOrganizacionMensual(coto,"2","2020"));

    }

    @Test
    public void impactoPersonalEnOrganizacionAnual() throws IOException {
        miembro1.agregarTrayectos(trayecto1); //trayecto con dos tramos: uno sinvehiculo, otro vehiculo particular
        miembro2.agregarTrayectos(trayecto2); // trayecto ccon dos tramos, un subte un vp
        when(this.vehiculoParticular.distancia(tramoB)).thenReturn(25.0);

        assertEquals(34.59119496855346,miembro1.impactoPersonalEnOrganizacionAnual(coto,"2020"));

    }

}
