//AdapterConcreto del patrón Adapter
package models.entities.apiDistancia;

import models.entities.personas.Tramo;
import models.entities.transportes.IAdapterCalculadoraDistancia;
import models.entities.ubicaciones.ubicacion.Direccion;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class CalculadoraDistanciaRetrofitAdapter implements IAdapterCalculadoraDistancia {
    private static volatile CalculadoraDistanciaRetrofitAdapter instancia = null;
    private static final String urlApi = "https://ddstpa.com.ar/api/";
    private Retrofit retrofit;

    LectorTokenDeProperties properties = new LectorTokenDeProperties();
    String token = properties.getPropToken();

    private CalculadoraDistanciaRetrofitAdapter() {
        if (instancia != null) {
            throw new RuntimeException("Utilice el método instancia() para instaciar la clase");
        }
        else {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(urlApi)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    public static CalculadoraDistanciaRetrofitAdapter instancia() {
        if(instancia == null) {
            synchronized(CalculadoraDistanciaRetrofitAdapter.class){
                if(instancia == null) {
                    instancia = new CalculadoraDistanciaRetrofitAdapter();
                }
            }
        }
        return instancia;
    }

    public Double distancia(Tramo tramo) throws IOException {
        DistanciaService service = this.retrofit.create(DistanciaService.class);

        Direccion origen = (Direccion) tramo.getPuntoDePartida();
        Direccion destino = (Direccion) tramo.getPuntoDeLlegada();

        Call<Distancia> requestDistancia = service.distancia(
                origen.getDepartamentoAsInteger(), origen.getCalle(), origen.getAltura().toString(),
                destino.getDepartamentoAsInteger(), destino.getCalle(), destino.getAltura(),
                "Bearer "+ token
        );

        Response<Distancia> responseDistancia = requestDistancia.execute();

        if(responseDistancia.body() == null) { //es un error que sucede a veces. Como no es deterministico no lo testeamos.
            int statusCode = responseDistancia.code();
            throw new IOException("error en el GET a la API calculadora de distancia. Status code: " + statusCode);
        }

        return responseDistancia.body().valor();
    }

}
