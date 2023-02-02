package entities.distanciasTransportes;

import models.entities.personas.Tramo;
import models.entities.transportes.IAdapterCalculadoraDistancia;
import models.entities.transportes.ServicioContratado;
import models.entities.ubicaciones.territorio.Departamento;
import models.entities.ubicaciones.ubicacion.Direccion;
import models.entities.ubicaciones.territorio.Provincia;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class DistanciaServicioContratadoTest {
    ServicioContratado servicioContratado= new ServicioContratado("uber");
    IAdapterCalculadoraDistancia adapterMock= mock(IAdapterCalculadoraDistancia.class);
    Provincia bsAs = new Provincia("Buenos Aires");
    Direccion direccionOrigen = new Direccion(new Departamento("San Isidro", bsAs),"santa fe", 100);
    Direccion direccionDestino = new Direccion(new Departamento("Vicente Lopez", bsAs),"maipu", 250);

    Tramo tramo = new Tramo(servicioContratado, direccionOrigen, direccionDestino);

    @BeforeEach
    public void init() {
        this.servicioContratado.setAdapter(this.adapterMock);
    }
    @Test
    public void distanciaServicioContratado() throws IOException {
        when(this.adapterMock.distancia(tramo)).thenReturn(100.0);

        Assertions.assertEquals(100.0, this.servicioContratado.distancia(tramo));
    }
}
