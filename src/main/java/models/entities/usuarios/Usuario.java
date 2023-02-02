package models.entities.usuarios;

import lombok.Getter;
import lombok.Setter;
import db.EntidadPersistente;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
@Setter
@Getter
public class Usuario extends EntidadPersistente {
    @Column(name="nombre")
    private String nombre;

    @Column(name="apellido")
    private String apellido;

    @Column(name="email")
    private String email;

    @Column(name="contrasenia")
    private String contrasenia;

    @Enumerated(EnumType.STRING)
    private RolEnum rol;


    public Usuario(String nombre, String apellido, String email, String contrasenia, RolEnum rol){
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    public Usuario() {
    }
}
