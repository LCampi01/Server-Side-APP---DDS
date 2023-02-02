package models.entities.factorDeEmision;

import db.EntidadPersistente;
import models.entities.organizaciones.datoDeActividad.Actividad;
import models.entities.organizaciones.datoDeActividad.TipoDeConsumo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="factor_de_emision")
@Getter
public class FactorDeEmision extends EntidadPersistente {
    @Column(name="valor")
    private Double valor;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tipo_de_consumo_id", referencedColumnName = "id")
    @Setter private TipoDeConsumo tipoDeConsumo;

    @Column(name="unidad")
    private String unidadFactorEmision;

    public FactorDeEmision() { }

    public FactorDeEmision(Double valor, String unidadFactorEmision) {
        this.valor = valor;
        this.unidadFactorEmision = unidadFactorEmision;
    }

    public FactorDeEmision(Double valor,TipoDeConsumo tipoDeConsumo) {
        this.valor = valor;
        this.tipoDeConsumo = tipoDeConsumo;
        this.unidadFactorEmision = tipoDeConsumo.getUnidad();
    }

    public Boolean esDe(Actividad unaActividad, TipoDeConsumo unTipoDeConsumo){
        return (unaActividad.getDescripcion().equals(tipoDeConsumo.getActividad().getDescripcion())
                && unTipoDeConsumo.getDescripcion().equals(tipoDeConsumo.getDescripcion()));
    }

    public void setValor(){
        //Aca tendriamos que poner los valores de acuerdo al tipo de consumo
    }
}