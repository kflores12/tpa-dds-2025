package ar.edu.utn.frba.dds.dominio.fuentes;

import static ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente.FUENTEPROXYDEMO;

import ar.edu.utn.frba.dds.dominio.Hecho;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FuenteProxyDemo implements Fuente {
  private final Conexion conexion;
  private final List<Hecho> listadoDeHechos;
  private final URL url;


  public FuenteProxyDemo(Conexion conexion, URL url, List<Hecho> listadoDeHechos) {
    this.conexion = conexion;
    this.url = url;
    this.listadoDeHechos = new ArrayList<>(listadoDeHechos);
  }

  @Override
  public List<Hecho> getHechos() {
    return new ArrayList<>(listadoDeHechos);
  }

  //Me parece que no tiene que lanzar la exception, queda cambiarlo
  public void obtenerHechos() throws Exception {
    Map<String, Object> mapConexion = conexion.siguienteHecho(url, LocalDateTime.now());

    while (mapConexion != null) {
      this.guardarHecho(mapConexion);
      mapConexion = conexion.siguienteHecho(url, LocalDateTime.now());
    }
  }

  private void guardarHecho(Map<String, Object> mapConexion) {
    Hecho hecho = new Hecho(
        (String) mapConexion.get("titulo"),
        mapConexion.get("descripcion").toString(),
        (String) mapConexion.get("categoria"),
        (Double) mapConexion.get("latitud"),
        (Double) mapConexion.get("longitud"),
        (LocalDate) mapConexion.get("fecha acontecimiento"),
        (LocalDate) mapConexion.get("fecha carga"),
        FUENTEPROXYDEMO,
        (String) mapConexion.get("multimedia"),
        true
    );
    listadoDeHechos.add(hecho);
  }

  public List<Hecho> getListadoDeHechos() {
    return new ArrayList<>(listadoDeHechos);
  }
}