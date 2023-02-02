package models.entities.notificaciones.email;

import models.entities.notificaciones.Notificacion;
import lombok.Setter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class AdapterMailJava implements AdapterNotificadorEmail {
    @Setter private String fromEmail;
    @Setter private String appPassword;
    private Session session;
    private LectorPropertiesEmail propertiesEmail = new LectorPropertiesEmail();

    public AdapterMailJava() {
        this.fromEmail = propertiesEmail.getPropFromEmail();
        this.appPassword = propertiesEmail.getPropAppPassword();
    }

    @Override
    public void enviarMailConGuia(Notificacion notificacion) throws MessagingException {
        configurarEmail();
        enviarMail(notificacion);
    }

    private void enviarMail(Notificacion notificacion) throws MessagingException {
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(fromEmail));

        String toEmail = notificacion.getEmail();
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

        message.setSubject(notificacion.getAsunto());
        message.setText(notificacion.getMensaje());

        System.out.println("enviando mail...");
        Transport.send(message);
        System.out.println("mail enviado exitosamente....");
    }

    private void configurarEmail() {
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });
    }

}
