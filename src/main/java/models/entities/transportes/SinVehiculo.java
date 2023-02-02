package models.entities.transportes;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sin_vehiculo")
@DiscriminatorValue(value = "sinVehiculo")
public class SinVehiculo extends MedioDeTransporte {

    public SinVehiculo() {
        //super();
    }

    @Override
    public Double factorDeEmision() {
        return 0.0; //No deberia tener HC
    }

}
