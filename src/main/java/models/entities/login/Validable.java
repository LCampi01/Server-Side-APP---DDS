package models.entities.login;

public interface Validable {
    void validar(String contrasenia) throws ValidableException;
}
