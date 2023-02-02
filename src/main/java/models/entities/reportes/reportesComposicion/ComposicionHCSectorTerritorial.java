package models.entities.reportes.reportesComposicion;

import models.entities.huellaDeCarbono.HuellaDeCarbono;
import models.entities.organizaciones.Organizacion;
import models.entities.sectores.Sector;

import java.util.ArrayList;
import java.util.List;

public class ComposicionHCSectorTerritorial {

    public List<PorcentajeHC> reporteComposicionSectorTerritorialAnual(Sector sectorTerritorial, Integer anio) {
        List<PorcentajeHC> porcentajeHCPorOrganizaciones = new ArrayList<>();
        HuellaDeCarbono totalHCSectorAnual = sectorTerritorial.huellaDeCarbonoAnual(String.valueOf(anio));

        for (Organizacion org : sectorTerritorial.getOrganizaciones()) {
            Double porcentaje = org.huellaDeCarbonoAnual(String.valueOf(anio)).getValor() * 100 / totalHCSectorAnual.getValor();

            PorcentajeHC porcentajeHCDeEstaOrganizacion = new PorcentajeHC(org.getRazonSocial(), porcentaje);
            porcentajeHCPorOrganizaciones.add(porcentajeHCDeEstaOrganizacion);
        }

        return porcentajeHCPorOrganizaciones;
    }

    public List<PorcentajeHC> reporteComposicionSectorTerritorialMensual(Sector sectorTerritorial, Integer mes, Integer anio) {
        List<PorcentajeHC> porcentajeHCPorOrganizaciones = new ArrayList<>();
        HuellaDeCarbono totalHCSectorMensual = sectorTerritorial.huellaDeCarbonoMensual(String.valueOf(mes), String.valueOf(anio));

        for (Organizacion org : sectorTerritorial.getOrganizaciones()) {
            Double porcentaje = org.huellaDeCarbonoMensual(String.valueOf(mes), String.valueOf(anio)).getValor()
                    * 100 / totalHCSectorMensual.getValor();

            PorcentajeHC porcentajeHCDeEstaOrganizacion = new PorcentajeHC(org.getRazonSocial(), porcentaje);
            porcentajeHCPorOrganizaciones.add(porcentajeHCDeEstaOrganizacion);
        }

        return porcentajeHCPorOrganizaciones;
    }
}
