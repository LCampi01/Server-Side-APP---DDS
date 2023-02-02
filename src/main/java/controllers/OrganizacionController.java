package controllers;

import com.google.gson.Gson;
import controllers.transformadosParaVistas.SectorConMiembrosTransformadoParaVista;
import controllers.transformadosParaVistas.SectorMiembrosPendientesTransformadoParaVista;
import helpers.UsuarioHelper;
import models.entities.huellaDeCarbono.HuellaDeCarbono;
import models.entities.organizaciones.CargadorDeMediciones;
import models.entities.organizaciones.ConversorMedicionADatoActividad;
import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.SectorOrganizacion;
import models.entities.organizaciones.datoDeActividad.Actividad;
import models.entities.organizaciones.datoDeActividad.DatoDeActividad;
import models.entities.personas.MiembroDeOrganizacion;
import models.entities.reportes.reportesComposicion.ComposicionHCOrganizacion;
import models.entities.reportes.reportesComposicion.PorcentajeHC;
import models.entities.reportes.reportesEvolucion.EvolucionOrganizacion;
import models.entities.reportes.reportesEvolucion.HCAnual;
import models.repositorios.RepositorioDeActividad;
import models.repositorios.RepositorioDeMiembroDeOrganizacion;
import models.repositorios.RepositorioDeOrganizacion;
import models.repositorios.RepositorioDeSectorOrganizacion;
import org.mockito.internal.matchers.Or;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.io.*;
import java.lang.Object;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class OrganizacionController {
    private RepositorioDeOrganizacion repositorioDeOrganizacion;
    private RepositorioDeSectorOrganizacion repositorioDeSectorOrganizacion;
    private RepositorioDeMiembroDeOrganizacion repositorioDeMiembroDeOrganizacion;
    private RepositorioDeActividad repositorioDeActividad;

    public OrganizacionController() {
        this.repositorioDeOrganizacion = new RepositorioDeOrganizacion();
        this.repositorioDeMiembroDeOrganizacion = new RepositorioDeMiembroDeOrganizacion();
        this.repositorioDeSectorOrganizacion = new RepositorioDeSectorOrganizacion();
        this.repositorioDeActividad = new RepositorioDeActividad();
    }

    public ModelAndView mostrarInicio(Request request, Response response) {
        return new ModelAndView(new HashMap<String, Object>(){{
            put("razonSocial", UsuarioHelper.obtenerOrganizacion(request).getRazonSocial());
        }}, "organizaciones/inicio.hbs");
    }

    /////////////////// huella /////////////////////
    public ModelAndView mostrarHuellaMensualVista(Request request, Response response) {
        return new ModelAndView(new HashMap<String, Object>(){{
            put("razonSocial", UsuarioHelper.obtenerOrganizacion(request).getRazonSocial());
        }}, "organizaciones/huella-mensual.hbs");
    }

    public ModelAndView mostrarHuellaAnualVista(Request request, Response response) {
        return new ModelAndView(new HashMap<String, Object>(){{
            put("razonSocial", UsuarioHelper.obtenerOrganizacion(request).getRazonSocial());
        }}, "organizaciones/huella-anual.hbs");
    }

    public String obtenerHuellaAnual(Request request, Response response) {
        Organizacion organizacion = UsuarioHelper.obtenerOrganizacion(request);
        HuellaDeCarbono huellaDeCarbono = organizacion.huellaDeCarbonoAnual(request.params("anio"));
        String json = "{\"message\": \"La huella de carbono anual es: <strong><font size='+2'>" + huellaDeCarbono.getValor() + "</font></strong> "+ huellaDeCarbono.getUnidadHuellaCarbono()+ "\"}";
        //Gson gson = new Gson();
        return json;
    }

    public String obtenerHuellaMensual(Request request, Response response) {
        Organizacion organizacion = UsuarioHelper.obtenerOrganizacion(request);
        HuellaDeCarbono huellaDeCarbono = organizacion.huellaDeCarbonoMensual(request.params("mes"),request.params("anio"));
        String json = "{\"message\": \"La huella de carbono mensual es: <strong><font size='+2'>" + huellaDeCarbono.getValor() + "</font></strong> "+ huellaDeCarbono.getUnidadHuellaCarbono()+ "\"}";
        return json;
    }

    /////////////////// reportes /////////////////////
    public ModelAndView mostrarReportes(Request request, Response response) {
        return new ModelAndView(new HashMap<String, Object>(){{
            put("razonSocial", UsuarioHelper.obtenerOrganizacion(request).getRazonSocial());
        }}, "organizaciones/reportes.hbs");
    }

    public ModelAndView mostrarReporteComposicionOrganizacionVista(Request request, Response response) {
        Organizacion organizacion = UsuarioHelper.obtenerOrganizacion(request);

        ComposicionHCOrganizacion generadorReporte = new ComposicionHCOrganizacion();
        List<PorcentajeHC> reporte;

        if(request.queryParams("mes") == null) {
            reporte = generadorReporte.reporteComposicionOrganizacionAnual(organizacion, Integer.parseInt(request.queryParams("anio")));
        } else {
            reporte = generadorReporte.reporteComposicionOrganizacionMensual(
                    organizacion,
                    Integer.parseInt(request.queryParams("mes")),
                    Integer.parseInt(request.queryParams("anio"))
            );
        }

        List<Double> porcentajes = new ArrayList<>();
        porcentajes.add(reporte.get(0).getPorcentaje());
        porcentajes.add(reporte.get(1).getPorcentaje());

        return new ModelAndView(new HashMap<String, Object>(){{
            put("razonSocial", organizacion.getRazonSocial());
            put("porcentajes", porcentajes);
        }}, "organizaciones/reporte_composicion_organizacion.hbs");
    }

    public ModelAndView mostrarReporteEvolucionOrganizacionVista(Request request, Response response) {
        Organizacion organizacion = UsuarioHelper.obtenerOrganizacion(request);

        EvolucionOrganizacion generadorReporte = new EvolucionOrganizacion(organizacion);
        List<HCAnual> reporte = generadorReporte.reporteEvolucion(Integer.parseInt(request.queryParams("anio")));

        List<Integer> anios = new ArrayList<>();
        List<Double> valores = new ArrayList<>();
        for (HCAnual hcAnual : reporte) {
            anios.add(hcAnual.getAnio());
            valores.add(hcAnual.getValorHC());
        }
        String unidad = reporte.get(0).getUnidad().toString();

        return new ModelAndView(new HashMap<String, Object>(){{
            put("razonSocial", organizacion.getRazonSocial());
            put("anios", anios);
            put("valores", valores);
            put("unidad", unidad);
        }}, "organizaciones/reporte_evolucion_organizacion.hbs");
    }

    //////////////// mi-organizacion /////////////////////////
    public ModelAndView mostrarSectoresConMiembros(Request request, Response response) {
        Organizacion organizacion = UsuarioHelper.obtenerOrganizacion(request);

        List<SectorConMiembrosTransformadoParaVista> sectoresConMiembros = new ArrayList<>();
        List<SectorOrganizacion> sectores = repositorioDeOrganizacion.buscarSectoresDeOrganizacion(organizacion.getId());
        for (SectorOrganizacion sector : sectores) {
            SectorConMiembrosTransformadoParaVista sectorConMiembros = new SectorConMiembrosTransformadoParaVista(sector.getId(), sector.getNombre());
            sectorConMiembros.agregarMiembros(this.repositorioDeSectorOrganizacion.buscarMiembrosDeSector(sector.getId()));
            sectoresConMiembros.add(sectorConMiembros);
        }

        return new ModelAndView(new HashMap<String, Object>(){{
            put("sectoresConMiembros", sectoresConMiembros);
            put("razonSocial", organizacion.getRazonSocial());
        }}, "organizaciones/mi-organizacion.hbs");
    }

    public Response eliminarMiembro(Request request, Response response) {
        this.repositorioDeSectorOrganizacion.eliminarMiembroDeSector(
                Integer.parseInt(request.params("idMiembro")), Integer.parseInt(request.params("idSector"))
        );
        return response;
    }

    /////////////////// miembros-pendientes /////////////////////
    public ModelAndView mostrarMiembrosPendientes(Request request, Response response) {
        List<SectorOrganizacion> sectores = this.repositorioDeOrganizacion.buscarSectoresDeOrganizacion(request.session().attribute("idOrganizacion"));
        List<SectorMiembrosPendientesTransformadoParaVista> sectoresMiembroPendiente = new ArrayList<>();
        for(SectorOrganizacion sector : sectores){
            for(MiembroDeOrganizacion miembro : sector.getMiembrosPendientes()) {
                SectorMiembrosPendientesTransformadoParaVista sectorMiembrosPendientesTransformadoParaVista = new SectorMiembrosPendientesTransformadoParaVista();
                sectorMiembrosPendientesTransformadoParaVista.setIdSector(sector.getId());
                sectorMiembrosPendientesTransformadoParaVista.setSector(sector.getNombre());
                sectorMiembrosPendientesTransformadoParaVista.setIdMiembro(miembro.getId());
                sectorMiembrosPendientesTransformadoParaVista.setMiembroPendiente(miembro.getNombre() + " " + miembro.getApellido());
                sectoresMiembroPendiente.add(sectorMiembrosPendientesTransformadoParaVista);
            }
        }

        return new ModelAndView(new HashMap<String, Object>(){{
            put("razonSocial", UsuarioHelper.obtenerOrganizacion(request).getRazonSocial());
            put("sectoresMiembroPendiente", sectoresMiembroPendiente);
        }}, "organizaciones/miembros-pendientes.hbs");
    }

    public Response aceptarMiembro(Request request, Response response) throws IOException {
        MiembroDeOrganizacion miembroDeOrganizacion = this.repositorioDeMiembroDeOrganizacion.buscar(Integer.parseInt(request.params("idMiembro")));
        SectorOrganizacion sectorOrganizacion = this.repositorioDeSectorOrganizacion.buscar(Integer.parseInt(request.params("idSector")));
        sectorOrganizacion.agregarMiembro(miembroDeOrganizacion);
        this.repositorioDeSectorOrganizacion.guardar(sectorOrganizacion);
        return response;
    }

    public Response rechazarMiembro(Request request, Response response) {
        repositorioDeSectorOrganizacion.eliminarMiembroPendienteDeSector(Integer.parseInt(request.params("idMiembro")), Integer.parseInt(request.params("idSector")));
        return response;
    }

    /////////////////// registrar-mediciones /////////////////////
    public ModelAndView mostrarRegistrarMedicionesVista(Request request, Response response) {
        return new ModelAndView(new HashMap<String, Object>(){{
            put("razonSocial", UsuarioHelper.obtenerOrganizacion(request).getRazonSocial());
        }}, "organizaciones/registrar-mediciones.hbs");
    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    public Response cargarMediciones(Request request, Response response){

        Organizacion organizacion = UsuarioHelper.obtenerOrganizacion(request);
        //le inyectamos las actividades
        CargadorDeMediciones lectorExcel = CargadorDeMediciones.instancia();
        ConversorMedicionADatoActividad conversor = ConversorMedicionADatoActividad.instancia();
        List<Actividad> actividades = this.repositorioDeActividad.buscarTodos();
        Actividad[] array = new Actividad[actividades.size()];
        actividades.toArray(array); // fill the array
        conversor.agregarActividades(array);

        String pathS = "src/main/resources/excelActividades/"+request.session().id() +".xlsx";
        File myFile = new File(pathS);
        try {
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
            request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
            Part file = request.raw().getPart("file"); //obtenemos del request el path del excel de alguna manera

            OutputStream out = new FileOutputStream(myFile);
            InputStream filecontent = file.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

        } catch (FileNotFoundException fne) {
            System.out.println(fne.getMessage());
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
//                lectorExcel.cargarDatosActividadesEnOrganizacion(pathS,organizacion);
                List<DatoDeActividad> datosActividadesNuevos = lectorExcel.obtenerDatosActividades(pathS);
                organizacion.cargarMediciones(datosActividadesNuevos);
                this.repositorioDeOrganizacion.guardarDatosDeActividad(organizacion, datosActividadesNuevos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

//        if (myFile.delete()) { //no lo borra xd
//            System.out.println("File deleted successfully");
//        }
//        else {
//            System.out.println("Failed to delete the file");
//        }
//        this.repositorioDeOrganizacion.guardarDatosDeActividad(organizacion);
        return response;
    }

    /////////////////// recomendaciones /////////////////////
    public ModelAndView mostrarRecomendaciones(Request request, Response response) {
        return new ModelAndView(new HashMap<String, Object>(){{
            put("razonSocial", UsuarioHelper.obtenerOrganizacion(request).getRazonSocial());
        }}, "organizaciones/recomendaciones.hbs");
    }

    public ModelAndView mostrarAyudaVista(Request request, Response response) {
        return new ModelAndView(new HashMap<String, Object>(){{
            put("razonSocial", UsuarioHelper.obtenerOrganizacion(request).getRazonSocial());
        }}, "organizaciones/ayuda.hbs");
    }
}
