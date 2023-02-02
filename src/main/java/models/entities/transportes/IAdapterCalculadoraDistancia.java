//IAdapter del patrón Adapter
package models.entities.transportes;

import models.entities.personas.Tramo;

import java.io.IOException;

public interface IAdapterCalculadoraDistancia {
    
    public Double distancia(Tramo tramo) throws IOException;
}
