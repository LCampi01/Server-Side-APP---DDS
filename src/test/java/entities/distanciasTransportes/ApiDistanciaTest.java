package entities.distanciasTransportes;

import models.entities.personas.Tramo;
import models.entities.transportes.MedioDeTransporte;
import models.entities.transportes.SinVehiculo;
import models.entities.ubicaciones.territorio.Departamento;
import models.entities.ubicaciones.ubicacion.Direccion;
import models.entities.ubicaciones.territorio.Provincia;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ApiDistanciaTest {

    @Test
    public void calcularDistancia() throws IOException {
        Provincia bsAs = new Provincia("Buenos Aires");
        Direccion direccionOrigen = new Direccion(new Departamento("San Isidro", bsAs),"santa fe", 100);
        Direccion direccionDestino = new Direccion(new Departamento("Vicente Lopez", bsAs),"maipu", 250);

        MedioDeTransporte medio = new SinVehiculo();
        Tramo tramo = new Tramo(medio, direccionOrigen, direccionDestino);
        Double distancia = tramo.distancia();

        Assertions.assertTrue(distancia > 0);
    }
}
