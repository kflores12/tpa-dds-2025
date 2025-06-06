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
    private final String handler;

    public FuenteAPI(String baseUrl, String handler) {
        Gson gson = Converters.registerAll(new GsonBuilder()).create();
        
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
        
        this.apiService = retrofit.create(ApiService.class);
        this.handler = handler;
    }

    @Override
    public List<Hecho> importarHechos(List<Criterio> criterios) {
        try {
            Response<List<Hecho>> response;
            
            if (handler != null) {
                response = criterios.isEmpty() 
                    ? apiService.getHechosDeUnaColeccion(handler).execute()
                    : apiService.getHechosDeUnaColeccion(
                        CriterioConverter.toQuery(criterios), handler).execute();
            } else {
                response = criterios.isEmpty() 
                    ? apiService.getTodosLosHechos().execute()
                    : apiService.getTodosLosHechos(CriterioConverter.toQuery(criterios)).execute();
            }

            return response.isSuccessful() ? response.body() : Collections.emptyList();
        } catch (IOException e) {
            System.err.println("Error de red: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}