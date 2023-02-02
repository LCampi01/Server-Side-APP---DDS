package models.entities.sectores;

import db.EntidadPersistente;
import models.entities.huellaDeCarbono.HuellaDeCarbono;
import models.entities.huellaDeCarbono.UnidadHuellaCarbono;
import models.entities.organizaciones.Organizacion;
import models.entities.ubicaciones.territorio.Territorio;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "sector_territorial")
@Getter
public class Sector extends EntidadPersistente {

    @Column(name = "nombre")
    @Setter @Getter
    private String nombre;

    @ManyToMany
    @JoinTable(name = "sector_por_organizacion", joinColumns = @JoinColumn(name = "sector_id"), inverseJoinColumns = @JoinColumn(name = "organizacion_id"))
    private List<Organizacion> organizaciones;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "territorio_id", referencedColumnName = "id", unique = true)
    private Territorio territorio;

    public Sector(String nombre, Territorio territorio) {
        this.nombre = nombre;
        this.territorio = territorio;
        this.organizaciones = new ArrayList<>();
        territorio.setSector(this);
    }

    public Sector() { }

    public void agregarOrganizaciones(Organizacion... organizacionesAAgregar){
        Collections.addAll(this.organizaciones, organizacionesAAgregar);
    }


    public HuellaDeCarbono huellaDeCarbonoAnual(String anio) {
        Double valorHC = this.organizaciones.stream().mapToDouble(organizacion -> organizacion.huellaDeCarbonoAnual(anio).getValor()).sum();
        return this.generadorHC(valorHC);
    }

    public HuellaDeCarbono huellaDeCarbonoMensual(String mes, String anio) {
        Double valorHC = this.organizaciones.stream().mapToDouble(organizacion -> organizacion.huellaDeCarbonoMensual(mes, anio).getValor()).sum();

        return this.generadorHC(valorHC);
    }

    private HuellaDeCarbono generadorHC(Double valorHC){
        return new HuellaDeCarbono(valorHC, UnidadHuellaCarbono.GCO2EQ);
    }
}
