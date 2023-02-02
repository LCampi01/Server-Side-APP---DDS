package models.entities.ubicaciones.territorio;

import lombok.Getter;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "departamento")
public class Departamento extends Territorio {

    @ManyToOne
    @JoinColumn(name = "provincia_id", referencedColumnName = "id")
    @Getter private Provincia provincia;

    public Departamento(String nombre, Provincia provincia) {
        super.setNombre(nombre);
        this.provincia = provincia;
    }

    public Departamento() {

    }
}
