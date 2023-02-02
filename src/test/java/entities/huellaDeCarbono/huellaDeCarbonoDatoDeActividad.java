package entities.huellaDeCarbono;

import models.entities.factorDeEmision.BuscadorDeFactorDeEmision;
import models.entities.factorDeEmision.FactorDeEmision;
import models.entities.organizaciones.CargadorDeMediciones;
import models.entities.organizaciones.datoDeActividad.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class huellaDeCarbonoDatoDeActividad {
   TipoDeConsumo kerosene = new TipoDeConsumo("Kerosene", "lt");
   PeriodoDeImputacion periodo = new PeriodoDeImputacion("MENSUAL", "02/2020");
   Actividad combustionFija = new Actividad("Combustion fija", Alcance.EMISIONES_DIRECTAS);
   TipoDeConsumo gasNatural = new TipoDeConsumo("Gas Natural", "m3");
   TipoDeConsumo dieselGasoil = new TipoDeConsumo("Dieses/Gasoil", "lt");
   Actividad combustionMovil = new Actividad("Combustion movil", Alcance.EMISIONES_DIRECTAS);
   TipoDeConsumo gasoil = new TipoDeConsumo("Combustible consumido - Gasoil", "lts");
   TipoDeConsumo gnc = new TipoDeConsumo("Combustible consumido - GNC", "lts");
   TipoDeConsumo nafta = new TipoDeConsumo("Combustible consumido - Nafta", "lts");

    Actividad electricidadAdquirida = new Actividad("Electricidad adquirida y consumida", Alcance.EMISIONES_INDIRECTAS_ELECTRICIDAD);
    TipoDeConsumo electricidad = new TipoDeConsumo("Electricidad", "Kwh");

    CargadorDeMediciones lectorExcel;
    DatoDeActividad dato = new DatoDeActividad(combustionFija, kerosene, 20, periodo);

    public huellaDeCarbonoDatoDeActividad() throws IOException {
    }
    @BeforeEach
    public void init(){
        combustionFija.agregarTiposDeConsumo(gasNatural,dieselGasoil,kerosene);
        combustionMovil.agregarTiposDeConsumo(gasoil,gnc,nafta);
        electricidadAdquirida.agregarTiposDeConsumo(electricidad);
        lectorExcel = CargadorDeMediciones.instancia();
        lectorExcel.agregarActividades(combustionFija,combustionMovil,electricidadAdquirida);
        FactorDeEmision feElectricidad = new FactorDeEmision(15.0,electricidad);
        FactorDeEmision feNafta = new FactorDeEmision(23.0,nafta);
        FactorDeEmision feKerosene = new FactorDeEmision(56.0,kerosene);
        BuscadorDeFactorDeEmision.instancia().agregarFactores(feElectricidad,feKerosene,feNafta);
    }

    @Test
    public void calcularHuellaDeCarbonoDeDatoDeActividad(){
        assertEquals(1120.0, dato.huellaDeCarbono());
    }
    @Test
    public void calcularHuellaMensual(){
        assertEquals(1120.0, dato.huellaDeCarbonoMensual("2", "2020"));
    }
    @Test
    public void calcularHuellaDeCarbonoAnual(){
        assertEquals(1120.0, dato.huellaDeCarbonoAnual("2020"));
    }
}
