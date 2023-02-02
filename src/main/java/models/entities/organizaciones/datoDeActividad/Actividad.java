package models.entities.organizaciones.datoDeActividad;

import db.EntidadPersistente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name="actividad")
@Getter
@Setter
public class Actividad extends EntidadPersistente {

    @Column(name="descripcion")
    private String descripcion;

    @Column(name="alcance")
    @Enumerated(EnumType.STRING)
    private Alcance alcance;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "actividad")
    private List<TipoDeConsumo> tiposDeConsumo;

    public Actividad(String descripcion, Alcance alcance) {
        this.descripcion = descripcion;
        this.alcance = alcance;
        this.tiposDeConsumo = new ArrayList<>();
    }

    public Actividad() { }

    public void agregarTiposDeConsumo(TipoDeConsumo ... tiposDeConsumo) {
        Collections.addAll(this.tiposDeConsumo, tiposDeConsumo);
        List<TipoDeConsumo> tipos = Arrays.asList(tiposDeConsumo.clone());
        tipos.forEach(tipo -> tipo.setActividad(this));
    }

}
