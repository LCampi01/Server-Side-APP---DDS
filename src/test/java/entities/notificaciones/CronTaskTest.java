package entities.notificaciones;

import models.entities.notificaciones.MedioNotificacion;
import models.entities.notificaciones.Notificador;
import models.entities.notificaciones.NotificadorCalendarizado;
import models.entities.organizaciones.ClasificacionDeOrganizacion;
import models.entities.organizaciones.Contacto;
import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.TipoDeOrganizacion;
import models.entities.sectores.BuscadorDeSectores;
import models.entities.sectores.Sector;
import models.entities.ubicaciones.territorio.Departamento;
import models.entities.ubicaciones.ubicacion.Direccion;
import models.entities.ubicaciones.territorio.Provincia;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CronTaskTest {

    @Test
    public void givenUsingTimer_whenSchedulingTaskOnce_thenCorrect() throws InterruptedException, MessagingException {
        MedioNotificacion medioNotificacionMock = mock(MedioNotificacion.class);

        Provincia bsAs = new Provincia("Buenos Aires");
        Departamento quilmes = new Departamento("Quilmes",bsAs);
        Departamento lanus = new Departamento("Lanus",bsAs);

        Sector sector1 = new Sector("provincia Bs As", bsAs);
        Sector sector2 = new Sector("departamento Lanus de Bs As", lanus);
        BuscadorDeSectores.instancia().agregarSectores(sector1,sector2);

        Notificador notificador = new Notificador("unLink.com");
        notificador.agregarMediosDeNotificacion(medioNotificacionMock);

        Organizacion organizacion1 =
                new Organizacion(
                        "orga1", TipoDeOrganizacion.EMPRESA,
                        new ClasificacionDeOrganizacion("Supermercados"),
                        new Direccion(lanus,"San Martin",455)
                );

        Contacto contacto1 = new Contacto("santi1", "santiagot.orlando@gmail.com", "+5491163759878");

        List<Organizacion> organizacionesANotificar = new ArrayList<>();
        organizacion1.agregarContactos(contacto1);
        Collections.addAll(organizacionesANotificar, organizacion1);


        NotificadorCalendarizado notificadorCalendarizado = new NotificadorCalendarizado(notificador, organizacionesANotificar);
        notificadorCalendarizado.setUnidad(TimeUnit.SECONDS);
        notificadorCalendarizado.enviarGuiaCadaCiertoTiempo(1);

        Thread.sleep(2005);
        verify(medioNotificacionMock, times(3)).enviarGuia(any());

        notificadorCalendarizado.cambiarPeriodoParaEnviarGuia(2);

        Thread.sleep(4005);
        verify(medioNotificacionMock, times(3 + 3)).enviarGuia(any());

        notificadorCalendarizado.cancelarTareaCalendarizada();

        Thread.sleep(3000);
        verify(medioNotificacionMock, times(3 + 3)).enviarGuia(any());
    }
}
