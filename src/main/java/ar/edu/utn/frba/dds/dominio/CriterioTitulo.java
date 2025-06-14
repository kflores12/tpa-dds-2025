package ar.edu.utn.frba.dds.dominio;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CriterioTitulo implements Criterio {

  String titulo;

  public CriterioTitulo(String titulo) {
    this.titulo = titulo.toLowerCase();
  }

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getTitulo().toLowerCase().contains(titulo);
  }

  public String toQuery() {
    return "titulo=" + URLEncoder.encode(titulo, StandardCharsets.UTF_8);
  }
}
