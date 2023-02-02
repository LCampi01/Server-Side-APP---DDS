package entities.huellaDeCarbono;

import models.entities.factorDeEmision.FactorDeEmision;
import models.entities.organizaciones.CargadorDeMediciones;
import models.entities.organizaciones.ClasificacionDeOrganizacion;
import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.TipoDeOrganizacion;
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

public class huellaDeCarbonoTrayectos {
    IAdapterCalculadoraDistancia adapterMock;

    static Provincia bsAs = new Provincia("Buenos Aires");
    static Departamento quilmes = new Departamento("Quilmes",bsAs);
    static Departamento lanus = new Departamento("Lanus",bsAs);

    Sector sectorProvinciaBsAs = new Sector("provincia Bs As", bsAs);
    Sector sectorDepartamentoQuilmes = new Sector("departamento Quilmes de Bs As", quilmes);
    Sector sectorDepartamentoLanus = new Sector("departamento Lanus de Bs As", lanus);

    Organizacion coto = new Organizacion("Coto SA", TipoDeOrganizacion.EMPRESA,new ClasificacionDeOrganizacion("supermercado"), new Direccion(quilmes,"Colon",65));

    MiembroDeOrganizacion miembro1 = new MiembroDeOrganizacion(TipoDocumento.DNI, "4565654");

    MiembroDeOrganizacion miembro2 = new MiembroDeOrganizacion(TipoDocumento.DNI, "2525526");

    VehiculoParticular vehiculoParticular = new VehiculoParticular(TipoDeVehiculo.CAMIONETA, TipoDeCombustible.GNC);
    SinVehiculo sinVehiculo = new SinVehiculo();


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

    ClasificacionDeOrganizacion supermercado = new ClasificacionDeOrganizacion("Supermercados");
    Direccion ubicacionGenerica = new Direccion(quilmes,"San Martin",455);

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
    public void init () {
        this.adapterMock = mock(IAdapterCalculadoraDistancia.class);
        this.vehiculoParticular.setAdapter(this.adapterMock);
        this.sinVehiculo.setAdapter(this.adapterMock);

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

        BuscadorDeSectores.instancia().agregarSectores(sectorProvinciaBsAs, sectorDepartamentoQuilmes, sectorDepartamentoLanus);
    }
    @Test
    public void huellaDeCarbonoTrayectos() throws IOException {
        miembro2.agregarTrayectos(trayecto2);
        assertEquals(765.0,trayecto2.huellaDeCarbono());
   }
}
