package controllers.transformadosParaVistas;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TramoTransformadoParaVista {
    private int id;
    private String medioDeTransporte;
    private String puntoDePartida;
    private String puntoDeLlegada;

    public TramoTransformadoParaVista(int id, String medioDeTransporte, String puntoDePartida, String puntoDeLlegada) {
        this.id = id;
        this.medioDeTransporte = medioDeTransporte;
        this.puntoDePartida = puntoDePartida;
        this.puntoDeLlegada = puntoDeLlegada;
    }
}
