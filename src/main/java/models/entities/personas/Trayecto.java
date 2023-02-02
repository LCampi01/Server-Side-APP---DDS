package models.entities.personas;

import db.EntidadPersistente;
import models.entities.organizaciones.Organizacion;
import lombok.Getter;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="trayecto")
public class Trayecto extends EntidadPersistente {

    @Column(name="periodicidad_mensual")
    private Integer periodicidadMensual;

    @Column(name="descripcion")
    private String descripcion;

    @Column(columnDefinition = "DATE")
    private LocalDate fechaInicioTrayecto;

    @ManyToOne
    @JoinColumn(name = "organizacion_id", referencedColumnName = "id")
    private Organizacion organizacionInvolucrada;

    @ManyToMany
    @JoinTable(name = "tramos_del_trayecto", joinColumns = @JoinColumn(name = "trayecto_id"), inverseJoinColumns = @JoinColumn(name = "tramo_id"))
    @Getter private List<Tramo> tramos;


    public Trayecto(Organizacion organizacionInvolucrada, String descripcion,Integer periodicidadMensual, LocalDate fechaInicioTrayecto) {
        this.organizacionInvolucrada = organizacionInvolucrada;
        this.tramos = new ArrayList<>();
        this.descripcion = descripcion;
        this.periodicidadMensual = periodicidadMensual;
        this.fechaInicioTrayecto = fechaInicioTrayecto;
    }

    public Trayecto() { }

    public void agregarTramo(Tramo tramo) {
        this.tramos.add(tramo);
        tramo.agregarTrayecto(this);
    }

    public void eliminarTramo(Tramo tramo) {
        this.tramos.remove(tramo);
        tramo.getTrayectos().remove(this);
    }

    public Double distancia() { //calcula la distancia de todos sus tramos y los suma
        return this.tramos.stream().mapToDouble(unTramo -> {
            return unTramo.distancia();
        }).sum();
    }

    /*
    public void agregarMiembro(MiembroDeOrganizacion miembroDeOrganizacion) {
        this.tramos.forEach(tramo -> tramo.agregarMiembro(miembroDeOrganizacion));
    }
    */

    public Double huellaDeCarbono() {
        return this.tramos.stream().mapToDouble(tramo -> {
            try {
                return tramo.huellaDeCarbono();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }).sum();
    }


    public Double huellaDeCarbonoOrganizacional(Organizacion organizacion) {
        if(this.organizacionInvolucrada.equals(organizacion)){
            return this.huellaDeCarbono();
        }else{
            return 0.0; //Este trayecto no es de la organizacion cuyo HC estamos calculando
        }
    }
    //fehca de inicio 02 2019
    //mes 1 2020
    public double huellaDeCarbonoMensual(String mes, String anio) {
        if (this.fechaInicioTrayecto.getYear() == Integer.parseInt(anio)){
            if (this.fechaInicioTrayecto.getMonth().getValue() <= Integer.parseInt(mes)){
                return this.huellaDeCarbono() * this.periodicidadMensual;
            }
        }
        if (this.fechaInicioTrayecto.getYear() < Integer.parseInt(anio)){
                return this.huellaDeCarbono() * this.periodicidadMensual;
        }
        return 0;
    }

    public double huellaDeCarbonoAnual(String anio) {
        if (this.fechaInicioTrayecto.getYear() <= Integer.parseInt(anio)){
            Double hc = 0.0;
            Integer mes = 1;
            while(mes<=12){
                hc = hc + this.huellaDeCarbonoMensual(mes.toString(), anio);
                mes++;
            }
            return hc;
        }
        return 0;
    }

    public double huellaDeCarbonoOrganizacionalMensual(Organizacion organizacion,String mes, String anio) {
        if(this.organizacionInvolucrada.equals(organizacion)){
            return this.huellaDeCarbonoMensual(mes,anio);
        }else{
            return 0.0; //Este trayecto no es de la organizacion cuyo HC estamos calculando
        }
    }

    public double huellaDeCarbonoOrganizacionalAnual(Organizacion organizacion, String anio) {
        if(this.organizacionInvolucrada.equals(organizacion)){
            return this.huellaDeCarbonoAnual(anio);
        }else{
            return 0.0; //Este trayecto no es de la organizacion cuyo HC estamos calculando
        }
    }
}
