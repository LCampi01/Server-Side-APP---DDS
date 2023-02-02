package models.entities.transportes;

import javax.persistence.*;

@Entity
@Table(name = "vehiculo_particular", uniqueConstraints = {@UniqueConstraint(columnNames = {"tipo_vehiculo","tipo_combustible"})})
@DiscriminatorValue(value = "vehiculoParticular")
public class VehiculoParticular extends MedioDeTransporte {

    @Column(name="tipo_vehiculo")
    @Enumerated(EnumType.STRING)
    private TipoDeVehiculo tipoDeVehiculo;

    @Column(name="tipo_combustible")
    @Enumerated(EnumType.STRING)
    private TipoDeCombustible tipoDeCombustible;

    public VehiculoParticular(TipoDeVehiculo tipoDeVehiculo, TipoDeCombustible tipoDeCombustible) {
        this.tipoDeVehiculo = tipoDeVehiculo;
        this.tipoDeCombustible = tipoDeCombustible;
    }

    public VehiculoParticular() { }


    /*
    @Override
    public Double factorDeEmision() {
        Actividad act = new Actividad("Combustión móvil", Alcance.EMISIONES_DIRECTAS);
        TipoDeConsumo tipo = this.tipoDeConsumo(this.tipoDeCombustible);
        //Buscar el factor de emision que corresponde a ese tipo de combustible
        return BuscadorDeFactorDeEmision.instancia().obtenerFactor(act,tipo);
    }

    private TipoDeConsumo tipoDeConsumo(TipoDeCombustible tipoDeCombustible) {
        switch (tipoDeCombustible){
            case NAFTA:
                return new TipoDeConsumo("Combustible consumido - Nafta","lts");
        }
        //q pasa con electrico??
        return null;
    }*/
}
