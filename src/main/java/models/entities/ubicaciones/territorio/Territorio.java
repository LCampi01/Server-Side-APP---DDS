package models.entities.ubicaciones.territorio;

import db.EntidadPersistente;
import models.entities.sectores.Sector;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "territorio")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_territorio")
@Getter
@Setter
public abstract class Territorio extends EntidadPersistente {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sector_id", referencedColumnName = "id", unique = true)
    private Sector sector;

    @Column(name = "nombre")
    private String nombre;
}
