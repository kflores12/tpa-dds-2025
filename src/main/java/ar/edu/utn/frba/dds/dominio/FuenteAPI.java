package ar.edu.utn.frba.dds.dominio;

import java.util.List;
import java.util.Collections;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.fatboyindustrial.gsonjavatime.Converters;
import retrofit2.Response;

public class FuenteAPI implements Fuente {
    private final ApiService apiService;

    public FuenteAPI(String baseUrl) {
        Gson gson = Converters.registerAll(new GsonBuilder()).create();
        
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
        
        this.apiService = retrofit.create(ApiService.class);
    }

    @Override
    public List<Hecho> importarHechos(List<Criterio> criterios) {
        try {
            Response<List<Hecho>> response = criterios.isEmpty() 
                ? apiService.getTodosLosHechos().execute()
                : apiService.getTodosLosHechos(CriterioConverter.toQuery(criterios)).execute();

            if (response.isSuccessful()) {
                System.out.println("Datos obtenidos exitosamente. Código: " + response.code());
                return response.body();
            } else {
                System.err.println("Error al obtener datos. Código: " + response.code());
                return Collections.emptyList();
            }
        } catch (IOException e) {
            System.err.println("Error de red al importar hechos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Hecho> importarHechosDeColeccion(List<Criterio> criterios, String handler) {
        try {
            Response<List<Hecho>> response = criterios.isEmpty() 
                ? apiService.getHechosDeUnaColeccion(handler).execute()
                : apiService.getHechosDeUnaColeccion(CriterioConverter.toQuery(criterios), handler).execute();

            if (response.isSuccessful()) {
                System.out.println("Datos obtenidos exitosamente. Código: " + response.code());
                return response.body();
            } else {
                System.err.println("Error al obtener datos. Código: " + response.code());
                return Collections.emptyList();
            }
        } catch (IOException e) {
            System.err.println("Error de red al importar hechos: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}