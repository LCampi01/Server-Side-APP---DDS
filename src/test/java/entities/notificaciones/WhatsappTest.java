package entities.notificaciones;

import com.twilio.exception.ApiException;
import models.entities.notificaciones.Notificacion;
import models.entities.notificaciones.whatsapp.AdapterWhatsappTwilio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WhatsappTest {

    Notificacion notificacion = new Notificacion(
            "santiagot.orlando@gmail.com", "+5491126777697",
            "Eze y Lalo los mejores <3", "Guia de recomendaciones para reducir la HC"
    );

    @Test
    public void enviarWhatsappTwilioTest() {
        AdapterWhatsappTwilio adapter = new AdapterWhatsappTwilio();

        Assertions.assertDoesNotThrow(
                () -> { adapter.enviarWhatsappConGuia(notificacion); }
        );
    }

    @Test
    public void falloEnvioWhatsappTwilioTest() {
        AdapterWhatsappTwilio adapter = new AdapterWhatsappTwilio();
        adapter.setAccountSID("cualquiera");

        Assertions.assertThrows(
                ApiException.class,
                () -> { adapter.enviarWhatsappConGuia(notificacion); }
        );
    }

}
