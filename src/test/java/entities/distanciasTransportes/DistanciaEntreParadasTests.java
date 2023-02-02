package entities.distanciasTransportes;

import models.entities.personas.Tramo;
import models.entities.transportes.TipoTransportePublico;
import models.entities.transportes.TransportePublico;
import models.entities.ubicaciones.ubicacion.Parada;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class DistanciaEntreParadasTests {

    static TransportePublico subteA;
    static Parada paradaA;
    static Parada paradaB;
    static Parada paradaC;
    static Parada paradaD;
    static Parada paradaE;

    @BeforeAll
    static void init() {
        subteA = new TransportePublico(TipoTransportePublico.SUBTE, "A");

        paradaA = new Parada("paradaA", 0.0 , 7.0);
        paradaB = new Parada("paradaB", 7.0 , 10.0);
        paradaC = new Parada("paradaC", 10.0 , 15.0);
        paradaD = new Parada("paradaD", 15.0 , 20.0);
        paradaE = new Parada("paradaE", 20.0 , 0.0);

        subteA.agregarParadas(paradaA, paradaB, paradaC, paradaD, paradaE);
    }

    @Test
    public void distanciaEntreParadasParaAdelante() throws IOException {
        Tramo tramo = new Tramo(subteA, paradaB, paradaD);

        Double distancia = tramo.distancia();
        Assertions.assertEquals(25.0, distancia);
    }

    @Test
    public void distanciaEntreParadasParaAtras() throws IOException {
        Tramo tramo = new Tramo(subteA, paradaD, paradaB);

        Double distancia = tramo.distancia();
        Assertions.assertEquals(25.0, distancia);
    }

    @Test
    public void distanciaEntrePrimerParadaYUltima() throws IOException {
        Tramo tramo = new Tramo(subteA, paradaA, paradaE);
        Double distancia = tramo.distancia();
        Assertions.assertEquals(52.0, distancia);
    }

    @Test
    public void distanciaEntreUltimaParadaYPrimera() throws IOException {
        Tramo tramo = new Tramo(subteA, paradaE, paradaA);

        Double distancia = tramo.distancia();
        Assertions.assertEquals(52.0, distancia);
    }
}
