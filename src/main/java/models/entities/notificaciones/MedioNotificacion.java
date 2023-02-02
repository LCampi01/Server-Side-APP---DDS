package models.entities.notificaciones;

import javax.mail.MessagingException;

public interface MedioNotificacion {
    void enviarGuia(Notificacion notificacion) throws MessagingException;
}
