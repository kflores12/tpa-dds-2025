package ar.edu.utn.frba.dds.model.entities.fuentes;

import static ar.edu.utn.frba.dds.model.entities.fuentes.TipoFuente.FUENTEPROXYDEMO;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FuenteProxyDemo extends Fuente {
  private Conexion conexion;
  private List<Hecho> hechos;
  private String url;

  public FuenteProxyDemo(Conexion conexion, String url, List<Hecho> hechos) {
    this.conexion = conexion;
    this.url = url;
    this.hechos = new ArrayList<>(hechos);
  }

  @Override
  public List<Hecho> getHechos() {
    return new ArrayList<>(hechos);
  }

  @Override
  public void actualizarHechos() {
    try {
      URL urlObj = new URL(this.url);
      Map<String, Object> mapConexion = conexion.siguienteHecho(urlObj, LocalDateTime.now());

      while (mapConexion != null) {
        this.guardarHecho(mapConexion);
        mapConexion = conexion.siguienteHecho(urlObj, LocalDateTime.now());
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("URL inválida: " + this.url, e);
    }
  }

  private void guardarHecho(Map<String, Object> mapConexion) {
    Hecho hecho = new Hecho(
        (String) mapConexion.get("titulo"),
        mapConexion.get("descripcion").toString(),
        (String) mapConexion.get("categoria"),
        (Double) mapConexion.get("latitud"),
        (Double) mapConexion.get("longitud"),
        (LocalDateTime) mapConexion.get("fecha acontecimiento"),
        (LocalDateTime) mapConexion.get("fecha carga"),
        FUENTEPROXYDEMO,
        (String) mapConexion.get("multimedia"),
        true
    );
    hechos.add(hecho);
  }

}