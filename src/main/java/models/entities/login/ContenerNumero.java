package models.entities.login;

final public class ContenerNumero implements Validable {

    public void validar(String contrasenia) throws ValidableException {
        if (!contrasenia.chars().anyMatch(Character::isDigit)){
            throw new ValidableException("La contraseña debe contener al menos un número.");
        }
    }
}
