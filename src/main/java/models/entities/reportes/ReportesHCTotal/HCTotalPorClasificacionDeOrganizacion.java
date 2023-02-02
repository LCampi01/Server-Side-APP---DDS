package models.entities.reportes.ReportesHCTotal;

import models.entities.huellaDeCarbono.UnidadHuellaCarbono;
import models.entities.organizaciones.ClasificacionDeOrganizacion;
import models.entities.organizaciones.Organizacion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HCTotalPorClasificacionDeOrganizacion {

    public List<HCTotalSegunAlgo> reporteHCTotalAnual(List<ClasificacionDeOrganizacion> clasificaciones,
                                                      List<Organizacion> organizaciones, Integer anio) {

        List<HCTotalSegunAlgo> hcPorClasificaciones = new ArrayList<>();
        for (ClasificacionDeOrganizacion c : clasificaciones) {
            List<Organizacion> organizacionesDeClasificacion
                    = organizaciones.stream()
                    .filter(org -> org.getClasificacionDeOrganizacion().getId() == c.getId())
                    .collect(Collectors.toList());

            Double hcTotal = organizacionesDeClasificacion.stream()
                    .mapToDouble(orga -> orga.huellaDeCarbonoAnual(String.valueOf(anio)).getValor())
                    .sum();

            UnidadHuellaCarbono hcUnidad = null; //si está en null imprime raro. sino yafue le podemos harcodear la unidad.
            if(!organizacionesDeClasificacion.isEmpty()){
                hcUnidad = organizacionesDeClasificacion.get(0).huellaDeCarbonoAnual(String.valueOf(anio)).getUnidadHuellaCarbono();
            }

            HCTotalSegunAlgo hcDeEstaClasificacion = new HCTotalSegunAlgo(c.getNombre(), hcTotal, hcUnidad);
            hcPorClasificaciones.add(hcDeEstaClasificacion);
        }

        return hcPorClasificaciones;
    }

    public List<HCTotalSegunAlgo> reporteHCTotalMensual(List<ClasificacionDeOrganizacion> clasificaciones,
                                                        List<Organizacion> organizaciones, Integer mes, Integer anio) {

        List<HCTotalSegunAlgo> hcPorClasificaciones = new ArrayList<>();
        for (ClasificacionDeOrganizacion c : clasificaciones) {
            List<Organizacion> organizacionesDeClasificacion
                    = organizaciones.stream()
                    .filter(org -> org.getClasificacionDeOrganizacion().getId() == c.getId())
                    .collect(Collectors.toList());

            Double hcTotal = organizacionesDeClasificacion.stream()
                    .mapToDouble(orga -> orga.huellaDeCarbonoMensual(String.valueOf(mes), String.valueOf(anio)).getValor())
                    .sum();

            UnidadHuellaCarbono hcUnidad = null; //si está en null imprime raro. sino ya fue le podemos hardcodear la unidad.
            if(!organizacionesDeClasificacion.isEmpty()){
                hcUnidad = organizacionesDeClasificacion.get(0).huellaDeCarbonoMensual(String.valueOf(mes), String.valueOf(anio)).getUnidadHuellaCarbono();
            }

            HCTotalSegunAlgo hcDeEstaClasificacion = new HCTotalSegunAlgo(c.getNombre(), hcTotal, hcUnidad);
            hcPorClasificaciones.add(hcDeEstaClasificacion);
        }

        return hcPorClasificaciones;
    }
}
