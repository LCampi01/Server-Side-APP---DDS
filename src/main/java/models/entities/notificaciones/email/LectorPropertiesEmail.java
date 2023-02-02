package models.entities.notificaciones.email;

import java.io.InputStream;
import java.util.Properties;

public class LectorPropertiesEmail {
    private String fromEmail;
    private String appPassword;

    Properties prop = new Properties();
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");

    public String getPropFromEmail() {
        try {
            prop.load(inputStream);

            fromEmail = (String) prop.get("FROM_EMAIL");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fromEmail;
    }

    public String getPropAppPassword() {
        try {
            prop.load(inputStream);

            appPassword = (String) prop.get("APP_PASSWORD");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return appPassword;
    }
}
