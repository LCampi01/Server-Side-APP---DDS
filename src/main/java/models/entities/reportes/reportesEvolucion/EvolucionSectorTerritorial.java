package models.entities.reportes.reportesEvolucion;

import models.entities.huellaDeCarbono.HuellaDeCarbono;
import models.entities.sectores.Sector;

public class EvolucionSectorTerritorial extends Evolucion{
    private Sector sector;

    public EvolucionSectorTerritorial(Sector sector) {
        this.sector = sector;
    }

    public HuellaDeCarbono huellaDeCarbono(Integer anio) {
        return sector.huellaDeCarbonoAnual(String.valueOf(anio));
    }
}
