package ar.edu.utn.frba.dds.dominio.criterios;

import ar.edu.utn.frba.dds.dominio.Hecho;

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



}
