package ar.edu.utn.frba.dds.dominio.fuentes;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ConexionMock implements Conexion {
  private int contador = 0;

  @Override
  public Map<String, Object> siguienteHecho(URL url, LocalDateTime fecha) {
    contador++;
    if (contador > 2) {
      return null;
    } // Simula fin de datos después de 2 hechos
    Map<String, Object> hecho = new HashMap<>();
    hecho.put("titulo", "Hecho Mockeado: " + contador);
    hecho.put("descripcion", "Descripción simulada para Cron");
    hecho.put("categoria", "test");
    hecho.put("latitud", -34.6037);
    hecho.put("longitud", -58.3816);
    hecho.put("fecha acontecimiento", java.time.LocalDate.now());
    hecho.put("fecha carga", java.time.LocalDate.now());
    hecho.put("multimedia", "http://mock.com/img" + contador + ".jpg");
        
    return hecho;
  }
}