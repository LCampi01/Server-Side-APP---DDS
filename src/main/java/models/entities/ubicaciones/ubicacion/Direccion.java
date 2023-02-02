package models.entities.ubicaciones.ubicacion;

import models.entities.ubicaciones.territorio.Departamento;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Random;

@Entity
@DiscriminatorValue("direccion")
@Getter
@Setter
public class Direccion extends Ubicacion {

    @Column(name = "piso")
    private String piso;

    public Direccion(Departamento departamento, String calle, Integer altura) {
        super.departamento = departamento;
        super.calle = calle;
        super.altura = altura;
    }

    public Direccion(Departamento departamento, String calle, Integer altura, String piso) {
        super.departamento = departamento;
        super.calle = calle;
        super.altura = altura;
        this.piso = piso;
    }

    public Direccion() { }


    public Departamento getDepartamento() {
        return super.departamento;
    }

    public String getCalle() {
        return super.calle;
    }

    public Integer getAltura() {
        return super.altura;
    }


    public Integer getDepartamentoAsInteger() {
        return getDepartamentoIntegerRandom(); //provisorio hasta que se persistan las cosas
    }

    private Integer getDepartamentoIntegerRandom() {
        Random random = new Random();
        return random.nextInt(11); //random.nextInt(max - min + 1) + min
    }

}
