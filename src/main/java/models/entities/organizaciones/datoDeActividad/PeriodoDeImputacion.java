package models.entities.organizaciones.datoDeActividad;

import lombok.Getter;

import javax.persistence.*;
import java.io.IOException;
import java.util.Locale;

@Embeddable
public class PeriodoDeImputacion {

    @Column(name = "periodicidad")
    @Getter private String periodicidad;

    @Column(name = "periodo")
    @Getter private String periodoDeImputacion;


    public PeriodoDeImputacion(String periodicidad, String periodoDeImputacion) throws IOException {
        this.periodoEsValido(periodicidad,periodoDeImputacion);
        this.periodicidad = periodicidad.toUpperCase(Locale.ROOT).trim();
        this.periodoDeImputacion = periodoDeImputacion;
    }

    public PeriodoDeImputacion() { }

    private void periodoEsValido(String periodicidad, String periodoDeImputacion) throws IOException {
        switch (periodicidad.toUpperCase(Locale.ROOT).trim()){
            case "ANUAL":
                if( !this.periodoAnualEsValido(periodoDeImputacion) ) {
                    throw new IOException("El periodo anual esta en un formato invalido");
                }
                break;
            case "MENSUAL":
                this.periodoMensualEsValido(periodoDeImputacion);
                if( !this.periodoMensualEsValido(periodoDeImputacion) ) {
                    throw new IOException("El periodo mensual esta en un formato invalido");
                }
                break;
            default:
                throw new IOException("La periodicidad ingresada no es valida");
        }
    }

    private boolean periodoMensualEsValido(String periodoDeImputacion) {
        return periodoDeImputacion.matches("(0[1-9]|1[0-2])/\\d{4}");
    }

    private boolean periodoAnualEsValido(String periodoDeImputacion) {
        return periodoDeImputacion.matches("\\d{4}");
    }

    public boolean esDelMes(String mes) {
        if ((periodicidad.equals("MENSUAL")) && (Integer.parseInt(periodoDeImputacion.substring(0,2)) == Integer.parseInt(mes))){
            return true;
        }
        return false;
    }

    public boolean esDelAnio(String anio) {
        if ((periodicidad.equals("MENSUAL")) && (Integer.parseInt(periodoDeImputacion.substring(3)) == Integer.parseInt(anio))){
            return true;
        }
        if ((periodicidad.equals("ANUAL")) && (Integer.parseInt(periodoDeImputacion) == Integer.parseInt(anio))){
            return true;
        }
        return false;
    }

    public boolean esMensualYCoincide(String mes, String anio) {
        return (this.periodicidad.equals("MENSUAL")) && (this.esDelAnio(anio)) && (this.esDelMes(mes));
    }


    public boolean esAnualYCoincide(String anio) {
        return (this.periodicidad.equals("ANUAL")) && (this.esDelAnio(anio));
    }
}
