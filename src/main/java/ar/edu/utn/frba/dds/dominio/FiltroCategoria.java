package ar.edu.utn.frba.dds.dominio;

public class FiltroCategoria implements Filtro {

  Etiqueta categoria;

  public FiltroCategoria(Etiqueta categoria) {

    this.categoria = categoria;
  }

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getCategoria().equals(categoria);
  }

}
