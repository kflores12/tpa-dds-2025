package ar.edu.utn.frba.dds.dominio;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Path;

public interface ApiService {
    @GET("hechos")
    Call<List<Hecho>> getTodosLosHechos();

    @GET("hechos")
    Call<List<Hecho>> getTodosLosHechos(@Query("filtros") String filtros);

    @GET("colecciones/{identificador}/hechos")
    Call<List<Hecho>> getHechosDeUnaColeccion(@Path("identificador") String identificador);

    @GET("colecciones/{identificador}/hechos")
    Call<List<Hecho>> getHechosDeUnaColeccion(@Query("filtros") String filtros,  @Path("identificador") String identificador);

    @POST("solicitudes")
    Call<Void> crearSolicitudDeEliminacion(@Body SolicitudDeEliminacion solicitud);


}