package controllers.transformadosParaVistas;

import lombok.Getter;

@Getter
public class SectorOrganizacionTransformadoParaVista {
    private String sectorOrganizacion;
    private String organizacion;
    private String estado;

    public SectorOrganizacionTransformadoParaVista(String sectorOrganizacion, String organizacion, String estado) {
        this.sectorOrganizacion = sectorOrganizacion;
        this.organizacion = organizacion;
        this.estado = estado;
    }
}
