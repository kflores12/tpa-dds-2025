package ar.edu.utn.frba.dds.dominio;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CriterioCategoria implements Criterio {

  String categoria;

  public CriterioCategoria(String categoria) {

    this.categoria = categoria.toLowerCase();
  }

  @Override
  public boolean aplicarFiltro(Hecho hecho) {
    String categoriaHecho = hecho.getCategoria().toLowerCase();
    return categoria.contains(categoriaHecho);
  }

  public String toQuery() {
    return "categoria=" + URLEncoder.encode(categoria, StandardCharsets.UTF_8);
  }

}
