package ar.edu.utn.frba.dds.dominio.fuentes;

import static ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente.FUENTEPROXYDEMO;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioHechos;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public class FuenteProxyDemo implements Fuente {
  private final Conexion conexion;
  private final RepositorioHechos repositorioDeHechos;
  private final URL url;


  public FuenteProxyDemo(Conexion conexion, URL url, RepositorioHechos repositorio) {
    this.conexion = conexion;
    this.url = url;
    this.repositorioDeHechos = repositorio;
  }

  @Override
  public List<Hecho> getHechos() {
    return repositorioDeHechos.obtenerTodos();
  }

  //Me parece que no tiene que lanzar la exception, queda cambiarlo
  public void obtenerHechos() throws Exception {
    Map<String, Object> mapConexion = conexion.siguienteHecho(url, LocalDateTime.now());

    while (mapConexion != null) {
      try {
        this.guardarHecho(mapConexion);
        mapConexion = conexion.siguienteHecho(url, LocalDateTime.now());
      } catch (Exception ex) {
        System.err.println("Ocurrio un error procesando un hecho: " + ex.getMessage());
        break;
      }
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
    repositorioDeHechos.cargarHecho(hecho);
  }
}