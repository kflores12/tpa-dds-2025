package ar.edu.utn.frba.dds.dominio;


import javax.naming.CannotProceedException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ar.edu.utn.frba.dds.dominio.TipoFuente.FUENTEPROXYDEMO;

public class FuenteProxyDemo implements Fuente {
  private Conexion conexion;
  private RepositorioHechos repositorioDeHechos;
  private URL url;
  private LocalDateTime fechaUltimaConsulta;

  public FuenteProxyDemo(Conexion conexion, URL url,RepositorioHechos repositorio){
    this.conexion = conexion;
    this.url = url;
    this.fechaUltimaConsulta = LocalDateTime.now();
    this.repositorioDeHechos = repositorio;
  }

  public List<Hecho> importarHechos(List<Criterio> criterios) {
    if (criterios == null || criterios.isEmpty()) {
      return new ArrayList<>(this.filtrarDuplicados(repositorioDeHechos.obtenerTodos()).values());
    }
  }

  public Map<String, Hecho> filtrarDuplicados(List<Hecho> duplicados) { //EVALUAR DE JUNTAR ESTA LOGICA PARA NO REPETIR CODIGO
    Map<String, Hecho> hechosUnicos = new HashMap<>();
    for (Hecho hecho : duplicados) {
      hechosUnicos.put(hecho.getTitulo(), hecho);
    }
    return hechosUnicos;
  }
  public void obtenerHechos() throws Exception {
     if (this.verificarHora()){
       Map<String, Object> mapConexion = conexion.siguienteHecho(url,LocalDateTime.now());
       while (mapConexion == null) {
         this.guardarHecho(mapConexion);
         mapConexion = conexion.siguienteHecho(url,LocalDateTime.now());
       }
    }
     else throw new CannotProceedException("aún no pasó una hora de la última vez");
     }

  ///  Excepción de que no se encontró la url +
  /// pensar como actualizar la fecha de consulta cuando se concrete correctamente la
  /// no hay nadie del otro lado
  public Boolean verificarHora() {
    return this.fechaUltimaConsulta.isEqual(LocalDateTime.now()) || this.fechaUltimaConsulta.isAfter(LocalDateTime.now());
  }
  public void guardarHecho(Map<String, Object> mapConexion){
    Hecho hecho = new Hecho((String) mapConexion.get("titulo"),(String) mapConexion.get("descripcion").toString(),(String) mapConexion.get("categoria"),(Double) mapConexion.get("latitud"),(Double) mapConexion.get("longitud"),(LocalDate) mapConexion.get("fecha acontecimiento"), (LocalDate) mapConexion.get("fecha carga"),FUENTEPROXYDEMO,(String) mapConexion.get("multimedia"),true);
  }
}
/// public Hecho(String titulo, String descripcion, String categoria, Double latitud,
///                Double longitud, LocalDate fechaAcontecimiento, LocalDate fechaDeCarga,
///                TipoFuente origen, String multimedia, Boolean disponibilidad)

