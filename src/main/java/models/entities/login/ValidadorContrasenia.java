package models.entities.login;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final public class ValidadorContrasenia {

    private List<Validable> criterios;

    public ValidadorContrasenia() {
        this.criterios = new ArrayList<>();
    }

    public ValidadorContrasenia(Validable ... criterios) {
        this.criterios = new ArrayList<>();
        this.agregarCriterios(criterios);
    }

    public void validar(String contrasenia) throws ValidableException {
        for (Validable criterio: criterios) {
            criterio.validar(contrasenia);
        };
    }

    public void agregarCriterios(Validable ... criterios){
        Collections.addAll(this.criterios, criterios);
    }

    public void agregarCriterio(Validable criterio){this.agregarCriterios(criterio);}

    public void sacarCriterio(Validable criterio){this.criterios.remove(criterio);}
}
