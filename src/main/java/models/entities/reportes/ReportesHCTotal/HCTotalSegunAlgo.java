package models.entities.reportes.ReportesHCTotal;

import models.entities.huellaDeCarbono.UnidadHuellaCarbono;
import lombok.Getter;

@Getter
public class HCTotalSegunAlgo {
    private String algo;
    private Double valorHC;
    private UnidadHuellaCarbono unidad;

    public HCTotalSegunAlgo(String algo, Double valorHC, UnidadHuellaCarbono unidad) {
        this.algo = algo;
        this.valorHC = valorHC;
        this.unidad = unidad;
    }
}
