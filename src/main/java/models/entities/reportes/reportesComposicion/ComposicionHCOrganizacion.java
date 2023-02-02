package models.entities.reportes.reportesComposicion;

import models.entities.organizaciones.Organizacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComposicionHCOrganizacion {

    public List<PorcentajeHC> reporteComposicionOrganizacionAnual(Organizacion organizacion, Integer anio) {
        List<PorcentajeHC> porcentajeHCPorOrganizacion = new ArrayList<>();

        Double totalHCAnual = organizacion.huellaDeCarbonoAnual(String.valueOf(anio)).getValor();
        Double porcentajeTrayectosAnual = organizacion.huellaDeCarbonoTrayectosAnual(String.valueOf(anio)) *100 / totalHCAnual;
        Double porcentajeActividadesAnual = organizacion.huellaDeCarbonoActividadesAnual(String.valueOf(anio)) *100 / totalHCAnual;

        PorcentajeHC porcentajeHCTrayectos = new PorcentajeHC("Trayectos", porcentajeTrayectosAnual);
        PorcentajeHC porcentajeHCActividades = new PorcentajeHC("Actividades", porcentajeActividadesAnual);
        Collections.addAll(porcentajeHCPorOrganizacion, porcentajeHCTrayectos, porcentajeHCActividades);

        return porcentajeHCPorOrganizacion;
    }

    public List<PorcentajeHC> reporteComposicionOrganizacionMensual(Organizacion organizacion,Integer mes, Integer anio) {
        List<PorcentajeHC> porcentajeHCPorOrganizacion = new ArrayList<>();

        Double totalHCMensual = organizacion.huellaDeCarbonoMensual(String.valueOf(mes), String.valueOf(anio)).getValor();
        Double porcentajeTrayectosMensual = organizacion.huellaDeCarbonoTrayectosMensual(String.valueOf(mes), String.valueOf(anio)) *100 / totalHCMensual;
        Double porcentajeActividadesMensual = organizacion.huellaDeCarbonoActividadesMensual(String.valueOf(mes), String.valueOf(anio)) *100 / totalHCMensual;

        PorcentajeHC porcentajeHCTrayectos = new PorcentajeHC("Trayectos", porcentajeTrayectosMensual);
        PorcentajeHC porcentajeHCActividades = new PorcentajeHC("Actividades", porcentajeActividadesMensual);
        Collections.addAll(porcentajeHCPorOrganizacion, porcentajeHCTrayectos, porcentajeHCActividades);

        return porcentajeHCPorOrganizacion;
    }
}
