package controllers.transformadosParaVistas;

import lombok.Getter;
import models.entities.personas.MiembroDeOrganizacion;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SectorConMiembrosTransformadoParaVista {
    private int idSector;
    private String sector;
    private List<MiembroDeOrganizacion> miembros;

    public SectorConMiembrosTransformadoParaVista(int idSector, String sector) {
        this.idSector = idSector;
        this.sector = sector;
        this.miembros = new ArrayList<>();
    }

    public void agregarMiembros(List<MiembroDeOrganizacion> miembros) {
        this.miembros.addAll(miembros);
    }
}
