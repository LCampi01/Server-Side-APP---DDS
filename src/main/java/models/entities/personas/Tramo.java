package models.entities.personas;

import db.EntidadPersistente;
import models.entities.transportes.MedioDeTransporte;
import models.entities.ubicaciones.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tramo")
@Getter
@Setter
public class Tramo extends EntidadPersistente {
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "medio_de_transporte_id", referencedColumnName = "id")
    private MedioDeTransporte medioDeTransporte;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ubicacion_partida_id", referencedColumnName = "id")
    private Ubicacion puntoDePartida; //puede ser una parada o una direccion

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ubicacion_llegada_id", referencedColumnName = "id")
    private Ubicacion puntoDeLlegada; //puede ser una parada o una direccion

    @ManyToMany(mappedBy="tramos", cascade = CascadeType.ALL)
    private List<Trayecto> trayectos;

    @Column(name="distancia")
    private Double distancia;

    public Tramo(MedioDeTransporte medioDeTransporte, Ubicacion puntoDePartida, Ubicacion puntoDeLlegada) {
        this.medioDeTransporte = medioDeTransporte;
        this.puntoDePartida = puntoDePartida;
        this.puntoDeLlegada = puntoDeLlegada;
        this.trayectos = new ArrayList<>();
    }

    public Tramo() { }

    public void agregarTrayecto(Trayecto trayecto){
        trayectos.add(trayecto);
    }

    public Double calcularDistancia(){
        try {
//            System.out.println("calculando distancia");
            return this.medioDeTransporte.distancia(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Double huellaDeCarbono() throws IOException {
//        System.out.println("distacnia tramo" + this.distancia());
//        System.out.println("fe tramo" + this.medioDeTransporte.factorDeEmision());
//        System.out.println("cantidad miembros" + this.cantidadDeMiembros());
        return (this.distancia()*this.medioDeTransporte.factorDeEmision()) / this.cantidadDeMiembros();
    }

    public Integer cantidadDeMiembros(){
        return this.trayectos.size(); //hay un miembro por trayecto!!
    }

    public Double distancia(){
        if(this.distancia == null){
            this.distancia = calcularDistancia();
        }
        return this.distancia;
    }

}
