package models.entities.reportes.reportesComposicion;

import models.entities.sectores.Sector;

import java.util.ArrayList;
import java.util.List;

public class ComposicionHCPorPais {

    public List<PorcentajeHC> reporteComposicionPaisAnual(List<Sector> sectores, Integer anio) {
        List<PorcentajeHC> porcentajeHCPorProvincias = new ArrayList<>();

        Double totalHCAnualProvincia =
                sectores.stream().mapToDouble(s -> s.huellaDeCarbonoAnual(String.valueOf(anio)).getValor()).sum();
        for (Sector sector : sectores) {
            Double porcentaje = sector.huellaDeCarbonoAnual(String.valueOf(anio)).getValor() * 100 / totalHCAnualProvincia;

            PorcentajeHC porcentajeHCDeEstaOrganizacion = new PorcentajeHC(sector.getNombre(), porcentaje);
            porcentajeHCPorProvincias.add(porcentajeHCDeEstaOrganizacion);
        }

        return porcentajeHCPorProvincias;
    }

    public List<PorcentajeHC> reporteComposicionPaisMensual(List<Sector> sectores, Integer mes, Integer anio) {
        List<PorcentajeHC> porcentajeHCPorProvincias = new ArrayList<>();

        Double totalHCMensualProvincia =
                sectores.stream()
                        .mapToDouble(s -> s.huellaDeCarbonoMensual(String.valueOf(mes), String.valueOf(anio)).getValor())
                        .sum();
        for (Sector sector : sectores) {
            Double porcentaje = sector.huellaDeCarbonoMensual(String.valueOf(mes), String.valueOf(anio)).getValor()
                    * 100 / totalHCMensualProvincia;

            PorcentajeHC porcentajeHCDeEstaOrganizacion = new PorcentajeHC(sector.getNombre(), porcentaje);
            porcentajeHCPorProvincias.add(porcentajeHCDeEstaOrganizacion);
        }

        return porcentajeHCPorProvincias;
    }
}
