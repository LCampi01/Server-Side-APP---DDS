package models.entities.reportes.reportesEvolucion;

import models.entities.huellaDeCarbono.HuellaDeCarbono;
import models.entities.organizaciones.Organizacion;

public class EvolucionOrganizacion extends Evolucion {
    private Organizacion organizacion;

    public EvolucionOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    public HuellaDeCarbono huellaDeCarbono(Integer anio) {
        return organizacion.huellaDeCarbonoAnual(String.valueOf(anio));
    }
}
