package models.entities.notificaciones;

import models.entities.organizaciones.Contacto;
import models.entities.organizaciones.Organizacion;
import lombok.Setter;

import javax.mail.MessagingException;
import java.util.*;
import java.util.stream.Collectors;

public class Notificador { //podria ser Singleton
    private List<MedioNotificacion> mediosDeNotificacion;
    @Setter private String linkGuiaRecomendaciones;

    public Notificador(String linkGuiaRecomendaciones) {
        this.mediosDeNotificacion = new ArrayList<>();
        this.linkGuiaRecomendaciones = linkGuiaRecomendaciones;
    }

    public void agregarMediosDeNotificacion(MedioNotificacion ... medios) {
        Collections.addAll(this.mediosDeNotificacion, medios);
    }


    public void enviarGuia(List<Organizacion> organizaciones) throws MessagingException {
        List<Notificacion> notificaciones = obtenerNotificaciones(organizaciones);
        for (Notificacion n : notificaciones) {
            for (MedioNotificacion m : this.mediosDeNotificacion) {
                m.enviarGuia(n);
            }
        }
    }

    private List<Contacto> obtenerContactosDeOrganizaciones(List<Organizacion> organizaciones) {
        return organizaciones.stream().map(o -> o.getContactos()).flatMap(Collection::stream).collect(Collectors.toList());
    }

    private List<Notificacion> obtenerNotificaciones(List<Organizacion> organizaciones) {
        List<Contacto> contactos = this.obtenerContactosDeOrganizaciones(organizaciones);

        List<Notificacion> notificaciones = new ArrayList<>();

        for(Contacto c : contactos) {
            Notificacion notif = new Notificacion(
                    c.getEmail(),
                    c.getNumeroTelefono(),
                    "Hola " + c.getNombre()
                            + "\nEn el siguiente link podra encontrar el link a la guia de recomendaciones: "
                            + this.linkGuiaRecomendaciones,
                    "Guia de recomendaciones para reducir la HC"
            );
            notificaciones.add(notif);
        }

        return notificaciones;
    }
}
