package models.entities.sectores;

import models.entities.organizaciones.Organizacion;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuscadorDeSectores {
    @Getter private List<Sector> sectores = new ArrayList<>();
    public static BuscadorDeSectores instancia = null;

    public static BuscadorDeSectores instancia() {
        if(instancia == null) {
            instancia = new BuscadorDeSectores();
        }
        return instancia;
    }

    public void agregarSectores(Sector... sectores) {
        Collections.addAll(this.sectores, sectores);
    }

    public void asignarSectores(Organizacion organizacion){
        Sector sectorDepartamento = organizacion.getDireccion().getDepartamento().getSector();
        Sector sectorProvincia = organizacion.getDireccion().getDepartamento().getProvincia().getSector();

        sectorDepartamento.agregarOrganizaciones(organizacion);
        sectorProvincia.agregarOrganizaciones(organizacion);

        //si dejamos el atributo sectores en Organizacion, esto queda. sino vuela creo.
        List<Sector> sectores = new ArrayList<>();
        sectores.add(sectorProvincia);
        sectores.add(sectorDepartamento);
//        if(! sectores.isEmpty()){
          //  organizacion.agregarSectores(sectores);
//        }
    }

}
