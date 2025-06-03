package ar.edu.utn.frba.dds.dominio;


import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;




public class ConexionHttp implements Conexion {
  public  final Adaptable adaptable = new Adaptable();
  public LocalDateTime fechaUltimaDeConsulta;

  public ConexionHttp (LocalDateTime fechaUltimaDeConsulta){
    this.fechaUltimaDeConsulta = fechaUltimaDeConsulta;
  }

  // ConecionHttp(adaptable){ this.adaptable = adaptable} apirest/biblioteca
  @Override
  public Map<String, Object> siguienteHecho(URL url, LocalDateTime fechaUltimaConsulta) throws Exception{
    return adaptable.retornarHecho(url, fechaUltimaConsulta);

  }
}
