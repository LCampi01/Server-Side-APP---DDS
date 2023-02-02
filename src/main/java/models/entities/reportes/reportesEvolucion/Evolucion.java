package models.entities.reportes.reportesEvolucion;

import models.entities.huellaDeCarbono.HuellaDeCarbono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Evolucion {
    //hice clase abstracta así no se repetía el codigo para Organizacion y Sector territorial
    public abstract HuellaDeCarbono huellaDeCarbono(Integer anio);

    public List<HCAnual> reporteEvolucion(Integer anioInicioReporte) {
        List<HCAnual> HCPorAnio = new ArrayList<>();
        LocalDate hoy = LocalDate.now();

        while (anioInicioReporte <= hoy.getYear()) {
            HuellaDeCarbono hc = this.huellaDeCarbono(anioInicioReporte);

            HCAnual HCDeEsteAnio = new HCAnual(anioInicioReporte, hc.getValor(), hc.getUnidadHuellaCarbono());
            HCPorAnio.add(HCDeEsteAnio);

            anioInicioReporte += 1;
        }

        return HCPorAnio;
    }

}
