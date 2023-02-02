package models.entities.transportes;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "servicio_contratado")
@DiscriminatorValue(value = "servicioContratado")
public class ServicioContratado extends MedioDeTransporte {

    @Column(name="medioInvolucrado")
    @Getter
    private String medioInvolucrado;

    public ServicioContratado(String medioInvolucrado) {
        this.medioInvolucrado = medioInvolucrado;
    }

    public ServicioContratado() { }
}
