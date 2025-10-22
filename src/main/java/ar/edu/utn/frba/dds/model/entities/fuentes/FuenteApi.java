package ar.edu.utn.frba.dds.model.entities.fuentes;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Entity
@DiscriminatorValue("FUENTE_API")
public class FuenteApi extends Fuente {
  @Column
  private String handler;
  @Column
  private String baseUrl;

  public FuenteApi(String handler, String baseUrl) {
    this.handler = handler;
    this.baseUrl = requireNonNull(baseUrl);
  }

  public FuenteApi() {
  }

  private ApiService getApiService() {
    Gson gson = Converters.registerAll(new GsonBuilder()).create();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();

    ApiService api = retrofit.create(ApiService.class);
    return api;
  }

  @Override
  public List<Hecho> getHechos() {
    try {
      Response<List<Hecho>> response;

      if (handler != null) {
        // Obtener todos los hechos de una colección específica
        response = this.getApiService().getHechosDeUnaColeccion(handler).execute();
      } else {
        // Obtener todos los hechos sin filtro
        response = this.getApiService().getTodosLosHechos().execute();
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