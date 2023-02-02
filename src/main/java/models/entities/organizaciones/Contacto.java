package models.entities.organizaciones;

import db.EntidadPersistente;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name="contacto")
@Getter
public class Contacto extends EntidadPersistente {

    @Column(name="nombre")
    private String nombre;

    @Column(name="email")
    private String email;

    @Column(name="numero_telefono")
    private String numeroTelefono;


    public Contacto(String nombre, String email, String numeroWhatsapp) {
        this.nombre = nombre;
        this.email = email;
        this.numeroTelefono = numeroWhatsapp;
    }

}