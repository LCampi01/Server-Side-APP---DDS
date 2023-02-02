package models.entities.notificaciones;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Notificacion {
    private String email;
    private String numeroTelefono;
    private String mensaje;
    private String asunto;

    public Notificacion(String email, String numeroWhatsapp, String mensaje, String asunto) {
        this.email = email;
        this.numeroTelefono = numeroWhatsapp;
        this.mensaje = mensaje;
        this.asunto = asunto;
    }
}
