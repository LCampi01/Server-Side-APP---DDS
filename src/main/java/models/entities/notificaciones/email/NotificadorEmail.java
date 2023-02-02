package models.entities.notificaciones.email;

import models.entities.notificaciones.MedioNotificacion;
import models.entities.notificaciones.Notificacion;

import javax.mail.MessagingException;


public class NotificadorEmail implements MedioNotificacion {
    private AdapterNotificadorEmail adapter;

    public NotificadorEmail(AdapterNotificadorEmail adapter) {
        this.adapter = adapter;
    }

    public void setAdapter(AdapterNotificadorEmail adapter) {
        this.adapter = adapter;
    }

    @Override
    public void enviarGuia(Notificacion notificacion) throws MessagingException {
        this.adapter.enviarMailConGuia(notificacion);
    }
}
