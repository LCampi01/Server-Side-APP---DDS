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
import models.entities.ubicaciones.ubicacion.Parada;
import models.entities.ubicaciones.territorio.Provincia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class huellaDeCarbonoMiembro {
    IAdapterCalculadoraDistancia adapterMock;

    static Provincia bsAs = new Provincia("Buenos Aires");
    static Departamento quilmes = new Departamento("Quilmes",bsAs);
    static Departamento lanus = new Departamento("Lanus",bsAs);

    Sector sectorProvinciaBsAs = new Sector("provincia Bs As", bsAs);
    Sector sectorDepartamentoQuilmes = new Sector("departamento Quilmes de Bs As", quilmes);
    Sector sectorDepartamentoLanus = new Sector("departamento Lanus de Bs As", lanus);

    static ClasificacionDeOrganizacion supermercado = new ClasificacionDeOrganizacion("Supermercados");
    static Direccion direccionLanus = new Direccion(lanus,"Colon",65);

    Organizacion coto = new Organizacion("Coto SA", TipoDeOrganizacion.EMPRESA, supermercado, direccionLanus);

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

    Direccion direccionOrigenA = new Direccion(quilmes,"San Martin",45);
    Direccion direccionDestinoA = new Direccion(quilmes,"Colon",65);
    Direccion direccionOrigenB = new Direccion(lanus,"San Martin",45);
    Direccion direccionDestinoB = new Direccion(lanus,"Colon",500);

    Tramo tramoA = new Tramo(sinVehiculo, direccionOrigenA, direccionDestinoA);
    Tramo tramoB = new Tramo(vehiculoParticular, direccionOrigenB, direccionDestinoB);
    Tramo tramoC = new Tramo(subteA, paradaA,paradaC);

    LocalDate date = LocalDate.now();

    Trayecto trayecto1 = new Trayecto(coto,"Ir a coto",10,date.withYear(2019).withMonth(2));
    Trayecto trayecto2 = new Trayecto(coto,"Ir a coto",5,date.withYear(2019).withMonth(2));

    @BeforeEach
    public void init () {
        BuscadorDeSectores.instancia().agregarSectores(sectorProvinciaBsAs, sectorDepartamentoQuilmes, sectorDepartamentoLanus);

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
    }

    @Test
    public void calculoHuellaCarbonoMensualMiembro() throws IOException {
        miembro1.agregarTrayectos(trayecto1); //trayecto con dos tramos: uno sinvehiculo, otro vehiculo particular
        when(this.vehiculoParticular.distancia(tramoB)).thenReturn(25.0);
        /*
            El miembro 1 tiene solo al trayecto1, el cal tiene dos tramos
            La hc del primer tramo es 0 porque es sinvehiculo
            La hc del segundo tramo se calcula con la distancia (25) * el fe(22) dividido la cantidad de miembros
            La hc del segundo tramo da 275
            Como el trayecto ocurre 10 veces al mes, la periodicidad mensual es 275 * 10
         */
        assertEquals(2750.0,miembro1.huellaDeCarbonoMensual("2","2022").getValor());
    }

    @Test
    public void calculoHuellaCarbonoAnualMiembro() throws IOException {
        miembro1.agregarTrayectos(trayecto1); //trayecto con dos tramos: uno sinvehiculo, otro vehiculo particular

        when(this.vehiculoParticular.distancia(tramoB)).thenReturn(25.0);

        /*
            El miembro 1 tiene solo al trayecto1, el cal tiene dos tramos
            La hc del primer tramo es 0 porque es sinvehiculo
            La hc del segundo tramo se calcula con la distancia (25) * el fe(22) dividido la cantidad de miembros
            La hc del segundo tramo da 275
            Como el trayecto ocurre 10 veces al mes, la periodicidad anual es 275 * 10 * 12
         */

        assertEquals(33000.0,miembro1.huellaDeCarbonoAnual("2022").getValor());
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
}
