package models.entities.ubicaciones.ubicacion;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@DiscriminatorValue("parada")
@Getter
@Setter
public class Parada extends Ubicacion {

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "distanciaAAnterior")
    private Double distanciaAAnterior; //no la usamos creo. la distancia la calculamos con la distanciaASiguiente

    @Column(name = "distanciaASiguiente")
    private Double distanciaASiguiente;

    public Parada(String descripcion, Double distanciaAAnterior, Double distanciaASiguiente) {
        this.descripcion = descripcion;
        this.distanciaAAnterior = distanciaAAnterior;
        this.distanciaASiguiente = distanciaASiguiente;
    }

    public Parada() { }
}
