package models.entities.notificaciones.whatsapp;

import com.twilio.exception.ApiException;
import models.entities.notificaciones.Notificacion;

public interface AdapterNotificadorWhatsApp {
    void enviarWhatsappConGuia(Notificacion notificacion) throws ApiException;
}
