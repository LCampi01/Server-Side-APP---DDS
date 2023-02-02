package models.entities.apiDistancia;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface DistanciaService {
    @GET("distancia")
    Call<Distancia> distancia(
            @Query("localidadOrigenId") int localidadOrigenId,
            @Query("calleOrigen") String calleOrigen,
            @Query("alturaOrigen") String alturaOrigen,
            @Query("localidadDestinoId") int localidadDestinoId,
            @Query("calleDestino") String calleDestino,
            @Query("alturaDestino") int alturaDestino,
            @Header("Authorization") String token
    );
}

