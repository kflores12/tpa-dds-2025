package ar.edu.utn.frba.dds.dominio;


import java.net.URL;
import java.util.Map;
import java.time.LocalDateTime;

public interface Conexion {

  Map<String, Object> siguienteHecho(URL url, LocalDateTime fechaUltimaConmsulta) throws Exception;

  ///  Excepción de que no se encontró la url +
  /// pensar como actualizar la fecha de consulta cuando se concrete correctamente la
  }
