package models.entities.reportes.ReportesHCTotal;

import models.entities.huellaDeCarbono.HuellaDeCarbono;
import models.entities.sectores.Sector;

import java.util.ArrayList;
import java.util.List;

public class HCTotalPorSectorTerritorial {

    public List<HCTotalSegunAlgo> reporteHCTotalAnual(List<Sector> sectoresTerritoriales, Integer anio) {
        List<HCTotalSegunAlgo> hcPorSectores = new ArrayList<>();
        for(Sector sector : sectoresTerritoriales) {
            HuellaDeCarbono hc = sector.huellaDeCarbonoAnual(String.valueOf(anio));

            HCTotalSegunAlgo hcDeEsteSector = new HCTotalSegunAlgo(sector.getNombre(), hc.getValor(), hc.getUnidadHuellaCarbono());
            hcPorSectores.add(hcDeEsteSector);
        }

        return hcPorSectores;
    }

    public List<HCTotalSegunAlgo> reporteHCTotalMensual(List<Sector> sectoresTerritoriales, Integer mes, Integer anio) {
        List<HCTotalSegunAlgo> hcPorSectores = new ArrayList<>();
        for(Sector sector : sectoresTerritoriales) {
            HuellaDeCarbono hc = sector.huellaDeCarbonoMensual(String.valueOf(mes), String.valueOf(anio));

            HCTotalSegunAlgo hcDeEsteSector = new HCTotalSegunAlgo(sector.getNombre(), hc.getValor(), hc.getUnidadHuellaCarbono());
            hcPorSectores.add(hcDeEsteSector);
        }

        return hcPorSectores;
    }
}
