package models.entities.notificaciones.whatsapp;

import models.entities.notificaciones.MedioNotificacion;
import models.entities.notificaciones.Notificacion;


public class NotificadorWhatsApp implements MedioNotificacion {
    private AdapterNotificadorWhatsApp adapter;

    public NotificadorWhatsApp(AdapterNotificadorWhatsApp adapter) {
        this.adapter = adapter;
    }

    public void setAdapter(AdapterNotificadorWhatsApp adapter) {
        this.adapter = adapter;
    }

    @Override
    public void enviarGuia(Notificacion notificacion) {
        this.adapter.enviarWhatsappConGuia(notificacion);
    }
}
