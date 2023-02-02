package entities.sectoresTerritoriales;

import models.entities.factorDeEmision.FactorDeEmision;
import models.entities.organizaciones.ClasificacionDeOrganizacion;
import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.SectorOrganizacion;
import models.entities.organizaciones.TipoDeOrganizacion;
import models.entities.personas.MiembroDeOrganizacion;
import models.entities.personas.TipoDocumento;
import models.entities.personas.Tramo;
import models.entities.personas.Trayecto;
import models.entities.sectores.BuscadorDeSectores;
import models.entities.sectores.Sector;
import models.entities.transportes.TipoTransportePublico;
import models.entities.transportes.TransportePublico;
import models.entities.ubicaciones.territorio.Departamento;
import models.entities.ubicaciones.ubicacion.Direccion;
import models.entities.ubicaciones.ubicacion.Parada;
import models.entities.ubicaciones.territorio.Provincia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HCSectorTerritorialTest {

    Provincia bsAs = new Provincia("Buenos Aires");
    Departamento quilmes = new Departamento("Quilmes",bsAs);
    Departamento lanus = new Departamento("Lanus",bsAs);

    Sector sectorProvinciaBsAs = new Sector("provincia Bs As", bsAs);
    Sector sectorDepartamentoQuilmes = new Sector("departamento Quilmes de Bs As", quilmes);
    Sector sectorDepartamentoLanus = new Sector("departamento Lanus de Bs As", lanus);

    ClasificacionDeOrganizacion supermercado = new ClasificacionDeOrganizacion("Supermercados");

    // organizacion 1
    Direccion direccionQuilmes = new Direccion(quilmes,"San Martin",455);
    Organizacion coto = new Organizacion("Coto SA", TipoDeOrganizacion.EMPRESA, supermercado, direccionQuilmes);

    // organizacion 2
    Direccion direccionLanus1 = new Direccion(lanus,"Pueyrredon",555);
    Organizacion dia = new Organizacion("Dia SA", TipoDeOrganizacion.EMPRESA, supermercado, direccionLanus1);

    // organizacion 3
    Direccion direccionLanus2 = new Direccion(lanus,"Av de Mayo",120);
    Organizacion jumbo = new Organizacion("Jumbo SA", TipoDeOrganizacion.EMPRESA, supermercado, direccionLanus2);


    //cosas para generar HC en las organizaciones:
    TransportePublico subteA = new TransportePublico(TipoTransportePublico.SUBTE, "A");
    Parada paradaA = new Parada("paradaA", 0.0, 5.0);
    Parada paradaB = new Parada("paradaB", 5.0, 10.0);
    Parada paradaC = new Parada("paradaC", 10.0, 0.0);
    FactorDeEmision feSubte = new FactorDeEmision(2.0, "kgCO2eq/km");


    LocalDate date = LocalDate.now();



    // organizacion 1
    MiembroDeOrganizacion miembro1 = new MiembroDeOrganizacion(TipoDocumento.DNI, "4565654");
    SectorOrganizacion sectorOrganizacion1 = new SectorOrganizacion("marketing");
    Tramo tramo1 = new Tramo(subteA, paradaA, paradaC);
    Trayecto trayecto1 = new Trayecto(coto, "Ir a coto",10,date.withYear(2019).withMonth(2));

    // organizacion 2
    MiembroDeOrganizacion miembro2 = new MiembroDeOrganizacion(TipoDocumento.DNI, "2525526");
    SectorOrganizacion sectorOrganizacion2 = new SectorOrganizacion("marketing");
    Tramo tramo2 = new Tramo(subteA, paradaA, paradaC);
    Trayecto trayecto2 = new Trayecto(dia, "Ir a dia",10,date.withYear(2019).withMonth(2));

    // organizacion 3
    MiembroDeOrganizacion miembro3 = new MiembroDeOrganizacion(TipoDocumento.DNI, "12312312");
    SectorOrganizacion sectorOrganizacion3 = new SectorOrganizacion("marketing");
    Tramo tramo3 = new Tramo(subteA, paradaA, paradaC);
    Trayecto trayecto3 = new Trayecto(jumbo, "Ir a dia",10,date.withYear(2019).withMonth(2));

    @BeforeEach
    public void init () throws IOException {
        BuscadorDeSectores.instancia().agregarSectores(sectorProvinciaBsAs, sectorDepartamentoQuilmes, sectorDepartamentoLanus);

        subteA.agregarParadas(paradaA,paradaB,paradaC);
        subteA.setFactorDeEmision(feSubte);

        // organizacion 1
        coto.agregarSectoresOrganizacion(sectorOrganizacion1);
        sectorOrganizacion1.agregarMiembrosPendientes(miembro1);
        sectorOrganizacion1.agregarMiembro(miembro1);
        trayecto1.agregarTramo(tramo1);
        miembro1.agregarTrayectos(trayecto1);

        // organizacion 2
        dia.agregarSectoresOrganizacion(sectorOrganizacion2);
        sectorOrganizacion2.agregarMiembrosPendientes(miembro2);
        sectorOrganizacion2.agregarMiembro(miembro2);
        trayecto2.agregarTramo(tramo2);
        miembro2.agregarTrayectos(trayecto2);

        // organizacion 3
        jumbo.agregarSectoresOrganizacion(sectorOrganizacion3);
        sectorOrganizacion3.agregarMiembrosPendientes(miembro3);
        sectorOrganizacion3.agregarMiembro(miembro3);
        trayecto3.agregarTramo(tramo3);
        miembro3.agregarTrayectos(trayecto3);
    }

    @Test
    public void HCMensualProvincia() {
        //la distancia recorrida por el subte es 15 y el factor de emision es 2 ==> 30 por viaje.
        // Periodicidad mensual = 10  ==> 30*10 = 300 por mes por organizacion.
        // las 3 organizaciones de Pronvincia bsAs tienen la misma HC ==> 3*300 = 900
        assertEquals(900, sectorProvinciaBsAs.huellaDeCarbonoMensual("2","2020").getValor());
    }

    @Test
    public void HCAnualProvincia() {
        //la distancia recorrida por el subte es 15 y el factor de emision es 2 ==> 30 por viaje.
        // Periodicidad mensual = 10  ==> 30*10 *12 meses = 3600 por anio por organizacion.
        // las 3 organizaciones de Pronvincia bsAs tienen la misma HC ==> 3*3600 = 10800
        assertEquals(10800, sectorProvinciaBsAs.huellaDeCarbonoAnual("2020").getValor());
    }

    @Test
    public void HCMensualDepartamento() {
        //la distancia recorrida por el subte es 15 y el factor de emision es 2 ==> 30 por viaje.
        // Periodicidad mensual = 10  ==> 30*10 = 300 por mes por organizacion.
        // las 2 organizaciones del Departamento lanus tienen la misma HC ==> 2*300 = 600
        assertEquals(600, sectorDepartamentoLanus.huellaDeCarbonoMensual("2","2020").getValor());
    }

    @Test
    public void HCAnualDepartamento() {
        //la distancia recorrida por el subte es 15 y el factor de emision es 2 ==> 30 por viaje.
        // Periodicidad mensual = 10  ==> 30*10 *12 meses = 3600 por anio por organizacion.
        // las 3 organizaciones del Departamento lanus tienen la misma HC ==> 2*3600 = 7200
        assertEquals(7200, sectorDepartamentoLanus.huellaDeCarbonoAnual("2020").getValor());
    }

}
