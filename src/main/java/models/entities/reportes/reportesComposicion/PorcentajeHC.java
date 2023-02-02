package models.entities.reportes.reportesComposicion;

import lombok.Getter;

@Getter
public class PorcentajeHC {
    private String nombre;
    private Double porcentaje;

    public PorcentajeHC(String nombre, Double porcentaje) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
    }
}
