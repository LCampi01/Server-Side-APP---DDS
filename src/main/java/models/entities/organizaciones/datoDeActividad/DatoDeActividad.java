package models.entities.organizaciones.datoDeActividad;

import db.EntidadPersistente;
import models.entities.factorDeEmision.BuscadorDeFactorDeEmision;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="dato_de_actividad")
@Getter
@Setter
public class DatoDeActividad extends EntidadPersistente {

    @ManyToOne
    @JoinColumn(name = "actividad_id", referencedColumnName = "id")
    private Actividad actividad;

    @ManyToOne
    @JoinColumn(name = "tipo_de_consumo_id", referencedColumnName = "id")
    private TipoDeConsumo tipoDeConsumo;

    @Column(name = "valor")
    private double valor;

    @Embedded
    private PeriodoDeImputacion periodo;


    public DatoDeActividad(Actividad actividad, TipoDeConsumo tipoDeConsumo, double valor, PeriodoDeImputacion periodo) {
        this.actividad = actividad;
        this.tipoDeConsumo = tipoDeConsumo;
        this.valor = valor;
        this.periodo = periodo;
    }

    public DatoDeActividad() { }


    public Double huellaDeCarbono() {
        return this.valor * BuscadorDeFactorDeEmision.instancia().obtenerFactor(actividad, tipoDeConsumo);
    }

    public Double huellaDeCarbonoMensual(String mes, String anio) {
        if (this.periodo.esMensualYCoincide(mes, anio)){
            return this.huellaDeCarbono();
        }else{
            if (this.periodo.esAnualYCoincide(anio)){
                return this.huellaDeCarbono()/12.0;
            }
        }
        return 0.0;//si llegue aca, es porque es un dato de actividad de otro periodo y no lo cuento.
    }

    public Double huellaDeCarbonoAnual(String anio) {
        if (this.periodo.esDelAnio(anio)){
            //corresponde a ese año, no importa si sea mensual o anual lo trato igual
            return this.huellaDeCarbono();
        }
        return 0.0; //si llegue aca es porque es de otro anio y no lo cuento en el sum
    }
}
