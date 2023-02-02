package models.entities.notificaciones;

import models.entities.organizaciones.Organizacion;
import lombok.Setter;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Setter
public class NotificadorCalendarizado extends TimerTask { //podria ser Singleton
    private TimeUnit unidad;
    private Notificador notificador;
    private List<Organizacion> organizaciones;

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public NotificadorCalendarizado(Notificador notificador, List<Organizacion> organizaciones) {
        this.unidad = TimeUnit.DAYS;
        this.notificador = notificador;
        this.organizaciones = organizaciones;
    }

    public void enviarGuiaCadaCiertoTiempo(Integer periodo) {
        long periodoEnMilisegundos = this.unidad.toMillis(periodo);
        executor.scheduleAtFixedRate(this, 0, periodoEnMilisegundos, TimeUnit.MILLISECONDS);
    }

    public void cancelarTareaCalendarizada() {
        executor.shutdown();
    }

    public void cambiarPeriodoParaEnviarGuia(Integer periodo) throws MessagingException, InterruptedException {
        executor.shutdown();
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.enviarGuiaCadaCiertoTiempo(periodo);
    }

    @Override
    public void run() {
        System.out.println("Ejecutando tarea calendarizada. " + new Date());

        try {
            notificador.enviarGuia(this.organizaciones);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
