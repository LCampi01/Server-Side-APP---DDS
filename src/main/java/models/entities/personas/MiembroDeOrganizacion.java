package models.entities.personas;

import db.EntidadPersistente;
import models.entities.huellaDeCarbono.HuellaDeCarbono;
import models.entities.huellaDeCarbono.UnidadHuellaCarbono;
import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.SectorOrganizacion;
import lombok.Getter;
import lombok.Setter;
import models.entities.usuarios.Usuario;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "miembro_de_organizacion")
@Getter
@Setter
public class MiembroDeOrganizacion extends EntidadPersistente {
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name="tipo_de_documento")
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @Column(name = "numero_documento")
    private String numeroDocumento;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "miembro_id", referencedColumnName = "id")
    private List<Trayecto> trayectos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    public MiembroDeOrganizacion(TipoDocumento tipoDocumento, String numeroDocumento) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.trayectos = new ArrayList<>();
    }

    public MiembroDeOrganizacion(String nombre, String apellido, TipoDocumento tipoDocumento, String numeroDocumento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.trayectos = new ArrayList<>();
    }

    public MiembroDeOrganizacion() { }

    public void agregarTrayectos(Trayecto ... trayectos) {
        Collections.addAll(this.trayectos, trayectos);
        //this.trayectos.forEach(trayecto -> trayecto.agregarMiembro(this));
    }

    public void solicitarVinculacion(Organizacion organizacion, SectorOrganizacion sectorOrganizacion) throws IOException {
        organizacion.recibirSolicitudVinculacion(this, sectorOrganizacion);
    }
    public Double huellaDeCarbonoOrganizacionalMensual(Organizacion organizacion, String mes, String anio) {
        return this.trayectos.stream().mapToDouble(trayecto -> trayecto.huellaDeCarbonoOrganizacionalMensual(organizacion,mes,anio)).sum();
    }

    public Double huellaDeCarbonoOrganizacionalAnual(Organizacion organizacion, String anio) {
        return this.trayectos.stream().mapToDouble(trayecto -> trayecto.huellaDeCarbonoOrganizacionalAnual(organizacion,anio)).sum();
    }

    public HuellaDeCarbono huellaDeCarbonoMensual(String mes, String anio) {
        Double valorHC=this.trayectos.stream().mapToDouble(trayecto -> trayecto.huellaDeCarbonoMensual(mes,anio)).sum();
        return new HuellaDeCarbono(valorHC, UnidadHuellaCarbono.KGCO2EQ);
    }

    public HuellaDeCarbono huellaDeCarbonoAnual(String anio) {
        Double valorHC=this.trayectos.stream().mapToDouble(trayecto -> trayecto.huellaDeCarbonoAnual(anio)).sum();
        return new HuellaDeCarbono(valorHC, UnidadHuellaCarbono.KGCO2EQ);
    }

    public Double impactoPersonalEnOrganizacionAnual(Organizacion organizacion, String anio) {
        return (this.huellaDeCarbonoOrganizacionalAnual(organizacion,anio)/organizacion.huellaDeCarbonoAnual(anio).getValor()) * 100;
    }

    public Double impactoPersonalEnOrganizacionMensual(Organizacion organizacion,String mes, String anio) {
        return (this.huellaDeCarbonoOrganizacionalMensual(organizacion,mes,anio)/organizacion.huellaDeCarbonoMensual(mes,anio).getValor()) * 100;
    }
}


