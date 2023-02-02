package models.entities.login;

import java.util.List;
import java.util.ArrayList;

final public class ContraseniaNoComun implements Validable {
    private List<String> contraseniasComunes;

    public ContraseniaNoComun() {
        this.contraseniasComunes = new ArrayList<>();
    }
    public ContraseniaNoComun(List<String> contraseniasComunes) {
        this.contraseniasComunes = contraseniasComunes;
    }

    public interface Importer { List<String> fetchContraseniasComunes(); }
    public interface Exporter { void storeContraseniasComunes(List<String> contraseniasComunes); }

    public ContraseniaNoComun(Importer source) {
        this.contraseniasComunes = source.fetchContraseniasComunes();
    }

    public ContraseniaNoComun(Exporter destination) {
        destination.storeContraseniasComunes(this.contraseniasComunes);
    }

    public void validar(String contrasenia) throws ValidableException {
        if (this.contraseniasComunes.contains(contrasenia)){
            throw new ValidableException("La contraseña es común.");
        }
    }
}

