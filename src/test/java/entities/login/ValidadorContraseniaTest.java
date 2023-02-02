package entities.login;

import models.entities.login.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@DisplayName("Test ValidadorContrasenia")
public class ValidadorContraseniaTest {

    static Validable criterioMayuscula;
    static Validable criterioMinuscula;
    static Validable criterioNumero;
    static Validable criterioLongitud;
    static Validable criterioBuena;

    @BeforeAll
    static void init() {
        criterioMayuscula = new ContenerMayuscula();
        criterioMinuscula = new ContenerMinuscula();
        criterioNumero = new ContenerNumero();
        criterioLongitud = new RestringirLongitud();


        String inputFile = "src/main/resources/10-million-password-list-top-10000.txt";
        InputStream is = null;
        Charset charset = StandardCharsets.UTF_8;

        try {
            is = new FileInputStream(inputFile);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }

        ContraseniaNoComun.Importer importer = new ContraseniaNoComunStreamImporter(is, charset);
        criterioBuena = new ContraseniaNoComun(importer);
    }

    /* ********************************************************************* */
    /* ContenerMayuscula
    /* ********************************************************************* */

    @Test
    public void conetenerMayusculaContraseniaValida(){
        ValidadorContrasenia validador = new ValidadorContrasenia(criterioMayuscula);

        Assertions.assertDoesNotThrow(() -> { validador.validar("Password"); });
    }

    @Test
    public void conetenerMayusculaContraseniaInvalida(){
        ValidadorContrasenia validador = new ValidadorContrasenia(criterioMayuscula);

        Assertions.assertThrows(
            ValidableException.class,
            () -> { validador.validar("password"); });
    }

    /* ********************************************************************* */
    /* ContenerMinuscula
    /* ********************************************************************* */

    @Test
    public void conetenerMinusculaContraseniaValida(){
        ValidadorContrasenia validador = new ValidadorContrasenia(criterioMinuscula);

        Assertions.assertDoesNotThrow(() -> { validador.validar("password"); });
    }

    @Test
    public void conetenerMinusculaContraseniaInvalida(){
        ValidadorContrasenia validador = new ValidadorContrasenia(criterioMinuscula);

        Assertions.assertThrows(
                ValidableException.class,
                () -> { validador.validar("PASSWORD"); });
    }

    /* ********************************************************************* */
    /* ContenerNumero
    /* ********************************************************************* */

    @Test
    public void conetenerNumeroContraseniaValida(){
        ValidadorContrasenia validador = new ValidadorContrasenia(criterioNumero);

        Assertions.assertDoesNotThrow(() -> { validador.validar("password123"); });
    }
    @Test
    public void conetenerNumeroContraseniaInvalida(){
        ValidadorContrasenia validador = new ValidadorContrasenia(criterioNumero);

        Assertions.assertThrows(
                ValidableException.class,
                () -> { validador.validar("password"); });
    }

    /* ********************************************************************* */
    /* RestringirLongitud
    /* ********************************************************************* */

    @Test
    public void conetenerLongitudInferiorAlMinimo(){
        ValidadorContrasenia validador = new ValidadorContrasenia(criterioLongitud);

        Assertions.assertThrows(
                ValidableException.class,
                () -> { validador.validar("1234567"); });
    }
    @Test
    public void conetenerLongitudIgualAlMinimo(){
        ValidadorContrasenia validador = new ValidadorContrasenia(criterioLongitud);

        Assertions.assertDoesNotThrow(() -> { validador.validar("12345678"); });
    }
    @Test
    public void conetenerLongitudSuperiorAlMinimo(){
        ValidadorContrasenia validador = new ValidadorContrasenia(criterioLongitud);

        Assertions.assertDoesNotThrow(() -> { validador.validar("123456789"); });
    }

    /* ********************************************************************* */
    /* ContraseniaNoComun
    /* ********************************************************************* */

    @Test
    public void ContraseniaComun(){
        ValidadorContrasenia validador = new ValidadorContrasenia(criterioBuena);

        Assertions.assertThrows(
                ValidableException.class,
                () -> { validador.validar("12345678"); });
    }

    @Test
    public void ContraseniaNoComun(){
        ValidadorContrasenia validador = new ValidadorContrasenia(criterioBuena);

        Assertions.assertDoesNotThrow(() -> { validador.validar("!%6#8s65^s7r&G@wRZ65"); });
    }

    /* ********************************************************************* */
    /* Validacion de Multiples Criterios
    /* ********************************************************************* */

    @Test
    public void validacionContraseniaMultiplesCriterios(){
        ValidadorContrasenia validador = new ValidadorContrasenia(criterioMayuscula, criterioMinuscula, criterioNumero, criterioLongitud, criterioBuena);

        Assertions.assertDoesNotThrow(() -> { validador.validar("!%6#8s65^s7r&G@wRZ65"); });
    }

    /* ********************************************************************* */
    /* sacarCriterio
    /* ********************************************************************* */

    @Test
    public void sacarCriterio(){
        ValidadorContrasenia validador = new ValidadorContrasenia(criterioMayuscula, criterioMinuscula, criterioLongitud);

        validador.sacarCriterio(criterioLongitud);

        Assertions.assertDoesNotThrow(() -> { validador.validar("Cortita"); });
    }

    /* ********************************************************************* */
    /* agregarCriterio
    /* ********************************************************************* */

    @Test
    public void agregarCriterio(){
        ValidadorContrasenia validador = new ValidadorContrasenia(criterioMayuscula, criterioMinuscula);

        validador.agregarCriterio(criterioLongitud);

        Assertions.assertThrows(
                ValidableException.class,
                () -> { validador.validar("Cortita"); });
    }

    @Test
    public void agregarCriterios(){
        ValidadorContrasenia validador = new ValidadorContrasenia();

        validador.agregarCriterios(criterioMayuscula, criterioMinuscula, criterioLongitud);

        Assertions.assertThrows(
                ValidableException.class,
                () -> { validador.validar("Cortita"); });
    }
}
