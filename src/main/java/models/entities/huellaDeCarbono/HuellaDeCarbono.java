package models.entities.huellaDeCarbono;

import db.EntidadPersistente;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name="huella_de_carbono")
public class HuellaDeCarbono  extends EntidadPersistente {

    @Column(name="valor")
    @Getter private Double valor;

    @Column(name="unidad")
    @Enumerated
    @Getter private UnidadHuellaCarbono unidadHuellaCarbono; //tenemos que poder hacer cambio de unidades?

    public HuellaDeCarbono(Double valor, UnidadHuellaCarbono unidadHuellaCarbono) {
        this.valor = valor;
        this.unidadHuellaCarbono = unidadHuellaCarbono;
    }

    public HuellaDeCarbono() {

    }
}
