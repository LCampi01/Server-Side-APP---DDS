package models.entities.login;

final public class ContenerMayuscula implements Validable {

    public void validar(String contrasenia) throws ValidableException  {
        if (!contrasenia.chars().anyMatch(Character::isUpperCase)){
            throw new ValidableException("La contraseña debe contener al menos una letra mayúscula.");
        }
    }
}
