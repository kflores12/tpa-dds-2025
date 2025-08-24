package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FuenteApi implements Fuente {
  private final ApiService apiService;
  private String handler;

  public FuenteApi(String baseUrl, String handler) {
    Gson gson = Converters.registerAll(new GsonBuilder()).create();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();

    this.apiService = retrofit.create(ApiService.class);
    this.handler = handler;
  }

  @Override
  public List<Hecho> getHechos() { // Ya no recibe criterios
    try {
      Response<List<Hecho>> response;

      if (handler != null) {
        // Obtener todos los hechos de una colección específica
        response = apiService.getHechosDeUnaColeccion(handler).execute();
      } else {
        // Obtener todos los hechos sin filtro
        response = apiService.getTodosLosHechos().execute();
      }

      return response.isSuccessful() ? response.body() : Collections.emptyList();

    } catch (IOException e) {
      System.err.println("Error de red: " + e.getMessage());
      return Collections.emptyList();
    }
  }

  @Override
  public void actualizarHechos() {

  }

}