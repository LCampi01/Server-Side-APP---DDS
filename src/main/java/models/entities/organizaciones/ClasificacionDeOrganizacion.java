package models.entities.organizaciones;

import db.EntidadPersistente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "clasificacion_de_organizacion")
@Getter
@Setter
public class ClasificacionDeOrganizacion extends EntidadPersistente {
    @Column(name= "nombre")
    private String nombre;

    public ClasificacionDeOrganizacion(String nombre){
        this.nombre = nombre;
    }

    public ClasificacionDeOrganizacion() {

    }
}
