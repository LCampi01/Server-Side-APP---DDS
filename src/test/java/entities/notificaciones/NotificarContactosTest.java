package entities.notificaciones;

import models.entities.notificaciones.Notificador;
import models.entities.notificaciones.email.AdapterNotificadorEmail;
import models.entities.notificaciones.email.NotificadorEmail;
import models.entities.notificaciones.whatsapp.AdapterNotificadorWhatsApp;
import models.entities.notificaciones.whatsapp.NotificadorWhatsApp;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class NotificarContactosTest {

    @Test
    public void enviarNotificacionesAContactosTest() throws MessagingException {
        AdapterNotificadorEmail adapterEmailMock = mock(AdapterNotificadorEmail.class);
        NotificadorEmail notificadorEmail = new NotificadorEmail(adapterEmailMock);

        Provincia bsAs = new Provincia("Buenos Aires");
        Departamento lanus = new Departamento("Lanus",bsAs);

        Sector sectorProvinciaBsAs = new Sector("provincia Bs As", bsAs);
        Sector sectorDepartamentoLanus = new Sector("departamento Lanus de Bs As", lanus);

        BuscadorDeSectores.instancia().agregarSectores(sectorProvinciaBsAs, sectorDepartamentoLanus);

        AdapterNotificadorWhatsApp adapterWhatsappMock = mock(AdapterNotificadorWhatsApp.class);
        NotificadorWhatsApp notificadorWhatsApp = new NotificadorWhatsApp(adapterWhatsappMock);

        Notificador notificador = new Notificador("unLink.com");
        notificador.agregarMediosDeNotificacion(notificadorEmail, notificadorWhatsApp);

        Organizacion organizacion1 =
                new Organizacion(
                        "orga1", TipoDeOrganizacion.EMPRESA,
                        new ClasificacionDeOrganizacion("Supermercados"),
                        new Direccion(lanus,"San Martin",455)
                );

        Organizacion organizacion2 =
                new Organizacion(
                        "orga2", TipoDeOrganizacion.EMPRESA,
                        new ClasificacionDeOrganizacion("Supermercados"),
                        new Direccion(lanus,"San Martin",555)
                );

        Contacto contacto1 = new Contacto("santi1", "santiagot.orlando@gmail.com", "+5491163759878");
        Contacto contacto2 = new Contacto("santi2", "santiagot.orlando@gmail.com", "+5491163759878");
        Contacto contacto3 = new Contacto("santi3utn", "sorlando@frba.utn.edu.ar", "+5491163759878");

        List<Organizacion> organizacionesANotificar = new ArrayList<>();
        organizacion1.agregarContactos(contacto1);
        organizacion2.agregarContactos(contacto2, contacto3);
        Collections.addAll(organizacionesANotificar, organizacion1, organizacion2);

        notificador.enviarGuia(organizacionesANotificar);

        verify(adapterEmailMock, times(3)).enviarMailConGuia(any());
        verify(adapterWhatsappMock, times(3)).enviarWhatsappConGuia(any());
    }
}
