package models.repositorios;

import db.EntityManagerHelper;
import models.entities.transportes.*;
import models.entities.ubicaciones.territorio.Departamento;
import models.entities.ubicaciones.ubicacion.Parada;

import java.util.List;

public class RepositorioDeTransportes {

    public SinVehiculo buscarSinVehiculo() {
        return (SinVehiculo) EntityManagerHelper
                .createQuery("FROM " + MedioDeTransporte.class.getName() + " WHERE tipo_medio_de_transporte = 'sinVehiculo'")
                .getSingleResult();
    }

    public VehiculoParticular buscarVehiculoParticular(String tipoCombustible, String tipoVehiculo) {
        return (VehiculoParticular) EntityManagerHelper
                .createQuery("FROM " + VehiculoParticular.class.getName()
                        + " WHERE tipo_combustible = '" + tipoCombustible
                        + "' AND tipo_vehiculo = '" + tipoVehiculo + "'"
                ).getSingleResult();
    }

    public List<ServicioContratado> buscarTodosServiciosContratados() {
        return EntityManagerHelper
                .createQuery("FROM " + ServicioContratado.class.getName())
                .getResultList();
    }

    public ServicioContratado buscarServicioContratado(String medioInvolucrado) {
        return (ServicioContratado) EntityManagerHelper
                .createQuery("FROM " + ServicioContratado.class.getName() + " WHERE medioInvolucrado = '" + medioInvolucrado + "'")
                .getSingleResult();
    }

    public List<TipoTransportePublico> buscarTipoDeTransporte() {
        return EntityManagerHelper
                .createQuery("FROM " + TipoTransportePublico.class.getName())
                .getResultList();
    }

    public List<TransportePublico> buscarLineas(String tipoDeTransporte) {
        return EntityManagerHelper
                .createQuery("FROM " + TransportePublico.class.getName() + " WHERE tipo = '" + tipoDeTransporte + "'")
                .getResultList();
    }

    public TransportePublico buscarTransportePublico (String tipoTransportePublico, String linea) {
        return (TransportePublico) EntityManagerHelper
                .createQuery("FROM " + TransportePublico.class.getName()
                        + " WHERE tipo = '" + tipoTransportePublico
                        + "' AND lineaUtilizada = '" + linea + "'"
                ).getSingleResult();
    }
    public List<Parada> buscarParadas(int transporteId) {
        return EntityManagerHelper
                .createQuery("FROM " + Parada.class.getName() + " WHERE transporte_publico_id = '" + transporteId + "'")
                .getResultList();
    }
    public Parada buscarParada(int id) {
        return EntityManagerHelper
                .getEntityManager()
                .find(Parada.class, id);
    }

}
