package entities.distanciasTransportes;

import models.entities.personas.Tramo;
import models.entities.transportes.IAdapterCalculadoraDistancia;
import models.entities.transportes.SinVehiculo;
import models.entities.ubicaciones.territorio.Departamento;
import models.entities.ubicaciones.ubicacion.Direccion;
import models.entities.ubicaciones.territorio.Provincia;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class DistanciaSinVehiculoTest {
    SinVehiculo sinVehiculo = new SinVehiculo();
    IAdapterCalculadoraDistancia adapterMock = mock(IAdapterCalculadoraDistancia.class);
    Provincia bsAs = new Provincia("Buenos Aires");
    Direccion direccionOrigen = new Direccion(new Departamento("San Isidro", bsAs),"santa fe", 100);
    Direccion direccionDestino = new Direccion(new Departamento("Vicente Lopez", bsAs),"maipu", 250);

    //falla aca porque no puede
    Tramo tramo = new Tramo(sinVehiculo, direccionOrigen, direccionDestino);

    @BeforeEach
    public void init() {
        this.sinVehiculo.setAdapter(this.adapterMock);
    }

    @Test
    public void distanciaSinVehiculo() throws IOException {
        when(this.adapterMock.distancia(tramo)).thenReturn(100.0);

        Assertions.assertEquals(100.0, this.sinVehiculo.distancia(tramo));
    }
}
