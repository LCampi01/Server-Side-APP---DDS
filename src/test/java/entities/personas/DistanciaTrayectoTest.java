package entities.personas;

import models.entities.organizaciones.ClasificacionDeOrganizacion;
import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.TipoDeOrganizacion;
import models.entities.personas.Tramo;
import models.entities.personas.Trayecto;
import models.entities.sectores.BuscadorDeSectores;
import models.entities.sectores.Sector;
import models.entities.transportes.*;
import models.entities.ubicaciones.territorio.Departamento;
import models.entities.ubicaciones.ubicacion.Direccion;
import models.entities.ubicaciones.ubicacion.Parada;
import models.entities.ubicaciones.territorio.Provincia;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DistanciaTrayectoTest {

    Provincia bsAs = new Provincia("Buenos Aires");
    Departamento quilmes = new Departamento("Quilmes",bsAs);

    Sector sectorProvinciaBsAs = new Sector("provincia Bs As", bsAs);
    Sector sectorDepartamentoQuilmes = new Sector("departamento Quilmes de Bs As", quilmes);

    IAdapterCalculadoraDistancia adapterMock;
    TransportePublico subteA = new TransportePublico(TipoTransportePublico.SUBTE, "A");
    SinVehiculo bicicleta = new SinVehiculo();
    ServicioContratado uber = new ServicioContratado("uber");
    VehiculoParticular camioneta = new VehiculoParticular(TipoDeVehiculo.CAMIONETA, TipoDeCombustible.GNC);

    Parada paradaA = new Parada("paradaA", 0.0 , 7.0);
    Parada paradaB = new Parada("paradaB", 7.0 , 10.0);
    Parada paradaC = new Parada("paradaC", 10.0 , 0.0);

    Direccion direccionOrigenBicicleta = new Direccion(quilmes,"San Martin",45);
    Direccion direccionDestinoBicicleta = new Direccion(quilmes,"Colon",65);
    Direccion direccionDestinoUber = new Direccion(quilmes,"Corrientes",1234);
    Direccion direccionDestinoCamioneta = new Direccion(quilmes,"San Martin",456);

    Organizacion coto = new Organizacion
            ("Coto SA", TipoDeOrganizacion.EMPRESA,new ClasificacionDeOrganizacion("supermercado"),
                    direccionDestinoCamioneta);

    Tramo tramo1, tramo2, tramo3, tramo4;
    private Trayecto trayecto1;

    @BeforeEach
    public void init () {
        subteA.agregarParadas(paradaA, paradaB, paradaC);
        tramo1 = new Tramo(subteA, paradaA, paradaC);//distancia = 17
        tramo2 = new Tramo(bicicleta, direccionOrigenBicicleta, direccionDestinoBicicleta);
        tramo3 = new Tramo(uber, direccionDestinoBicicleta, direccionDestinoUber);
        tramo4 = new Tramo(camioneta, direccionDestinoUber, direccionDestinoCamioneta);
        this.adapterMock = mock(IAdapterCalculadoraDistancia.class);
        this.bicicleta.setAdapter(this.adapterMock);
        this.uber.setAdapter(this.adapterMock);
        this.camioneta.setAdapter(this.adapterMock);

        BuscadorDeSectores.instancia().agregarSectores(sectorProvinciaBsAs, sectorDepartamentoQuilmes);
    }

    @Test
    public void distanciaTrayectoTotal() throws IOException {
        LocalDate date = LocalDate.now();

        trayecto1 = new Trayecto(coto,"Ir a coto",10,date.withYear(2019).withMonth(2));
        Double distancia1 = 5.0;
        Double distancia2 = 10.0;
        Double distancia3 = 15.0;

        trayecto1.agregarTramo(tramo1);
        trayecto1.agregarTramo(tramo2);
        trayecto1.agregarTramo(tramo3);
        trayecto1.agregarTramo(tramo4);

        when(this.bicicleta.distancia(tramo2)).thenReturn(distancia1);
        when(this.uber.distancia(tramo3)).thenReturn(distancia2);
        when(this.camioneta.distancia(tramo4)).thenReturn(distancia3);
        Assertions.assertEquals(47.0,trayecto1.distancia());
    }


}
