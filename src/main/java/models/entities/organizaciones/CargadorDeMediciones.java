package models.entities.organizaciones;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import models.entities.organizaciones.datoDeActividad.Actividad;
import models.entities.organizaciones.datoDeActividad.DatoDeActividad;
import org.apache.poi.ss.usermodel.*;


public class CargadorDeMediciones {
    private static volatile CargadorDeMediciones instancia = null;

    ConversorMedicionADatoActividad conversorADatoActividad = ConversorMedicionADatoActividad.instancia();

    private CargadorDeMediciones() {
        if (instancia != null) {
            throw new RuntimeException("Utilice el método instancia() para instaciar la clase");
        }
    }

    public static CargadorDeMediciones instancia() {
        if(instancia == null) {
            synchronized(CargadorDeMediciones.class){
                if(instancia == null) {
                    instancia = new CargadorDeMediciones();
                }
            }
        }
        return instancia;
    }

    public void agregarActividades(Actividad... actividades){
        this.conversorADatoActividad.agregarActividades(actividades);
    }

    public void cargarDatosActividadesEnOrganizacion(String path, Organizacion organizacion) throws IOException {
        List<DatoDeActividad> datosActividad = this.obtenerDatosActividades(path);
        organizacion.cargarMediciones(datosActividad);
        System.out.println("size: " + organizacion.getDatosDeActividad().size());
    }

    public List<DatoDeActividad> obtenerDatosActividades(String path) throws IOException {
        List<DatoDeActividad> datos = new ArrayList<>();

        File f = new File(path);
        InputStream inp = new FileInputStream(f);
        Workbook wb = WorkbookFactory.create(inp);
        Sheet sheet = wb.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();

        Integer iRow=1; //salteo la primera porque tiene titulos no importan
        Row row = sheet.getRow(iRow); //Cada fila es un datoDeActividad
        while(row!=null)
        {
            Cell cellActividad = row.getCell(0);
            String actividad = formatter.formatCellValue(cellActividad);

            Cell cellTipoDeConsumo = row.getCell(1);
            String tipoDeConsumo = formatter.formatCellValue(cellTipoDeConsumo);

            Cell cellValor = row.getCell(2);
            String valor = formatter.formatCellValue(cellValor);

            Cell cellPeriodicidad = row.getCell(3);
            String periodicidad = formatter.formatCellValue(cellPeriodicidad);

            Cell cellPeriodo = row.getCell(4);
            String periodo = formatter.formatCellValue(cellPeriodo);

            DatoDeActividad dato = this.conversorADatoActividad.convertirADatoActividad(actividad,tipoDeConsumo,valor,periodicidad,periodo);
            datos.add(dato);

            iRow++;
            row = sheet.getRow(iRow);

        }

        return datos;
    }

}
