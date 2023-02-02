package controllers;

import helpers.UsuarioHelper;
import models.entities.organizaciones.Organizacion;
import models.entities.organizaciones.TipoDeOrganizacion;
import models.entities.personas.MiembroDeOrganizacion;
import models.entities.reportes.ReportesHCTotal.HCTotalPorClasificacionDeOrganizacion;
import models.entities.reportes.ReportesHCTotal.HCTotalPorSectorTerritorial;
import models.entities.reportes.ReportesHCTotal.HCTotalPorTipoDeOrganizacion;
import models.entities.reportes.ReportesHCTotal.HCTotalSegunAlgo;
import models.entities.reportes.reportesComposicion.ComposicionHCOrganizacion;
import models.entities.reportes.reportesComposicion.ComposicionHCPorPais;
import models.entities.reportes.reportesComposicion.ComposicionHCSectorTerritorial;
import models.entities.reportes.reportesComposicion.PorcentajeHC;
import models.entities.reportes.reportesEvolucion.EvolucionOrganizacion;
import models.entities.reportes.reportesEvolucion.EvolucionSectorTerritorial;
import models.entities.reportes.reportesEvolucion.HCAnual;
import models.entities.sectores.AgenteSectorial;
import models.entities.sectores.Sector;
import models.repositorios.RepositorioDeAgenteSectorial;
import models.repositorios.RepositorioDeOrganizacion;
import models.repositorios.RepositorioDeSectorTerritorial;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class AgenteSectorialController {
    private RepositorioDeAgenteSectorial repositorioDeAgenteSectorial;
    private RepositorioDeOrganizacion repositorioDeOrganizacion;
    private RepositorioDeSectorTerritorial repositorioDeSectorTerritorial;

    public AgenteSectorialController() {
        this.repositorioDeOrganizacion = new RepositorioDeOrganizacion();
        this.repositorioDeAgenteSectorial = new RepositorioDeAgenteSectorial();
        this.repositorioDeSectorTerritorial = new RepositorioDeSectorTerritorial();
    }

    public ModelAndView mostrarInicio(Request request, Response response) {
        AgenteSectorial agenteSectorial = UsuarioHelper.obtenerAgenteSectorial(request);
        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombreAgente", agenteSectorial.getNombre());
            put("nombreSector", agenteSectorial.getSector().getNombre());
        }}, "agentes-sectoriales/inicio.hbs");
    }

    public ModelAndView mostrarReportes(Request request, Response response) {
        AgenteSectorial agenteSectorial = UsuarioHelper.obtenerAgenteSectorial(request);
        List<Organizacion> organizaciones = this.repositorioDeAgenteSectorial.buscarOrganizaciones(agenteSectorial.getId());
        List<Sector> sectoresTerritoriales = this.repositorioDeSectorTerritorial.buscarTodos();
        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombreAgente", agenteSectorial.getNombre());
            put("organizaciones", organizaciones);
            put("sectoresTerritoriales", sectoresTerritoriales);
        }}, "agentes-sectoriales/reportes.hbs");
    }

    public ModelAndView mostrarRecomendaciones(Request request, Response response) {
        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombreAgente", UsuarioHelper.obtenerAgenteSectorial(request).getNombre());
        }}, "agentes-sectoriales/recomendaciones.hbs");
    }

    public ModelAndView mostrarAyudaVista(Request request, Response response) {
        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombreAgente", UsuarioHelper.obtenerAgenteSectorial(request).getNombre());
        }}, "agentes-sectoriales/ayuda.hbs");
    }

    public ModelAndView mostrarReporteEvolucionOrganizacionVista(Request request, Response response) {
        AgenteSectorial agenteSectorial = UsuarioHelper.obtenerAgenteSectorial(request);
        Organizacion organizacion = this.repositorioDeOrganizacion.buscar(Integer.parseInt(request.queryParams("organizacion")));

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
            put("nombreAgente", agenteSectorial.getNombre());
            put("razonSocial", organizacion.getRazonSocial());
            put("anios", anios);
            put("valores", valores);
            put("unidad", unidad);
        }}, "agentes-sectoriales/reporte_evolucion_organizacion.hbs");
    }

    public ModelAndView mostrarReporteEvolucionMiSector(Request request, Response response) {
        AgenteSectorial agenteSectorial = UsuarioHelper.obtenerAgenteSectorial(request);
        Sector sector = this.repositorioDeSectorTerritorial.buscar(agenteSectorial.getSector().getId());

        EvolucionSectorTerritorial generadorReporte = new EvolucionSectorTerritorial(sector);
        List<HCAnual> reporte = generadorReporte.reporteEvolucion(Integer.parseInt(request.queryParams("anio")));

        List<Integer> anios = new ArrayList<>();
        List<Double> valores = new ArrayList<>();
        for (HCAnual hcAnual : reporte) {
            anios.add(hcAnual.getAnio());
            valores.add(hcAnual.getValorHC());
        }
        String unidad = reporte.get(0).getUnidad().toString();

        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombreAgente", agenteSectorial.getNombre());
            put("razonSocial", sector.getNombre());
            put("anios", anios);
            put("valores", valores);
            put("unidad", unidad);
        }}, "agentes-sectoriales/reporte_evolucion_organizacion.hbs");
    }

    public ModelAndView mostrarReporteComposicionHuellaMiSector(Request request, Response response) {
        AgenteSectorial agenteSectorial = UsuarioHelper.obtenerAgenteSectorial(request);
        Sector sector = this.repositorioDeSectorTerritorial.buscar(agenteSectorial.getSector().getId());
        Integer anio = Integer.parseInt(request.queryParams("anio"));
        Integer mes = 0;
        if(request.queryParams("mes") != null) {
            mes = Integer.parseInt(request.queryParams("mes"));
        }

        ComposicionHCSectorTerritorial generadorReporte = new ComposicionHCSectorTerritorial();

        List<PorcentajeHC> reporte = mes != 0 ? generadorReporte.reporteComposicionSectorTerritorialMensual(sector, mes, anio) : generadorReporte.reporteComposicionSectorTerritorialAnual(sector, anio);

        List<String> criterio = new ArrayList<>();
        List<Double> valores = new ArrayList<>();

        for (PorcentajeHC hcAnual : reporte) {
            criterio.add(hcAnual.getNombre());
            valores.add(hcAnual.getPorcentaje());
        }
        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombreAgente", agenteSectorial.getNombre());
            put("criterio", criterio);
            put("valores", valores);
            put("unidad", "mi sector");
            put("titulo", "Porcentajes de composición huella de carbono en");
            put("tipo", "pie");
        }}, "agentes-sectoriales/reporte_segun_algo.hbs");
    }
    public ModelAndView mostrarReporteComposicionPais(Request request, Response response) {
        AgenteSectorial agenteSectorial = UsuarioHelper.obtenerAgenteSectorial(request);
        List<Sector> sectores = this.repositorioDeSectorTerritorial.buscarTodos();
        Integer anio = Integer.parseInt(request.queryParams("anio"));
        Integer mes = 0;
        if(request.queryParams("mes") != null) {
            mes = Integer.parseInt(request.queryParams("mes"));
        }

        ComposicionHCPorPais generadorReporte = new ComposicionHCPorPais();

        List<PorcentajeHC> reporte = mes != 0 ? generadorReporte.reporteComposicionPaisMensual(sectores, mes, anio) : generadorReporte.reporteComposicionPaisAnual(sectores, anio);

        List<String> criterio = new ArrayList<>();
        List<Double> valores = new ArrayList<>();

        for (PorcentajeHC hcAnual : reporte) {
            criterio.add(hcAnual.getNombre());
            valores.add(hcAnual.getPorcentaje());
        }
        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombreAgente", agenteSectorial.getNombre());
            put("criterio", criterio);
            put("valores", valores);
            put("unidad", "el pais");
            put("titulo", "Porcentajes de composición huella de carbono en");
            put("tipo", "pie");
        }}, "agentes-sectoriales/reporte_segun_algo.hbs");
    }

    public ModelAndView mostrarReporteComposicionOrganizacion(Request request, Response response) {
        AgenteSectorial agenteSectorial = UsuarioHelper.obtenerAgenteSectorial(request);
        Organizacion organizacion = this.repositorioDeOrganizacion.buscar(Integer.parseInt(request.queryParams("organizacion")));
        Integer anio = Integer.parseInt(request.queryParams("anio"));
        Integer mes = 0;
        if(request.queryParams("mes") != null) {
            mes = Integer.parseInt(request.queryParams("mes"));
        }

        ComposicionHCOrganizacion generadorReporte = new ComposicionHCOrganizacion();
        List<PorcentajeHC> reporte = mes != 0 ? generadorReporte.reporteComposicionOrganizacionMensual(organizacion, mes, anio) : generadorReporte.reporteComposicionOrganizacionAnual(organizacion, anio);


        List<String> criterio = new ArrayList<>();
        List<Double> valores = new ArrayList<>();

        for (PorcentajeHC hcAnual : reporte) {
            criterio.add(hcAnual.getNombre());
            valores.add(hcAnual.getPorcentaje());
        }

        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombreAgente", agenteSectorial.getNombre());
            put("criterio", criterio);
            put("valores", valores);
            put("unidad", organizacion.getRazonSocial());
            put("titulo", "Porcentajes de composición huella de carbono en");
            put("tipo", "pie");
        }}, "agentes-sectoriales/reporte_segun_algo.hbs");
    }

    public ModelAndView mostrarReporteHuellaTotalTerritorial(Request request, Response response) {
        AgenteSectorial agenteSectorial = UsuarioHelper.obtenerAgenteSectorial(request);
        List<Sector> sectores = this.repositorioDeSectorTerritorial.buscarTodos();
        Integer anio = Integer.parseInt(request.queryParams("anio"));
        Integer mes = 0;
        if(request.queryParams("mes") != null) {
            mes = Integer.parseInt(request.queryParams("mes"));
        }

        HCTotalPorSectorTerritorial hcTotal = new HCTotalPorSectorTerritorial();
        List<HCTotalSegunAlgo> reporte = mes != 0 ? hcTotal.reporteHCTotalMensual(sectores, mes, anio): hcTotal.reporteHCTotalAnual(sectores, anio);

        List<String> nombresSectores = new ArrayList<>();
        for (Sector s : sectores) nombresSectores.add(s.getNombre());

        List<Double> valores = new ArrayList<>();
        for (HCTotalSegunAlgo hcAnual : reporte) {
            valores.add(hcAnual.getValorHC());
        }
        String unidad = reporte.get(0).getUnidad().toString();

        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombreAgente", agenteSectorial.getNombre());
            put("criterio", nombresSectores);
            put("valores", valores);
            put("unidad", unidad);
            put("titulo", "Huella de carbono total en ");
            put("tipo", "bar");
        }}, "agentes-sectoriales/reporte_segun_algo.hbs");
    }

    public ModelAndView mostrarReporteHuellaTotalPorTipoOrganizacion(Request request, Response response) {
        AgenteSectorial agenteSectorial = UsuarioHelper.obtenerAgenteSectorial(request);
        List<Organizacion> organizaciones = this.repositorioDeOrganizacion.buscarTodos();
        List<TipoDeOrganizacion> tipos = new ArrayList<>();
        for (Organizacion organizacion : organizaciones) {
            tipos.add(organizacion.getTipoDeOrganizacion());
        }
        List<TipoDeOrganizacion> tiposSinDuplicados = new ArrayList<>(
                new HashSet<>(tipos));
        Integer anio = Integer.parseInt(request.queryParams("anio"));
        Integer mes = 0;
        if(request.queryParams("mes") != null) {
            mes = Integer.parseInt(request.queryParams("mes"));
        }

        HCTotalPorTipoDeOrganizacion hcTotal = new HCTotalPorTipoDeOrganizacion();
        List<HCTotalSegunAlgo> reporte = mes != 0 ? hcTotal.reporteHCTotalMensual(tiposSinDuplicados, organizaciones, mes, anio): hcTotal.reporteHCTotalAnual(tiposSinDuplicados, organizaciones, anio);

        List<String> nombresTipos = new ArrayList<>();
        for (TipoDeOrganizacion t : tiposSinDuplicados) nombresTipos.add(t.name());

        List<Double> valores = new ArrayList<>();
        for (HCTotalSegunAlgo hcAnual : reporte) {
            valores.add(hcAnual.getValorHC());
        }
        String unidad = reporte.get(0).getUnidad().toString();

        return new ModelAndView(new HashMap<String, Object>(){{
            put("nombreAgente", agenteSectorial.getNombre());
            put("criterio", nombresTipos);
            put("valores", valores);
            put("unidad", unidad);
            put("titulo", "Huella de carbono total en ");
            put("tipo", "bar");
        }}, "agentes-sectoriales/reporte_segun_algo.hbs");
    }

}
