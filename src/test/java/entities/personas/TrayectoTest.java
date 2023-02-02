package entities.personas;

import models.entities.organizaciones.ClasificacionDeOrganizacion;
import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.TipoDeOrganizacion;
import models.entities.personas.MiembroDeOrganizacion;
import models.entities.personas.TipoDocumento;
import models.entities.personas.Tramo;
import models.entities.personas.Trayecto;
import models.entities.sectores.BuscadorDeSectores;
import models.entities.sectores.Sector;
import models.entities.transportes.*;
import models.entities.ubicaciones.territorio.Departamento;
import models.entities.ubicaciones.ubicacion.Direccion;
import models.entities.ubicaciones.ubicacion.Parada;
import models.entities.ubicaciones.territorio.Provincia;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


public class TrayectoTest {

    static Provincia bsAs = new Provincia("Buenos Aires");
    static Departamento quilmes = new Departamento("Quilmes",bsAs);

    static Sector sectorProvinciaBsAs = new Sector("provincia Bs As", bsAs);
    static Sector sectorDepartamentoQuilmes = new Sector("departamento Quilmes de Bs As", quilmes);

    static ClasificacionDeOrganizacion supermercado = new ClasificacionDeOrganizacion("Supermercados");
    static Direccion direccionQuilmes = new Direccion(quilmes,"Colon",65);

    static Organizacion coto = new Organizacion("Coto SA", TipoDeOrganizacion.EMPRESA, supermercado, direccionQuilmes);

    MiembroDeOrganizacion miembro1 = new MiembroDeOrganizacion(TipoDocumento.DNI, "456565456");
    MiembroDeOrganizacion miembro2 = new MiembroDeOrganizacion(TipoDocumento.PASAPORTE, "1212354");

    static VehiculoParticular vehiculoParticular = new VehiculoParticular(TipoDeVehiculo.CAMIONETA, TipoDeCombustible.GNC);
    static TransportePublico subteC = new TransportePublico(TipoTransportePublico.SUBTE, "A");

    static Parada independencia = new Parada("Independencia", 120.0, 0.0);
    static Parada moreno = new Parada("Moreno",40.0,120.0);
    static Parada avDeMayo = new Parada("Avenida de Mayo",100.0,40.0);
    static Parada diagonalNorte = new Parada("Diagonal Norte",0.0,100.0);

    static Tramo tramoA = new Tramo(vehiculoParticular,new Direccion(quilmes,"San Martin",45),new Direccion(quilmes,"Colon",65));
    static Tramo tramoB = new Tramo(subteC,diagonalNorte,moreno);
    static Tramo tramoC = new Tramo(subteC,avDeMayo,independencia);

    static LocalDate date = LocalDate.now();

    static Trayecto trayecto1 = new Trayecto(coto,"Ir a coto",10,date.withYear(2019).withMonth(2));
    static Trayecto trayecto2 = new Trayecto(coto,"Ir a coto",5,date.withYear(2019).withMonth(2));

    @BeforeAll
    static void init() {
        BuscadorDeSectores.instancia().agregarSectores(sectorProvinciaBsAs, sectorDepartamentoQuilmes);

        subteC.agregarParadas(diagonalNorte,avDeMayo,moreno,independencia);

        trayecto1.agregarTramo(tramoA);
        trayecto1.agregarTramo(tramoB);
        trayecto2.agregarTramo(tramoB);
        trayecto2.agregarTramo(tramoC);
    }

    @Test
    public void agregarTrayectoAMiembro1() {
        miembro1.agregarTrayectos(trayecto1);
        Assertions.assertEquals(1, miembro1.getTrayectos().size());
    }

    @Test
    public void miembrosCompartenTramos() {
        miembro2.agregarTrayectos(trayecto2);
        miembro1.agregarTrayectos(trayecto1);
        Assertions.assertEquals(2, tramoB.cantidadDeMiembros());
        Assertions.assertEquals(1, tramoA.cantidadDeMiembros());
        Assertions.assertEquals(1, tramoC.cantidadDeMiembros());
    }
}
