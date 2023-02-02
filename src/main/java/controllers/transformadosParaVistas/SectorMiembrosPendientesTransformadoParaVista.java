package controllers.transformadosParaVistas;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectorMiembrosPendientesTransformadoParaVista {
    private String sector;
    private String miembroPendiente;
    private int idSector;
    private int idMiembro;

    public SectorMiembrosPendientesTransformadoParaVista() { }
}
