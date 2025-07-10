package ar.edu.utn.frba.dds.dominio;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
  @GET("hechos")
  Call<List<Hecho>> getTodosLosHechos();

  @GET("colecciones/{identificador}/hechos")
  Call<List<Hecho>> getHechosDeUnaColeccion(@Path("identificador") String identificador);

  @POST("solicitudes")
  Call<Void> crearSolicitudDeEliminacion(@Body SolicitudDeEliminacion solicitud);
}
