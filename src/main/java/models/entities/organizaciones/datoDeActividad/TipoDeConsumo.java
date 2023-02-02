package models.entities.organizaciones.datoDeActividad;

import db.EntidadPersistente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="tipo_de_consumo")
@Getter
@Setter
public class TipoDeConsumo extends EntidadPersistente {

    @Column(name="descripcion")
    private String descripcion;

    @Column(name="unidad")
    private String unidad;

    @ManyToOne
    @JoinColumn(name = "actividad_id", referencedColumnName = "id")
    private Actividad actividad;


    public TipoDeConsumo(String descripcion, String unidad) {
        this.descripcion = descripcion;
        this.unidad = unidad;
    }

    public TipoDeConsumo() { }
}
