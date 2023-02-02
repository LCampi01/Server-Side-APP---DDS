package models.entities.organizaciones;

import models.entities.organizaciones.datoDeActividad.Actividad;
import models.entities.organizaciones.datoDeActividad.DatoDeActividad;
import models.entities.organizaciones.datoDeActividad.PeriodoDeImputacion;
import models.entities.organizaciones.datoDeActividad.TipoDeConsumo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ConversorMedicionADatoActividad {
    private static volatile ConversorMedicionADatoActividad instancia = null;
    private List<Actividad> actividadesExistentes = new ArrayList<>();

    private ConversorMedicionADatoActividad(){
        if (instancia != null) {
            throw new RuntimeException("Utilice el método instancia() para instaciar la clase");
        }
    }

    public static ConversorMedicionADatoActividad instancia() {
        if(instancia == null) {
            synchronized(ConversorMedicionADatoActividad.class){
                if(instancia == null) {
                    instancia = new ConversorMedicionADatoActividad();
                }
            }
        }
        return instancia;
    }

    public void agregarActividades(Actividad ... actividades){
        Collections.addAll(this.actividadesExistentes,actividades);
    }

    public DatoDeActividad convertirADatoActividad(String actividad, String tipoDeConsumo, String valor, String periodicidad, String periodo) throws IOException {
        Double valorDato = Double.parseDouble(valor);

        PeriodoDeImputacion periodoDeImputacion = new PeriodoDeImputacion(periodicidad,periodo);

        Optional<Actividad> actividadDato = actividadesExistentes.stream().filter(a ->  a.getDescripcion().equals(actividad)).findFirst();
        if ( !actividadDato.isPresent() ){
            throw new IOException("La actividad ingresada no es valida");
        }

        Optional<TipoDeConsumo> tipoDeConsumoDato= actividadDato.get().getTiposDeConsumo().stream().filter(t -> t.getDescripcion().equals(tipoDeConsumo)).findFirst();
        if ( !tipoDeConsumoDato.isPresent() ){
            throw new IOException("El tipo de consumo ingresado no existe o no corresponde a la actividad");
        }

        return new DatoDeActividad(actividadDato.get(),tipoDeConsumoDato.get(),valorDato,periodoDeImputacion);
    }
}
