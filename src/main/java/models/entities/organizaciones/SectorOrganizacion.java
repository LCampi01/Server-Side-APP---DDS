package models.entities.organizaciones;

import db.EntidadPersistente;
import models.entities.personas.MiembroDeOrganizacion;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "sector_organizacion")
@Getter
@Setter
public class SectorOrganizacion extends EntidadPersistente {
    @Column(name = "nombre")
    private String nombre;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "miembro_pendiente_por_sector_organizacion", joinColumns = @JoinColumn(name = "sector_organizacion_id"), inverseJoinColumns = @JoinColumn(name = "miembro_pendiente_id"))
    private List<MiembroDeOrganizacion> miembrosPendientes;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "miembro_por_sector_organizacion", joinColumns = @JoinColumn(name = "sector_organizacion_id"), inverseJoinColumns = @JoinColumn(name = "miembro_id"))
    private List<MiembroDeOrganizacion> miembros;


    public SectorOrganizacion(String nombre) {
        this.nombre = nombre;
        this.miembrosPendientes = new ArrayList<>();
        this.miembros = new ArrayList<>();
    }

    public SectorOrganizacion() { }

    public void agregarMiembrosPendientes(MiembroDeOrganizacion ... miembros) {
        Collections.addAll(this.miembrosPendientes, miembros);
    }

    public void agregarMiembro(MiembroDeOrganizacion miembro) throws IOException {
        if(this.miembrosPendientes.contains(miembro)){
            Collections.addAll(this.miembros, miembro);
            this.miembrosPendientes.remove(miembro);
        }else{
            throw new IOException("No se puede agregar un miembro no pendiente a la organizacion");
            //sería que el miembro no hizo la solicitud a la organizacion creo
        }
    }

    public void eliminarMiembro(MiembroDeOrganizacion miembro) {
        this.miembros.remove(miembro);
    }

    public Double huellaDeCarbonoMensualMiembros(Organizacion organizacion,String mes, String anio) {
        return this.miembros.stream().mapToDouble(miembro -> miembro.huellaDeCarbonoOrganizacionalMensual(organizacion,mes,anio)).sum();
    }

    public Double huellaDeCarbonoAnualMiembros(Organizacion organizacion, String anio) {
        return this.miembros.stream().mapToDouble(miembro -> miembro.huellaDeCarbonoOrganizacionalAnual(organizacion,anio)).sum();
    }


    public Double indicadorHCPorMiembrosMensual(Organizacion organizacion,String mes, String anio) {
        return this.huellaDeCarbonoMensualMiembros(organizacion,mes,anio)/ getMiembros().size();
    }

    public Double indicadorHCPorMiembrosAnual(Organizacion organizacion, String anio) {
        return this.huellaDeCarbonoAnualMiembros(organizacion,anio)/ getMiembros().size();
    }

    public void eliminarMiembroPendiente(MiembroDeOrganizacion miembro) {
        this.miembrosPendientes.remove(miembro);
    }
}

