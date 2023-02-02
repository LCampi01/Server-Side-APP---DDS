package entities.notificaciones.transportePublico;

import models.entities.transportes.*;
import models.entities.ubicaciones.ubicacion.Parada;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MedioDeTransporteTest {
    VehiculoParticular vehiculoParticular = new VehiculoParticular(TipoDeVehiculo.CAMIONETA, TipoDeCombustible.GNC);
    SinVehiculo aPie = new SinVehiculo();
    ServicioContratado uber = new ServicioContratado("uber");
    TransportePublico subteC = new TransportePublico(TipoTransportePublico.SUBTE, "A");
    TransportePublico colectivo37 = new TransportePublico(TipoTransportePublico.COLECTIVO, "37");

    Parada independencia = new Parada("Independencia", 120.0, 0.0);
    Parada moreno = new Parada("Moreno",40.0,120.0);
    Parada avDeMayo = new Parada("Avenida de Mayo",100.0,40.0);
    Parada diagonalNorte = new Parada("Diagonal Norte",0.0,100.0);

    @Test
    public void agregoParadasASubteC() throws IOException {
        subteC.agregarParadas(diagonalNorte,avDeMayo,moreno,independencia);
        Assertions.assertEquals(4, subteC.getParadas().size());
    }
}
