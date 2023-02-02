package models.entities.login;

final public class RestringirLongitud implements Validable {
    private Integer minimo = 8;

    public void validar(String contrasenia) throws ValidableException {
        if (contrasenia.length() < minimo){
            throw new ValidableException(String.format("La contraseña debe tener al menos %d caracteres.", minimo));
        }
    }
}
