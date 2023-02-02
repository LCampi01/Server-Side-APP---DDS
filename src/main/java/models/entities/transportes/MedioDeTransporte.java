//Cliente del patrón Adapter
package models.entities.transportes;

import models.entities.apiDistancia.CalculadoraDistanciaRetrofitAdapter;
import db.EntidadPersistente;
import models.entities.factorDeEmision.FactorDeEmision;
import models.entities.personas.Tramo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "medio_de_transporte")
@DiscriminatorColumn(name = "tipo_medio_de_transporte")
@Getter
@Setter
public abstract class MedioDeTransporte extends EntidadPersistente {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "factor_de_emision_id", referencedColumnName = "id")
    public FactorDeEmision factorDeEmision;

    @Transient
    private IAdapterCalculadoraDistancia adapter = CalculadoraDistanciaRetrofitAdapter.instancia(); //DEBERIA INSTANCIARSE DE UNA y ser un singleton

    //no hago constructor porque sino en el constructor de cada uno de los vehiculos aclarando el adapter
    //con este set hago que si querés lo cambias el adapter, pero sino usamos siempre esta api.
    public void setAdapter(IAdapterCalculadoraDistancia adapter) {
        this.adapter = adapter;
    }

    public Double distancia(Tramo tramo) throws IOException {
        return this.adapter.distancia(tramo);
    }

    public Double factorDeEmision() {
        return this.factorDeEmision.getValor();
    }
}
