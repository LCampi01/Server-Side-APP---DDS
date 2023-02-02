package models.entities.factorDeEmision;

import db.EntityManagerHelper;
import models.entities.organizaciones.datoDeActividad.Actividad;
import models.entities.organizaciones.datoDeActividad.TipoDeConsumo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuscadorDeFactorDeEmision {
    private static BuscadorDeFactorDeEmision instancia = null;
    @Getter public List<FactorDeEmision> factores = new ArrayList<>();

    public static BuscadorDeFactorDeEmision instancia() {
        if(instancia == null) {
            instancia = new BuscadorDeFactorDeEmision();
        }
        return instancia;
    }

    public void agregarFactores(FactorDeEmision ... factor){
        Collections.addAll(this.factores,factor);
    }

    public Double obtenerFactor(Actividad actividad, TipoDeConsumo tipoDeConsumo){
        //return factores.stream().filter(factorDeEmision -> factorDeEmision.esDe(actividad,tipoDeConsumo)).findFirst().get().getValor();
        List<FactorDeEmision> factores = (List<FactorDeEmision>) EntityManagerHelper.createQuery("from FactorDeEmision where tipo_de_consumo_id is not null").getResultList();
        return factores.stream().filter(fe -> fe.esDe(actividad,tipoDeConsumo)).findFirst().get().getValor();
    }
}
