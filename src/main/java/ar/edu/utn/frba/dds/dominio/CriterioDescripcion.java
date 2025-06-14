package ar.edu.utn.frba.dds.dominio;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CriterioDescripcion implements Criterio {

  String descripcion;

  public CriterioDescripcion(String descripcion) {
    this.descripcion = descripcion.toLowerCase();
  }

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getDescripcion().toLowerCase().contains(descripcion);
  }

  public String toQuery() {
    return "descripcion=" + URLEncoder.encode(descripcion, StandardCharsets.UTF_8);
  }

}
