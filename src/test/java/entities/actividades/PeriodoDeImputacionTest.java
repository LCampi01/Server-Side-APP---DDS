package entities.actividades;

import models.entities.organizaciones.datoDeActividad.PeriodoDeImputacion;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PeriodoDeImputacionTest {

    String mensajeErrorPeriodoAnual = "El periodo anual esta en un formato invalido";
    String mensajeErrorPeriodoMensual = "El periodo mensual esta en un formato invalido";

    @Test
    public void periodicidadInvalida() throws IOException {
        Exception exception = assertThrows(
                IOException.class,
                () -> { new PeriodoDeImputacion("anuales", "2020"); }
        );

        assertEquals("La periodicidad ingresada no es valida", exception.getMessage());
    }

    @Test
    public void periodoAnualValido() throws IOException {
        assertDoesNotThrow( () -> { new PeriodoDeImputacion("Anual", "2020"); } );
    }

    @Test
    public void periodoAnualInvalido() throws IOException {
        Exception exception = assertThrows(
            IOException.class,
            () -> { new PeriodoDeImputacion("Anual", "202"); }
        );

        assertEquals(mensajeErrorPeriodoAnual, exception.getMessage());
    }

    @Test
    public void periodoMensualValido() throws IOException {
        assertDoesNotThrow( () -> { new PeriodoDeImputacion("Mensual", "01/2020"); } );
    }

    @Test
    public void periodoMensualSinMesEsInvalido() throws IOException {
        Exception exception = assertThrows(
            IOException.class,
            () -> { new PeriodoDeImputacion("Mensual", "2020"); }
        );

        assertEquals(mensajeErrorPeriodoMensual, exception.getMessage());
    }

    @Test
    public void periodoMensualConMes00EsInvalido() throws IOException {
        Exception exception = assertThrows(
                IOException.class,
                () -> { new PeriodoDeImputacion("Mensual", "00/2020"); }
        );

        assertEquals(mensajeErrorPeriodoMensual, exception.getMessage());
    }

    @Test
    public void periodoMensualConMesMayorA12EsInvalido() throws IOException {
        Exception exception = assertThrows(
                IOException.class,
                () -> { new PeriodoDeImputacion("Mensual", "13/2020"); }
        );

        assertEquals(mensajeErrorPeriodoMensual, exception.getMessage());
    }

    @Test
    public void periodoMensualConAnioAdelanteEsInvalido() throws IOException {
        Exception exception = assertThrows(
                IOException.class,
                () -> { new PeriodoDeImputacion("Mensual", "2020/01"); }
        );

        assertEquals(mensajeErrorPeriodoMensual, exception.getMessage());
    }

}
