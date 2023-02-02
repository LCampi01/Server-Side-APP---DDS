package models.entities.ubicaciones.territorio;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "provincia")
public class Provincia extends Territorio {
    public Provincia(String nombre) {
        super.setNombre(nombre);
    }

    public Provincia() {

    }
}
