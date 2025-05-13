package ar.edu.utn.frba.dds.dominio;

public class CriterioTitulo implements Criterio {

  String titulo;

  public CriterioTitulo(String titulo) {
    this.titulo = titulo;
  }

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getTitulo().toLowerCase().contains(titulo.toLowerCase());
  }
}
