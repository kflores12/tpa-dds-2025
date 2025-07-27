package ar.edu.utn.frba.dds.dominio.criterios;

import ar.edu.utn.frba.dds.dominio.Hecho;

public class CriterioTitulo implements Criterio {

  String titulo;

  public CriterioTitulo(String titulo) {
    this.titulo = titulo.toLowerCase();
  }

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getTitulo().toLowerCase().contains(titulo);
  }

}
