package persistenciaDb;

import db.EntityManagerHelper;
import models.entities.factorDeEmision.BuscadorDeFactorDeEmision;
import models.entities.factorDeEmision.FactorDeEmision;
import models.entities.organizaciones.*;
import models.entities.organizaciones.datoDeActividad.*;
import models.entities.personas.MiembroDeOrganizacion;
import models.entities.personas.TipoDocumento;
import models.entities.personas.Tramo;
import models.entities.personas.Trayecto;
import models.entities.sectores.AgenteSectorial;
import models.entities.sectores.Sector;
import models.entities.transportes.*;
import models.entities.ubicaciones.territorio.Departamento;
import models.entities.ubicaciones.territorio.Provincia;
import models.entities.ubicaciones.ubicacion.Direccion;
import models.entities.ubicaciones.ubicacion.Parada;
import models.entities.usuarios.RolEnum;
import models.entities.usuarios.Usuario;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PersistirDatosTest {
/*
    @Test //PRIMERO HAY QUE PERSISTIR LAS COSAS EN NUESTRA DB
    public void persisteDeTodo() throws IOException {

        ////////////////////  Persistencia de ClasificacionDeOrganizacion  ////////////////////////////////
        ClasificacionDeOrganizacion universidad = new ClasificacionDeOrganizacion("Universidad");
        ClasificacionDeOrganizacion ministerio = new ClasificacionDeOrganizacion("Ministerio");
        ClasificacionDeOrganizacion escuela = new ClasificacionDeOrganizacion("Escuela");
        ClasificacionDeOrganizacion empresaPrimaria = new ClasificacionDeOrganizacion("Empresa Primaria");
        ClasificacionDeOrganizacion empresaSecundaria= new ClasificacionDeOrganizacion("Empresa Secundaria");

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(universidad);
        EntityManagerHelper.getEntityManager().persist(ministerio);
        EntityManagerHelper.getEntityManager().persist(escuela);
        EntityManagerHelper.getEntityManager().persist(empresaPrimaria);
        EntityManagerHelper.getEntityManager().persist(empresaSecundaria);
        EntityManagerHelper.commit();


        ////////////////////  Persistencia de Provincias - Departamentos - Sectores  ////////////////////////////////
        Provincia bsAs = new Provincia("Buenos Aires");
        Provincia cordoba = new Provincia("Cordoba");
        Departamento quilmes = new Departamento("Quilmes",bsAs);
        Departamento caniuelas = new Departamento("Canuelas",bsAs);
        Departamento carlosPaz = new Departamento("Carlos Paz",cordoba);

        Sector sectorProvinciaBsAs = new Sector("provincia Bs As", bsAs);
        Sector sectorProvinciaCordoba = new Sector("provincia Cordoba", cordoba);
        Sector sectorDepartamentoQuilmes = new Sector("departamento Quilmes de Bs As", quilmes);
        Sector sectorDepartamentoCaniuelas = new Sector("departamento Canuelas de Bs As", caniuelas);
        Sector sectorDepartamentoCarlosPaz = new Sector("departamento Carlos Paz de Cordoba", carlosPaz);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(sectorProvinciaBsAs);
        EntityManagerHelper.getEntityManager().persist(bsAs);
        EntityManagerHelper.getEntityManager().persist(sectorDepartamentoQuilmes);
        EntityManagerHelper.getEntityManager().persist(quilmes);
        EntityManagerHelper.getEntityManager().persist(sectorDepartamentoCaniuelas);
        EntityManagerHelper.getEntityManager().persist(caniuelas);
        EntityManagerHelper.getEntityManager().persist(sectorProvinciaCordoba);
        EntityManagerHelper.getEntityManager().persist(cordoba);
        EntityManagerHelper.getEntityManager().persist(sectorDepartamentoCarlosPaz);
        EntityManagerHelper.getEntityManager().persist(carlosPaz);
        EntityManagerHelper.commit();

        ////////////////////  Persistencia de AgenteSectorial  ////////////////////////////////
        AgenteSectorial agenteSectorialBsAs = new AgenteSectorial("Pepe A.S. Bs As", sectorProvinciaBsAs);
        AgenteSectorial agenteSectorialCordoba = new AgenteSectorial("John A.S. Cordoba", sectorProvinciaCordoba);
        AgenteSectorial agenteSectorialQuilmes = new AgenteSectorial("Maria A.S. Quilmes", sectorDepartamentoQuilmes);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(agenteSectorialBsAs);
        EntityManagerHelper.getEntityManager().persist(agenteSectorialCordoba);
        EntityManagerHelper.getEntityManager().persist(agenteSectorialQuilmes);
        EntityManagerHelper.commit();

        ////////////////////  Persistencia de Direcciones  ////////////////////////////////
        Direccion direccionCotoQuilmes = new Direccion(quilmes, "Av San Martin", 500);
        Direccion direccionJumboQuilmes = new Direccion(quilmes, "Av Libertador", 355);
        Direccion direccionCaniuelas = new Direccion(caniuelas, "Av de mayo", 1230);
        Direccion direccionUtnQuilmes = new Direccion(quilmes, "Calle false", 123);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(direccionCotoQuilmes);
        EntityManagerHelper.getEntityManager().persist(direccionJumboQuilmes);
        EntityManagerHelper.getEntityManager().persist(direccionCaniuelas);
        EntityManagerHelper.getEntityManager().persist(direccionUtnQuilmes);
        EntityManagerHelper.commit();


        ////////////////////  Persistencia de Organizaciones  ////////////////////////////////
        Organizacion coto = new Organizacion("Coto SA", TipoDeOrganizacion.EMPRESA, empresaPrimaria, direccionCotoQuilmes);
        Organizacion jumbo = new Organizacion("Jumbo SA", TipoDeOrganizacion.EMPRESA, empresaPrimaria, direccionJumboQuilmes);
        Organizacion dia = new Organizacion("Dia SA", TipoDeOrganizacion.EMPRESA, empresaPrimaria, direccionCaniuelas);
        Organizacion utn = new Organizacion("UTN", TipoDeOrganizacion.INSTITUCION, universidad, direccionUtnQuilmes);
        Organizacion disco = new Organizacion("Disco SA", TipoDeOrganizacion.EMPRESA, empresaPrimaria, direccionCotoQuilmes);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(coto);
        EntityManagerHelper.getEntityManager().persist(jumbo);
        EntityManagerHelper.getEntityManager().persist(dia);
        EntityManagerHelper.getEntityManager().persist(utn);
        EntityManagerHelper.getEntityManager().persist(disco);
        EntityManagerHelper.commit();


        ////////////////////  Persistencia de Contactos  ////////////////////////////////
        Contacto contacto1Coto = new Contacto("eze coto","ezecoto@gmail.com", "1122334455");
        Contacto contacto2Coto = new Contacto("lalo coto","lalocoto@gmail.com", "1166778899");
        coto.agregarContactos(contacto1Coto, contacto2Coto);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(contacto1Coto);
        EntityManagerHelper.getEntityManager().persist(contacto2Coto);
        EntityManagerHelper.commit();


        ////////////////////  Persistencia de Sectores de Organizacion  ////////////////////////////////
        SectorOrganizacion sectorCotoMarketing = new SectorOrganizacion("Marketing");
        SectorOrganizacion sectorCotoVentas = new SectorOrganizacion("Ventas");
        SectorOrganizacion sectorJumboMarketing = new SectorOrganizacion("Marketing");
        SectorOrganizacion sectorUTNLimpieza = new SectorOrganizacion("Limpieza");
        SectorOrganizacion sectorDiscoMarketing = new SectorOrganizacion("Marketing");

        coto.agregarSectoresOrganizacion(sectorCotoMarketing, sectorCotoVentas);
        jumbo.agregarSectoresOrganizacion(sectorJumboMarketing);
        utn.agregarSectoresOrganizacion(sectorUTNLimpieza);
        disco.agregarSectoresOrganizacion(sectorDiscoMarketing);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(sectorCotoMarketing);
        EntityManagerHelper.getEntityManager().persist(sectorCotoVentas);
        EntityManagerHelper.getEntityManager().persist(sectorJumboMarketing);
        EntityManagerHelper.getEntityManager().persist(sectorUTNLimpieza);
        EntityManagerHelper.getEntityManager().persist(sectorDiscoMarketing);
        EntityManagerHelper.commit();


        ////////////////////  Persistencia de Miembros  ////////////////////////////////
        MiembroDeOrganizacion miembroCoto1 = new MiembroDeOrganizacion("Agustin", "Dubatti", TipoDocumento.DNI, "11111111");
        MiembroDeOrganizacion miembroCoto2 = new MiembroDeOrganizacion("Guadalupe", "Lemme", TipoDocumento.DNI, "22222222");
        MiembroDeOrganizacion miembroCoto3 = new MiembroDeOrganizacion("Santiago", "Orlando", TipoDocumento.DNI, "33333333");
        MiembroDeOrganizacion miembroCompartido2 = new MiembroDeOrganizacion("Sofia", "Palmieri", TipoDocumento.DNI, "44444444");
        MiembroDeOrganizacion miembroCompartido = new MiembroDeOrganizacion("Rober", "QEPD", TipoDocumento.DNI, "55555555");
        MiembroDeOrganizacion miembroDiscoUnico = new MiembroDeOrganizacion("Lucas", "Campi", TipoDocumento.DNI, "66666666");

        sectorCotoMarketing.agregarMiembrosPendientes(miembroCoto1, miembroCoto2, miembroCompartido2);
        sectorCotoVentas.agregarMiembrosPendientes(miembroCoto3, miembroCompartido);
        sectorJumboMarketing.agregarMiembrosPendientes(miembroCoto1,miembroCompartido2, miembroCompartido);
        sectorUTNLimpieza.agregarMiembrosPendientes(miembroCoto1);
        sectorDiscoMarketing.agregarMiembrosPendientes(miembroDiscoUnico);

        sectorUTNLimpieza.agregarMiembro(miembroCoto1);
        sectorCotoMarketing.agregarMiembro(miembroCoto1);
        sectorCotoMarketing.agregarMiembro(miembroCoto2);
        sectorCotoVentas.agregarMiembro(miembroCoto3);
        sectorCotoVentas.agregarMiembro(miembroCompartido);
        sectorJumboMarketing.agregarMiembro(miembroCompartido);
        //el miembroCompartido2 queda en la lista de pendientes de jumboMarketing
        //pero lo agregaos a cotoMarketing
        sectorCotoMarketing.agregarMiembro(miembroCompartido2);
        sectorDiscoMarketing.agregarMiembro(miembroDiscoUnico);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(miembroCoto1);
        EntityManagerHelper.getEntityManager().persist(miembroCoto2);
        EntityManagerHelper.getEntityManager().persist(miembroCoto3);
        EntityManagerHelper.getEntityManager().persist(miembroCompartido2);
        EntityManagerHelper.getEntityManager().persist(miembroCompartido);
        EntityManagerHelper.getEntityManager().persist(miembroDiscoUnico);
        EntityManagerHelper.commit();


        ////////////////////////////////////  Persistencia de Transportes  ////////////////////////////////////////
        ////////////////////  Persistencia de SinVehiculo  ////////////////////////////////
        SinVehiculo sinVehiculo = new SinVehiculo();
        FactorDeEmision feSinVehiculo = new FactorDeEmision();
        sinVehiculo.setFactorDeEmision(feSinVehiculo);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(feSinVehiculo);
        EntityManagerHelper.getEntityManager().persist(sinVehiculo);
        EntityManagerHelper.commit();

        ////////////////////  Persistencia de VehiculoParticular  ////////////////////////////////
        //AUTOS
        VehiculoParticular autoGnc = new VehiculoParticular(TipoDeVehiculo.AUTO, TipoDeCombustible.GNC);
        VehiculoParticular autoNafta = new VehiculoParticular(TipoDeVehiculo.AUTO, TipoDeCombustible.NAFTA);
        VehiculoParticular autoElectrico = new VehiculoParticular(TipoDeVehiculo.AUTO, TipoDeCombustible.ELECTRICO);
        VehiculoParticular autoGasoil = new VehiculoParticular(TipoDeVehiculo.AUTO, TipoDeCombustible.GASOIL);

        FactorDeEmision feAutoGnc = new FactorDeEmision(0.7, "kg/km");
        FactorDeEmision feAutoNafta = new FactorDeEmision(0.3, "kg/km");
        FactorDeEmision feAutoElectrico = new FactorDeEmision(0.1, "kg/km");
        FactorDeEmision feAutoGasoil = new FactorDeEmision(0.8, "kg/km");

        autoGnc.setFactorDeEmision(feAutoGnc);
        autoNafta.setFactorDeEmision(feAutoNafta);
        autoElectrico.setFactorDeEmision(feAutoElectrico);
        autoGasoil.setFactorDeEmision(feAutoGasoil);

        //MOTOS
        VehiculoParticular motoGnc = new VehiculoParticular(TipoDeVehiculo.MOTO, TipoDeCombustible.GNC);
        VehiculoParticular motoNafta = new VehiculoParticular(TipoDeVehiculo.MOTO, TipoDeCombustible.NAFTA);
        VehiculoParticular motoElectrico = new VehiculoParticular(TipoDeVehiculo.MOTO, TipoDeCombustible.ELECTRICO);
        VehiculoParticular motoGasoil = new VehiculoParticular(TipoDeVehiculo.MOTO, TipoDeCombustible.GASOIL);

        FactorDeEmision feMotoGnc = new FactorDeEmision(0.4, "kg/km");
        FactorDeEmision feMotoNafta = new FactorDeEmision(0.1, "kg/km");
        FactorDeEmision feMotoElectrico = new FactorDeEmision(0.2, "kg/km");
        FactorDeEmision feMotoGasoil = new FactorDeEmision(0.6, "kg/km");

        motoGnc.setFactorDeEmision(feMotoGnc);
        motoNafta.setFactorDeEmision(feMotoNafta);
        motoElectrico.setFactorDeEmision(feMotoElectrico);
        motoGasoil.setFactorDeEmision(feMotoGasoil);

        //CAMIONETA
        VehiculoParticular camionetaGnc = new VehiculoParticular(TipoDeVehiculo.CAMIONETA, TipoDeCombustible.GNC);
        VehiculoParticular camionetaNafta = new VehiculoParticular(TipoDeVehiculo.CAMIONETA, TipoDeCombustible.NAFTA);
        VehiculoParticular camionetaElectrico = new VehiculoParticular(TipoDeVehiculo.CAMIONETA, TipoDeCombustible.ELECTRICO);
        VehiculoParticular camionetaGasoil = new VehiculoParticular(TipoDeVehiculo.CAMIONETA, TipoDeCombustible.GASOIL);

        FactorDeEmision feCamionetaGnc = new FactorDeEmision(0.42, "kg/km");
        FactorDeEmision feCamionetaNafta = new FactorDeEmision(0.12, "kg/km");
        FactorDeEmision feCamionetaElectrico = new FactorDeEmision(0.2, "kg/km");
        FactorDeEmision feCamionetaGasoil = new FactorDeEmision(0.62, "kg/km");

        camionetaGnc.setFactorDeEmision(feCamionetaGnc);
        camionetaNafta.setFactorDeEmision(feCamionetaNafta);
        camionetaElectrico.setFactorDeEmision(feCamionetaElectrico);
        camionetaGasoil.setFactorDeEmision(feCamionetaGasoil);


        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(feAutoGnc);
        EntityManagerHelper.getEntityManager().persist(feAutoNafta);
        EntityManagerHelper.getEntityManager().persist(feAutoElectrico);
        EntityManagerHelper.getEntityManager().persist(feAutoGasoil);

        EntityManagerHelper.getEntityManager().persist(feMotoGnc);
        EntityManagerHelper.getEntityManager().persist(feMotoNafta);
        EntityManagerHelper.getEntityManager().persist(feMotoElectrico);
        EntityManagerHelper.getEntityManager().persist(feMotoGasoil);

        EntityManagerHelper.getEntityManager().persist(feCamionetaGnc);
        EntityManagerHelper.getEntityManager().persist(feCamionetaNafta);
        EntityManagerHelper.getEntityManager().persist(feCamionetaElectrico);
        EntityManagerHelper.getEntityManager().persist(feCamionetaGasoil);


        EntityManagerHelper.getEntityManager().persist(autoGnc);
        EntityManagerHelper.getEntityManager().persist(autoNafta);
        EntityManagerHelper.getEntityManager().persist(autoElectrico);
        EntityManagerHelper.getEntityManager().persist(autoGasoil);

        EntityManagerHelper.getEntityManager().persist(motoGnc);
        EntityManagerHelper.getEntityManager().persist(motoNafta);
        EntityManagerHelper.getEntityManager().persist(motoElectrico);
        EntityManagerHelper.getEntityManager().persist(motoGasoil);

        EntityManagerHelper.getEntityManager().persist(camionetaGnc);
        EntityManagerHelper.getEntityManager().persist(camionetaNafta);
        EntityManagerHelper.getEntityManager().persist(camionetaElectrico);
        EntityManagerHelper.getEntityManager().persist(camionetaGasoil);

        EntityManagerHelper.commit();

        ////////////////////  Persistencia de ServicioContratado  ////////////////////////////////
        ServicioContratado uber = new ServicioContratado("Uber");
        FactorDeEmision feUber = new FactorDeEmision(0.4, "kg/km");
        uber.setFactorDeEmision(feUber);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(feUber);
        EntityManagerHelper.getEntityManager().persist(uber);
        EntityManagerHelper.commit();

        ////////////////////  Persistencia de TransportePublico  ////////////////////////////////
        //TREN
        TransportePublico trenRoca = new TransportePublico(TipoTransportePublico.TREN, "Roca");
        FactorDeEmision feTrenRoca = new FactorDeEmision(0.2, "kg/km");
        trenRoca.setFactorDeEmision(feTrenRoca);

        Parada parada1TrenRoca = new Parada("Parada 1 Tren Roca (primera)", 0.0, 10.0);
        Parada parada2TrenRoca = new Parada("Parada 2 Tren Roca", 10.0, 5.0);
        Parada parada3TrenRoca = new Parada("Parada 3 Tren Roca (última)", 5.0, 0.0);
        trenRoca.agregarParadas(parada1TrenRoca, parada2TrenRoca, parada3TrenRoca);

        //COLECTIVO
        TransportePublico bondi60 = new TransportePublico(TipoTransportePublico.COLECTIVO, "60");
        FactorDeEmision feBondi60 = new FactorDeEmision(0.5, "kg/km");
        bondi60.setFactorDeEmision(feBondi60);

        Parada parada1Bondi60 = new Parada("Parada 1 Bondi 60 (primera) - Goyena y Ramos", 0.0, 100.0);
        Parada parada2Bondi60 = new Parada("Parada 2 Bondi 60 - Ramos y Rivadavia", 100.0, 50.0);
        Parada parada3Bondi60 = new Parada("Parada 3 Bondi 60 (última) - Ramos y Corrientes", 50.0, 0.0);
        bondi60.agregarParadas(parada1Bondi60, parada2Bondi60, parada3Bondi60);

        TransportePublico bondi203 = new TransportePublico(TipoTransportePublico.COLECTIVO, "203");
        FactorDeEmision feBondi203 = new FactorDeEmision(0.5, "kg/km");
        bondi203.setFactorDeEmision(feBondi203);

        Parada parada1Bondi203 = new Parada("Parada 1 Bondi 203 (primera) - Goyena y Ramos", 0.0, 100.0);
        Parada parada2Bondi203 = new Parada("Parada 2 Bondi 203 - Ramos y Rivadavia", 100.0, 50.0);
        Parada parada3Bondi203 = new Parada("Parada 3 Bondi 203 (última) - Ramos y Corrientes", 50.0, 0.0);
        bondi203.agregarParadas(parada1Bondi203, parada2Bondi203, parada3Bondi203);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(feTrenRoca);
        EntityManagerHelper.getEntityManager().persist(trenRoca);
        EntityManagerHelper.getEntityManager().persist(parada1TrenRoca);
        EntityManagerHelper.getEntityManager().persist(parada2TrenRoca);
        EntityManagerHelper.getEntityManager().persist(parada3TrenRoca);

        EntityManagerHelper.getEntityManager().persist(feBondi60);
        EntityManagerHelper.getEntityManager().persist(bondi60);
        EntityManagerHelper.getEntityManager().persist(parada1Bondi60);
        EntityManagerHelper.getEntityManager().persist(parada2Bondi60);
        EntityManagerHelper.getEntityManager().persist(parada3Bondi60);

        EntityManagerHelper.getEntityManager().persist(feBondi203);
        EntityManagerHelper.getEntityManager().persist(bondi203);
        EntityManagerHelper.getEntityManager().persist(parada1Bondi203);
        EntityManagerHelper.getEntityManager().persist(parada2Bondi203);
        EntityManagerHelper.getEntityManager().persist(parada3Bondi203);
        EntityManagerHelper.commit();



        ////////////////////  Persistencia de Trayectos  ////////////////////////////////
        LocalDate date = LocalDate.now();
        LocalDate fecha = date.withYear(2019).withMonth(2).withDayOfMonth(21);


        Trayecto trayectoMiembro1ACoto = new Trayecto(coto, "De casa a Coto", 20, fecha);
        Trayecto trayectoMiembro1DesdeCoto = new Trayecto(coto, "De Coto a casa", 20, fecha);
        miembroCoto1.agregarTrayectos(trayectoMiembro1ACoto, trayectoMiembro1DesdeCoto);

        Trayecto trayectoMiembroCompartidoACoto = new Trayecto(coto, "De casa a Coto", 20, fecha);
        Trayecto trayectoMiembroCompartidoDeCotoAJumbo = new Trayecto(jumbo, "De Coto a Jumbo", 20, fecha);
        Trayecto trayectoMiembroCompartidoDesdeJumbo = new Trayecto(jumbo, "De Jumbo a casa", 20, fecha);
        miembroCompartido.agregarTrayectos(trayectoMiembroCompartidoACoto, trayectoMiembroCompartidoDeCotoAJumbo, trayectoMiembroCompartidoDesdeJumbo);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(trayectoMiembro1ACoto);
        EntityManagerHelper.getEntityManager().persist(trayectoMiembro1DesdeCoto);
        EntityManagerHelper.getEntityManager().persist(trayectoMiembroCompartidoACoto);
        EntityManagerHelper.getEntityManager().persist(trayectoMiembroCompartidoDeCotoAJumbo);
        EntityManagerHelper.getEntityManager().persist(trayectoMiembroCompartidoDesdeJumbo);
        EntityManagerHelper.commit();

        ////////////////////  Persistencia de Tramos  ////////////////////////////////
        //Tramos Miembro 1
        Tramo tramo1Miembro1 = new Tramo(trenRoca, parada1TrenRoca, parada3TrenRoca);
        trayectoMiembro1ACoto.agregarTramo(tramo1Miembro1);
        tramo1Miembro1.distancia();

        Tramo tramo2Miembro1 = new Tramo(trenRoca, parada3TrenRoca, parada1TrenRoca);
        trayectoMiembro1DesdeCoto.agregarTramo(tramo2Miembro1);
        tramo2Miembro1.distancia();

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(tramo1Miembro1);
        EntityManagerHelper.getEntityManager().persist(tramo2Miembro1);
        EntityManagerHelper.commit();

        //Tramos Miembro 2
        Tramo tramo1Miembro2 = new Tramo(uber, direccionCaniuelas, direccionCotoQuilmes); //casa a coto
        trayectoMiembroCompartidoACoto.agregarTramo(tramo1Miembro2);
        tramo1Miembro2.distancia();

        Tramo tramo2Miembro2 = new Tramo(bondi60, parada1Bondi60, parada3Bondi60); //coto a jumbo
        trayectoMiembroCompartidoDeCotoAJumbo.agregarTramo(tramo2Miembro2);
        tramo2Miembro2.distancia();

        Tramo tramo3Miembro2 = new Tramo(sinVehiculo, direccionJumboQuilmes, direccionCotoQuilmes); //jumbo -> coto -> casa
        Tramo tramo4Miembro2 = new Tramo(autoGnc, direccionCotoQuilmes, direccionCaniuelas);
        trayectoMiembroCompartidoDesdeJumbo.agregarTramo(tramo3Miembro2);
        trayectoMiembroCompartidoDesdeJumbo.agregarTramo(tramo4Miembro2);
        tramo3Miembro2.distancia();
        tramo4Miembro2.distancia();

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(tramo1Miembro2);
        EntityManagerHelper.getEntityManager().persist(tramo2Miembro2);
        EntityManagerHelper.getEntityManager().persist(tramo3Miembro2);
        EntityManagerHelper.getEntityManager().persist(tramo4Miembro2);
        EntityManagerHelper.commit();


        ////////////////////  Persistencia de Actividad  ////////////////////////////////
        Actividad actCombustionFija = new Actividad("Combustion Fija", Alcance.EMISIONES_DIRECTAS);
        Actividad actElectricidad = new Actividad("Electricidad adquirida y consumida", Alcance.EMISIONES_INDIRECTAS_ELECTRICIDAD);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(actCombustionFija);
        EntityManagerHelper.getEntityManager().persist(actElectricidad);
        EntityManagerHelper.commit();

        ////////////////////  Persistencia de TipoDeConsumo  ////////////////////////////////
        TipoDeConsumo tipoConsumoGasNatural = new TipoDeConsumo("Gas natural", "m3");
        TipoDeConsumo tipoConsumoCarbon = new TipoDeConsumo("Carbon", "kg");
        TipoDeConsumo tipoConsumoElectricidad = new TipoDeConsumo("Electricidad", "Kwh");

        actCombustionFija.agregarTiposDeConsumo(tipoConsumoGasNatural, tipoConsumoCarbon);
        actElectricidad.agregarTiposDeConsumo(tipoConsumoElectricidad);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(tipoConsumoGasNatural);
        EntityManagerHelper.getEntityManager().persist(tipoConsumoCarbon);
        EntityManagerHelper.getEntityManager().persist(tipoConsumoElectricidad);
        EntityManagerHelper.commit();

        ////////////////////  Persistencia de FactorDeEmision de actividades  ////////////////////////////////
        FactorDeEmision feCFGasNatural = new FactorDeEmision(300.2, tipoConsumoGasNatural);
        FactorDeEmision feCFCarbon = new FactorDeEmision(200.0, tipoConsumoCarbon);
        FactorDeEmision feEElectricidad = new FactorDeEmision(100.0, tipoConsumoElectricidad);

        BuscadorDeFactorDeEmision.instancia().agregarFactores(feCFGasNatural,feCFCarbon,feEElectricidad);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(feCFGasNatural);
        EntityManagerHelper.getEntityManager().persist(feCFCarbon);
        EntityManagerHelper.getEntityManager().persist(feEElectricidad);
        EntityManagerHelper.commit();

        ////////////////////  PeriodoDeImputacion  ////////////////////////////////
        PeriodoDeImputacion periodoAnual2022 = new PeriodoDeImputacion("anual", "2022");
        PeriodoDeImputacion periodoAnual2019 = new PeriodoDeImputacion("anual", "2019");
        PeriodoDeImputacion periodoMensualMayo2022 = new PeriodoDeImputacion("mensual", "05/2022");
        PeriodoDeImputacion periodoMensualMayo2019 = new PeriodoDeImputacion("mensual", "05/2019");
        PeriodoDeImputacion periodoMensualNoviembre2022 = new PeriodoDeImputacion("mensual", "11/2022");

        ////////////////////  Persistencia de DatoDeActividad  ////////////////////////////////
        DatoDeActividad datoAct2022CFgasCoto = new DatoDeActividad(actCombustionFija, tipoConsumoGasNatural, 5.0, periodoAnual2022);
        DatoDeActividad datoAct2019CFgasCoto = new DatoDeActividad(actCombustionFija, tipoConsumoGasNatural, 5.0, periodoAnual2019);
        DatoDeActividad datoActMayo2022CFcarbonCoto = new DatoDeActividad(actCombustionFija, tipoConsumoCarbon, 5.0, periodoMensualMayo2022);
        DatoDeActividad datoActMayo2019CFcarbonCoto = new DatoDeActividad(actCombustionFija, tipoConsumoCarbon, 5.0, periodoMensualMayo2019);
        DatoDeActividad datoActNoviembre2022CFelectricidadCoto = new DatoDeActividad(actElectricidad, tipoConsumoElectricidad, 5.0, periodoMensualNoviembre2022);
        DatoDeActividad datoAct2022CFgasDia = new DatoDeActividad(actCombustionFija, tipoConsumoGasNatural, 10.0, periodoAnual2022);
        DatoDeActividad datoActMayo2022CFcarbonDia = new DatoDeActividad(actCombustionFija, tipoConsumoCarbon, 10.0, periodoMensualMayo2022);

        List<DatoDeActividad> datosCoto = new ArrayList<>();
        Collections.addAll(datosCoto, datoAct2022CFgasCoto, datoActMayo2022CFcarbonCoto, datoActNoviembre2022CFelectricidadCoto);
        Collections.addAll(datosCoto, datoAct2019CFgasCoto, datoActMayo2019CFcarbonCoto, datoActNoviembre2022CFelectricidadCoto);
        coto.cargarMediciones(datosCoto);

        List<DatoDeActividad> datosDia = new ArrayList<>();
        Collections.addAll(datosDia, datoAct2022CFgasDia, datoActMayo2022CFcarbonDia);
        dia.cargarMediciones(datosDia);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(datoAct2022CFgasCoto);
        EntityManagerHelper.getEntityManager().persist(datoAct2019CFgasCoto);
        EntityManagerHelper.getEntityManager().persist(datoActMayo2022CFcarbonCoto);
        EntityManagerHelper.getEntityManager().persist(datoActMayo2019CFcarbonCoto);
        EntityManagerHelper.getEntityManager().persist(datoActNoviembre2022CFelectricidadCoto);
        EntityManagerHelper.getEntityManager().persist(datoAct2022CFgasDia);
        EntityManagerHelper.getEntityManager().persist(datoActMayo2022CFcarbonDia);
        EntityManagerHelper.commit();

///////////////////
        TipoDeConsumo kerosene = new TipoDeConsumo("Kerosene", "lt");
        //PeriodoDeImputacion periodo = new PeriodoDeImputacion("MENSUAL", "02/2020");
        Actividad combustionFija = new Actividad("Combustion fija", Alcance.EMISIONES_DIRECTAS);
        TipoDeConsumo gasNatural = new TipoDeConsumo("Gas Natural", "m3");
        TipoDeConsumo dieselGasoil = new TipoDeConsumo("Dieses/Gasoil", "lt");
        Actividad combustionMovil = new Actividad("Combustion movil", Alcance.EMISIONES_DIRECTAS);
        TipoDeConsumo gasoil = new TipoDeConsumo("Combustible consumido - Gasoil", "lts");
        TipoDeConsumo gnc = new TipoDeConsumo("Combustible consumido - GNC", "lts");
        TipoDeConsumo nafta = new TipoDeConsumo("Combustible consumido - Nafta", "lts");

        Actividad electricidadAdquirida = new Actividad("Electricidad adquirida y consumida", Alcance.EMISIONES_INDIRECTAS_ELECTRICIDAD);
        TipoDeConsumo electricidad = new TipoDeConsumo("Electricidad", "Kwh");

        combustionFija.agregarTiposDeConsumo(gasNatural,dieselGasoil,kerosene);
        combustionMovil.agregarTiposDeConsumo(gasoil,gnc,nafta);
        electricidadAdquirida.agregarTiposDeConsumo(electricidad);

        FactorDeEmision feKerosene = new FactorDeEmision(5600.0,kerosene);
        FactorDeEmision feGasNatural = new FactorDeEmision(5600.0,gasNatural);
        FactorDeEmision feDieselGasoil = new FactorDeEmision(5600.0,dieselGasoil);
        FactorDeEmision feGasoil = new FactorDeEmision(2300.0,gasoil);
        FactorDeEmision feGnc = new FactorDeEmision(2300.0,gnc);
        FactorDeEmision feNafta = new FactorDeEmision(2300.0,nafta);
        FactorDeEmision feElectricidad = new FactorDeEmision(1500.0,electricidad);
        BuscadorDeFactorDeEmision.instancia().agregarFactores(feElectricidad,feKerosene,feNafta);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(combustionFija);
        EntityManagerHelper.getEntityManager().persist(combustionMovil);
        EntityManagerHelper.getEntityManager().persist(electricidadAdquirida);

        EntityManagerHelper.getEntityManager().persist(kerosene);
        EntityManagerHelper.getEntityManager().persist(gasNatural);
        EntityManagerHelper.getEntityManager().persist(dieselGasoil);
        EntityManagerHelper.getEntityManager().persist(gasoil);
        EntityManagerHelper.getEntityManager().persist(gnc);
        EntityManagerHelper.getEntityManager().persist(nafta);
        EntityManagerHelper.getEntityManager().persist(electricidad);

        EntityManagerHelper.getEntityManager().persist(feElectricidad);
        EntityManagerHelper.getEntityManager().persist(feNafta);
        EntityManagerHelper.getEntityManager().persist(feKerosene);
        EntityManagerHelper.getEntityManager().persist(feGasNatural);
        EntityManagerHelper.getEntityManager().persist(feDieselGasoil);
        EntityManagerHelper.getEntityManager().persist(feGasoil);
        EntityManagerHelper.getEntityManager().persist(feGnc);

        EntityManagerHelper.commit();

        ////////////////////  Persistencia de Usuarios  ////////////////////////////////
        Usuario usuarioCoto = new Usuario("Coto", "orga", "coto@lala.com", "Segurisim4", RolEnum.ORGANIZACION);
        coto.setUsuario(usuarioCoto);
        Usuario usuarioJumbo = new Usuario("Jumbo", "Papu", "jumbo@lala.com", "Segurisim4", RolEnum.ORGANIZACION);
        jumbo.setUsuario(usuarioJumbo);
        Usuario usuarioDisco = new Usuario("Disco", "Rey", "disco@lala.com", "Segurisim4", RolEnum.ORGANIZACION);
        disco.setUsuario(usuarioDisco);

        Usuario usuarioMiembroCoto1 = new Usuario("Coto", "Uno", "coto1@lala.com", "Segurisim4", RolEnum.MIEMBRO_ORGANIZACION);
        miembroCoto1.setUsuario(usuarioMiembroCoto1);
        Usuario usuarioMiembroCoto2 = new Usuario("Coto", "Dos", "coto2@lala.com", "Segurisim4", RolEnum.MIEMBRO_ORGANIZACION);
        miembroCoto2.setUsuario(usuarioMiembroCoto2);
        Usuario usuarioMiembroCompartido = new Usuario("Compartido", "Uno", "comp1@lala.com", "Segurisim4", RolEnum.MIEMBRO_ORGANIZACION);
        miembroCompartido.setUsuario(usuarioMiembroCompartido);
        Usuario usuarioMiembroDisco1 = new Usuario("Disco", "Uno", "disco1@lala.com", "Segurisim4", RolEnum.MIEMBRO_ORGANIZACION);
        miembroDiscoUnico.setUsuario(usuarioMiembroDisco1);

        Usuario usuarioAgente1 = new Usuario("Buenos", "Aires", "bsas@lala.com", "Segurisim4", RolEnum.AGENTE_SECTORIAL);
        agenteSectorialBsAs.setUsuario(usuarioAgente1);
        Usuario usuarioAgente2 = new Usuario("Quimes", "BsAs", "quilmes@lala.com", "Segurisim4", RolEnum.AGENTE_SECTORIAL);
        agenteSectorialQuilmes.setUsuario(usuarioAgente2);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(usuarioCoto);
        EntityManagerHelper.getEntityManager().persist(usuarioJumbo);
        EntityManagerHelper.getEntityManager().persist(usuarioDisco);
        EntityManagerHelper.getEntityManager().persist(usuarioMiembroCoto1);
        EntityManagerHelper.getEntityManager().persist(usuarioMiembroCoto2);
        EntityManagerHelper.getEntityManager().persist(usuarioMiembroCompartido);
        EntityManagerHelper.getEntityManager().persist(usuarioMiembroDisco1);
        EntityManagerHelper.getEntityManager().persist(usuarioAgente1);
        EntityManagerHelper.getEntityManager().persist(usuarioAgente2);
        EntityManagerHelper.commit();


        ////////////////////  cierre entity manager  ////////////////////////////////
        EntityManagerHelper.closeEntityManager();
        EntityManagerHelper.closeEntityManagerFactory();
    }

 */
}
