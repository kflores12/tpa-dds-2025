package ar.edu.utn.frba.dds.dominio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class Adaptable {
  private static final HttpClient httpClient = HttpClient.newHttpClient();


  public Map<String, Object> retornarHecho(URL url, LocalDateTime fechaUltimaConsulta) throws Exception {

    URI uri = url.toURI();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(uri)
        .GET()
        .header("Accept", "application/json")
        .timeout(Duration.ofSeconds(10))
        .build();


    HttpResponse<String> response;
    try {
      response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (Exception e) {
      System.err.println("Error al realizar la petici\u00f3n HTTP: " + e.getMessage());
      throw e;
    }


    int status = response.statusCode();
    if (status == 304) {
      // 304 Not Modified: la fuente indica que no hay datos nuevos desde fechaUltimaConsulta (usando If-Modified-Since, por ejemplo).
      //System.out.println("No hay datos nuevos (respuesta 304 Not Modified)."); iimpementar como test
      return null;
    }
    if (status != 200) {
      throw new IllegalStateException("Error HTTP " + status + " al consultar la fuente externa.");
    }

    // formato JSON preguntar.
    String responseBody = response.body();


    //parseo del JSON
    Map<String, Object> hecho;
    try {
       ObjectMapper mapper = new ObjectMapper();
       hecho = mapper.readValue(responseBody, new TypeReference<Map<String,Object>>() {});
    } catch (Exception e) {
      throw e; //definir excepcion
    }

    return hecho;
  }
}


