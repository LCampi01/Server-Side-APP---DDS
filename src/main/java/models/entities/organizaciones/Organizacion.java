package models.entities.organizaciones;

import db.EntidadPersistente;
import models.entities.huellaDeCarbono.HuellaDeCarbono;
import models.entities.huellaDeCarbono.UnidadHuellaCarbono;
import models.entities.organizaciones.datoDeActividad.DatoDeActividad;
import models.entities.personas.MiembroDeOrganizacion;
import models.entities.sectores.BuscadorDeSectores;
import models.entities.ubicaciones.ubicacion.Direccion;
import lombok.Getter;
import lombok.Setter;
import models.entities.usuarios.Usuario;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table (name = "organizacion")
@Getter
@Setter
public class Organizacion extends EntidadPersistente {

    @Column(name= "razon_social", unique = true)
    private String razonSocial;

    @Column(name = "tipo_de_organizacion")
    @Enumerated(EnumType.STRING)
    private TipoDeOrganizacion tipoDeOrganizacion;

    @ManyToOne
    @JoinColumn(name= "clasificacion_de_organizacion_id", referencedColumnName = "id")
    private ClasificacionDeOrganizacion clasificacionDeOrganizacion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "direccion_id", referencedColumnName = "id")
    private Direccion direccion;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", referencedColumnName = "id")
    private List<SectorOrganizacion> sectoresOrganizacion;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", referencedColumnName = "id")
    private List<DatoDeActividad> datosDeActividad;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", referencedColumnName = "id")
    private List<Contacto> contactos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;


    /*
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Getter private List<Sector> sectores; //por su ubicacion, tiene uno o dos sectores
    // Habria que ver si es necesario que la organizacion conozca sus sectores. Yo (Santi) diría que creo que no. por ahora lo dejo.
    Guada: coincido con santi, comento todo eso porque creo q es al pedo hacerlo bidireccional
    */


    public Organizacion(String razonSocial, TipoDeOrganizacion tipoDeOrganizacion, ClasificacionDeOrganizacion clasificacionDeOrganizacion, Direccion direccion) {
        this.razonSocial = razonSocial;
        this.tipoDeOrganizacion = tipoDeOrganizacion;
        this.clasificacionDeOrganizacion = clasificacionDeOrganizacion;
        this.sectoresOrganizacion = new ArrayList<>();
        this.direccion = direccion;
        this.datosDeActividad = new ArrayList<>();
        this.contactos = new ArrayList<>();
        this.cargarSectores();
    }

    public Organizacion () { }

    public void agregarSectoresOrganizacion(SectorOrganizacion... sectoresAAgregar) {
        Collections.addAll(this.sectoresOrganizacion, sectoresAAgregar);
    }

    public void cargarSectores(){
        BuscadorDeSectores.instancia().asignarSectores(this);
    }
    /*
    public void agregarSectores(List<Sector> sectores) {
        this.sectores.addAll(sectores);
    }
    */

    public void recibirSolicitudVinculacion(MiembroDeOrganizacion miembro, SectorOrganizacion sectorOrganizacion) throws IOException {
        if(sectoresOrganizacion.contains(sectorOrganizacion)){
            sectorOrganizacion.agregarMiembrosPendientes(miembro);
        }else{
            throw new IOException("El sector ingresado no pertenece a esta organizacion");
        }
    }

    public void aceptarVinculacion(MiembroDeOrganizacion miembro, SectorOrganizacion sectorOrganizacion) throws IOException {
        if(sectoresOrganizacion.contains(sectorOrganizacion)){
            sectorOrganizacion.agregarMiembro(miembro);
        }else{
            throw new IOException("El sector ingresado no pertenece a esta organizacion");
        }
    }

    public void rechazarVinculacion(MiembroDeOrganizacion miembro, SectorOrganizacion sectorOrganizacion) throws IOException {
        if(sectoresOrganizacion.contains(sectorOrganizacion)){
            sectorOrganizacion.eliminarMiembroPendiente(miembro);
        }else{
            throw new IOException("El sector ingresado no pertenece a esta organizacion");
        }
    }


    public void cargarMediciones(List<DatoDeActividad> mediciones) {
        this.datosDeActividad.addAll(mediciones);
    }

    public HuellaDeCarbono huellaDeCarbonoMensual(String mes, String anio) {
        Double hcDatosActividad = this.huellaDeCarbonoActividadesMensual(mes,anio);
        Double hcTrayectos = this.huellaDeCarbonoTrayectosMensual(mes,anio);
        Double valorHC = hcDatosActividad  + hcTrayectos;
        return new HuellaDeCarbono(valorHC, UnidadHuellaCarbono.KGCO2EQ) ;
    }

    public HuellaDeCarbono huellaDeCarbonoAnual(String anio) {
        Double hcDatosActividad = this.huellaDeCarbonoActividadesAnual(anio);
        Double hcTrayectos = this.huellaDeCarbonoTrayectosAnual(anio);
        Double valorHC = hcDatosActividad  + hcTrayectos;
        return new HuellaDeCarbono(valorHC, UnidadHuellaCarbono.KGCO2EQ) ;
    }

    public Double huellaDeCarbonoActividadesMensual(String mes, String anio) {
        return this.datosDeActividad.stream().mapToDouble(dato -> dato.huellaDeCarbonoMensual(mes, anio)).sum();
    }

    public Double huellaDeCarbonoActividadesAnual(String anio) {
        return this.datosDeActividad.stream().mapToDouble(dato -> dato.huellaDeCarbonoAnual(anio)).sum();
    }

    public Double huellaDeCarbonoTrayectosMensual(String mes, String anio) { //evaluar periodicidad de un trayecto
        return this.sectoresOrganizacion.stream().mapToDouble(sectorOrganizacion -> sectorOrganizacion.huellaDeCarbonoMensualMiembros(this,mes,anio)).sum();
    }

    public Double huellaDeCarbonoTrayectosAnual(String anio) { //evaluar periodicidad de un trayecto
        return this.sectoresOrganizacion.stream().mapToDouble(sectorOrganizacion -> sectorOrganizacion.huellaDeCarbonoAnualMiembros(this,anio)).sum();
    }


    public void agregarContactos(Contacto ... contactos) {
        Collections.addAll(this.contactos, contactos);
    }

    public Double indicadorHCPorMiembrosMensual(SectorOrganizacion sectorOrganizacion,String mes, String anio){
        return sectorOrganizacion.indicadorHCPorMiembrosMensual(this,mes,anio);
    }

    public Double indicadorHCPorMiembrosAnual(SectorOrganizacion sectorOrganizacion, String anio){
        return sectorOrganizacion.indicadorHCPorMiembrosAnual(this, anio);
    }

}
