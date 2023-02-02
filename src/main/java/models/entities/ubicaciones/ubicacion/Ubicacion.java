package models.entities.ubicaciones.ubicacion;

import db.EntidadPersistente;
import models.entities.ubicaciones.territorio.Departamento;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name="ubicacion")
@DiscriminatorColumn(name = "tipo_ubicacion")
@Getter @Setter
public abstract class Ubicacion extends EntidadPersistente {

    @Column(name="calle")
    protected String calle;

    @Column(name="altura")
    protected Integer altura;

    @ManyToOne
    @JoinColumn(name = "departamento_id", referencedColumnName = "id")
    protected Departamento departamento;
}
