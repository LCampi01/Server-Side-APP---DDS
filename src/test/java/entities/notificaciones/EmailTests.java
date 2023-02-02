package entities.notificaciones;

import models.entities.notificaciones.Notificacion;
import models.entities.notificaciones.email.AdapterMailJava;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;

public class EmailTests {

    Notificacion notificacion = new Notificacion(
            "glemme@frba.utn.edu.ar", "1122334455",
            "Mensaje de prueba", "Guia de recomendaciones para reducir la HC"
    );

    @Test
    public void enviarMailJavaTest() {
        AdapterMailJava adapter = new AdapterMailJava();

        Assertions.assertDoesNotThrow(
                () -> { adapter.enviarMailConGuia(notificacion); }
        );
    }

    @Test
    public void falloEnvioMailJavaTest() {
        AdapterMailJava adapter = new AdapterMailJava();
        adapter.setAppPassword("contraseniaIncorrecta");

        Assertions.assertThrows(
                MessagingException.class,
                () -> { adapter.enviarMailConGuia(notificacion); }
        );
    }

}
