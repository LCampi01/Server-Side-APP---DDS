package models.entities.login;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ContraseniaNoComunStreamImporter implements ContraseniaNoComun.Importer {
    private InputStream is;
    private String charsetName;

    public ContraseniaNoComunStreamImporter(InputStream is, Charset charset) {
        this.is = is;
        this.charsetName = charset.name();
    }

    public List<String> fetchContraseniasComunes() {
        List<String> contraseniasComunes = new ArrayList();

        Scanner sc = new Scanner(is, charsetName);

        while (sc.hasNextLine()) {
            contraseniasComunes.add(sc.nextLine());
        }

        return contraseniasComunes;
    }
}
