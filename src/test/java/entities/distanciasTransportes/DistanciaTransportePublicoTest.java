package entities.distanciasTransportes;

import models.entities.personas.Tramo;
import models.entities.transportes.TipoTransportePublico;
import models.entities.transportes.TransportePublico;
import models.entities.ubicaciones.ubicacion.Parada;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DistanciaTransportePublicoTest {
    static TransportePublico subteA;
    static Parada paradaA;
    static Parada paradaB;
    static Parada paradaC;
    static Parada paradaD;
    static Parada paradaE;

    @BeforeAll
    static void init() {
        subteA = new TransportePublico(TipoTransportePublico.SUBTE, "A");

        paradaA = new Parada("paradaA", 0.0, 7.0);
        paradaB = new Parada("paradaB", 7.0, 10.0);
        paradaC = new Parada("paradaC", 10.0, 15.0);
        paradaD = new Parada("paradaD", 15.0, 20.0);
        paradaE = new Parada("paradaE", 20.0, 0.0);

        subteA.agregarParadas(paradaA, paradaB, paradaC, paradaD, paradaE);
    }

    @Test
    public void distanciaTotalMedioDeTransporte() {
        Tramo tramo1 = new Tramo(subteA, paradaA, paradaE);
        Assertions.assertEquals(52, subteA.distancia(tramo1));
    }
}

