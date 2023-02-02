package models.entities.reportes.reportesEvolucion;

import models.entities.huellaDeCarbono.UnidadHuellaCarbono;
import lombok.Getter;

@Getter
public class HCAnual {
    private Integer anio;
    private Double valorHC;
    private UnidadHuellaCarbono unidad;

    public HCAnual(Integer anio, Double valorHC, UnidadHuellaCarbono unidad) {
        this.anio = anio;
        this.valorHC = valorHC;
        this.unidad = unidad;
    }
}
