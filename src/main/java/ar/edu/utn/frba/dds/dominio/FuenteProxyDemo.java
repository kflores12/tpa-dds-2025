package ar.edu.utn.frba.dds.dominio;

import static ar.edu.utn.frba.dds.dominio.TipoFuente.FUENTEPROXYDEMO;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.CannotProceedException;

public class FuenteProxyDemo implements Fuente {
  private Conexion conexion;
  private RepositorioHechos repositorioDeHechos;
  private URL url;
  public LocalDateTime fechaUltimaConsulta;

  public FuenteProxyDemo(Conexion conexion, URL url, RepositorioHechos repositorio,
                         LocalDateTime fechaUltimaConsulta) {
    this.conexion = conexion;
    this.url = url;
    this.fechaUltimaConsulta = fechaUltimaConsulta;
    this.repositorioDeHechos = repositorio;
  }

  public List<Hecho> getHechos() {

    return repositorioDeHechos.obtenerTodos();
  }


  public void obtenerHechos() throws Exception {
    if (this.verificarHora()) {
      this.fechaUltimaConsulta = LocalDateTime.now(); //armar el setter
      Map<String, Object> mapConexion = conexion.siguienteHecho(url, LocalDateTime.now());
      while (mapConexion != null) {
        try {
          this.guardarHecho(mapConexion);
          mapConexion = conexion.siguienteHecho(url, LocalDateTime.now());
        } catch (Exception exn) {
          break;
        }
      }
    } else {
      throw new CannotProceedException("aún no pasó una hora de la última vez");
    }
  }

  public boolean verificarHora() {
    return  this.fechaUltimaConsulta.isBefore(LocalDateTime.now().minusHours(1));
  }

  public void guardarHecho(Map<String, Object> mapConexion) {
    Hecho hecho = new Hecho((String) mapConexion.get("titulo"), (String)
            mapConexion.get("descripcion").toString(), (String)
            mapConexion.get("categoria"), (Double)
            mapConexion.get("latitud"), (Double)
            mapConexion.get("longitud"), (LocalDate)
            mapConexion.get("fecha acontecimiento"), (LocalDate)
            mapConexion.get("fecha carga"), FUENTEPROXYDEMO, (String)
            mapConexion.get("multimedia"), true);
    repositorioDeHechos.cargarHecho(hecho);
  }
}


