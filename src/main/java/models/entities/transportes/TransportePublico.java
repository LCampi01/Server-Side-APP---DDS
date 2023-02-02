package models.entities.transportes;

import models.entities.personas.Tramo;
import models.entities.ubicaciones.ubicacion.Parada;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "transporte_publico", uniqueConstraints = {@UniqueConstraint(columnNames = {"tipo_transporte_publico","linea_utilizada"})})
@DiscriminatorValue(value = "transportePublico")
@Getter
public class TransportePublico extends MedioDeTransporte {

    @Column(name = "tipo_transporte_publico")
    @Enumerated(EnumType.STRING)
    private TipoTransportePublico tipo;

    @Column(name = "linea_utilizada")
    private String lineaUtilizada;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "transporte_publico_id")
    private List<Parada> paradas;

    public TransportePublico(TipoTransportePublico tipo, String lineaUtilizada) {
        this.tipo = tipo;
        this.lineaUtilizada = lineaUtilizada;
        this.paradas = new ArrayList<>();
    }

    public TransportePublico() { }

    public void agregarParadas(Parada... paradas) {
        Collections.addAll(this.paradas, paradas);
    }

    @Override
    public Double distancia(Tramo tramo) {
//        System.out.println(paradas.size());
        int indexPartida = paradas.indexOf(tramo.getPuntoDePartida());
        int indexLlegada = paradas.indexOf(tramo.getPuntoDeLlegada());
        List<Parada> recorrido;
        if(indexLlegada > indexPartida) { // la llegada está después en la lista de paradas
            recorrido = this.paradas.subList(indexPartida, indexLlegada);
        }else{ //la llegada está antes en la lista de paradas (va para la izquierda)
            recorrido = this.paradas.subList(indexLlegada, indexPartida);
        }

        Double distanciaTotal = recorrido.stream().mapToDouble(p -> p.getDistanciaASiguiente()).sum();

        return distanciaTotal;
    }

}
