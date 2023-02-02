package entities.huellaDeCarbono;

import models.entities.factorDeEmision.FactorDeEmision;
import models.entities.organizaciones.ClasificacionDeOrganizacion;
import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.TipoDeOrganizacion;
import models.entities.personas.MiembroDeOrganizacion;
import models.entities.personas.TipoDocumento;
import models.entities.personas.Tramo;
import models.entities.personas.Trayecto;
import models.entities.sectores.BuscadorDeSectores;
import models.entities.sectores.Sector;
import models.entities.transportes.*;
import models.entities.ubicaciones.territorio.Departamento;
import models.entities.ubicaciones.ubicacion.Direccion;
import models.entities.ubicaciones.territorio.Provincia;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class huellaDeCarbonoTransporteTest {
    IAdapterCalculadoraDistancia adapterMock = mock(IAdapterCalculadoraDistancia.class);

    static Provincia bsAs = new Provincia("Buenos Aires");
    static Departamento quilmes = new Departamento("Quilmes",bsAs);
    static Departamento lanus = new Departamento("Lanus",bsAs);

    Sector sector1 = new Sector("provincia Bs As", bsAs);
    Sector sector2 = new Sector("departamento Lanus de Bs As", lanus);
    Sector sector3 = new Sector("departamento Quilmes de Bs As", quilmes);

    MiembroDeOrganizacion miembro1 = new MiembroDeOrganizacion(TipoDocumento.DNI, "456565456");
    Organizacion coto = new Organizacion("Coto SA", TipoDeOrganizacion.EMPRESA,new ClasificacionDeOrganizacion("supermercado"), new Direccion(quilmes,"Colon",65));

    VehiculoParticular vehiculoParticular = new VehiculoParticular(TipoDeVehiculo.CAMIONETA, TipoDeCombustible.GNC);
    FactorDeEmision fe = new FactorDeEmision(22.0, "kgCO2eq/km");

    SinVehiculo sinVehiculo = new SinVehiculo();

    Tramo tramoA = new Tramo(sinVehiculo, new Direccion(quilmes,"San Martin",45),new Direccion(quilmes,"Colon",65));
    Tramo tramoB = new Tramo(vehiculoParticular, new Direccion(lanus,"San Martin",45),new Direccion(lanus,"Colon",500));

    LocalDate date = LocalDate.now();

    Trayecto trayecto = new Trayecto(coto,"Ir a coto",10,date.withYear(2019).withMonth(2));

    @BeforeEach
    public void init () {
        this.vehiculoParticular.setAdapter(this.adapterMock);
        this.sinVehiculo.setAdapter(this.adapterMock);

        BuscadorDeSectores.instancia().agregarSectores(sector1, sector2, sector3);
        //coto.cargarSectores();
    }

    @Test
    public void calculoHuellaMensualConVehiculoParticular() throws IOException {
        vehiculoParticular.setFactorDeEmision(fe);
        trayecto.agregarTramo(tramoA);
        trayecto.agregarTramo(tramoB);
        miembro1.agregarTrayectos(trayecto);

        when(this.vehiculoParticular.distancia(tramoA)).thenReturn(25.0);
        when(this.vehiculoParticular.distancia(tramoB)).thenReturn(64.0);

        Assertions.assertEquals(14080.0,this.miembro1.huellaDeCarbonoMensual("2","2022").getValor());
    }

}
