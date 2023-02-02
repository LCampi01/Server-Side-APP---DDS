package models.entities.notificaciones.whatsapp;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import models.entities.notificaciones.Notificacion;
import lombok.Setter;

public class AdapterWhatsappTwilio implements AdapterNotificadorWhatsApp {
    @Setter private String accountSID;
    @Setter private String authToken;
    @Setter private String fromNumber;
    private LectorPropertiesWhatsapp propertiesWhatsapp = new LectorPropertiesWhatsapp();

    public AdapterWhatsappTwilio() {
        this.accountSID = propertiesWhatsapp.getPropAccountSID();
        this.authToken = propertiesWhatsapp.getPropAuthToken();
        this.fromNumber = propertiesWhatsapp.getPropFromNumber();
    }

    @Override
    public void enviarWhatsappConGuia(Notificacion notificacion) throws ApiException {
        Twilio.init(accountSID, authToken);
        Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:" + notificacion.getNumeroTelefono()),
                new com.twilio.type.PhoneNumber("whatsapp:" + fromNumber),
                notificacion.getMensaje())
            .create();

        System.out.println("El WhatsApp se ha enviado correctamente");
    }
}
