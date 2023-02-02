package models.entities.notificaciones.whatsapp;

import java.io.InputStream;
import java.util.Properties;

public class LectorPropertiesWhatsapp {
    private String accountSID;
    private String authToken;
    private String fromNumber;

    Properties prop = new Properties();
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");

    public String getPropAccountSID() {
        try {
            prop.load(inputStream);

            accountSID = (String) prop.get("WHATSAPP_ACCOUNT_SID");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accountSID;
    }

    public String getPropAuthToken() {
        try {
            prop.load(inputStream);

            authToken = (String) prop.get("WHATSAPP_AUTH_TOKEN");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authToken;
    }

    public String getPropFromNumber() {
        try {
            prop.load(inputStream);

            fromNumber = (String) prop.get("WHATSAPP_FROM_NUMBER");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fromNumber;
    }
}
