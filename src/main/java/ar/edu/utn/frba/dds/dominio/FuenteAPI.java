package ar.edu.utn.frba.dds.dominio;

import java.util.List;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FuenteAPI //implements Fuente
{
//    private final ApiService apiService;
//
//    public FuenteAPI(String baseUrl) {
//        Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl(baseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
//
//        this.apiService = retrofit.create(ApiService.class);
//    }
//
//     @Override
//    public List<Hecho> importarHechos(List<Criterio> criterios) {
//        if (criterios.isEmpty()) {
//            return apiService.getTodosLosHechos().execute().body();
//        } else {
//            String query = criterioConverter.toQuery(criterios); // Delegamos la conversión
//            return apiService.getHechosDeUnaColeccion(query).execute().body();
//        }
//    }
}