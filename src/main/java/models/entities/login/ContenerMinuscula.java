package models.entities.login;

final public class ContenerMinuscula implements Validable {

    public void validar(String contrasenia) throws ValidableException {
        if (!contrasenia.chars().anyMatch(Character::isLowerCase)) {
            throw new ValidableException("La contraseña debe contener al menos una letra minúscula.");
        }
    }
}
