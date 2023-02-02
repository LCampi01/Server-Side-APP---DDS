package models.entities.notificaciones.email;

import models.entities.notificaciones.Notificacion;

import javax.mail.MessagingException;

public interface AdapterNotificadorEmail {
    void enviarMailConGuia(Notificacion notificacion) throws MessagingException;
}
