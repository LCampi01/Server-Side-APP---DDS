package models.entities.apiDistancia;

import java.io.InputStream;
import java.util.Properties;

public class LectorTokenDeProperties {
    String token;

    public String getPropToken() {
        try {
            Properties prop = new Properties();

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
            prop.load(inputStream);

            token = (String) prop.get("TOKEN");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return token;
    }
}
