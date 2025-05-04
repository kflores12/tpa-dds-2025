package ar.edu.utn.frba.dds.dominio;

public class FiltroCategoria implements Filtro {

  Categoria categoria;

  public FiltroCategoria(Categoria categoria) {

    this.categoria = categoria;
  }

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getCategoria().equals(categoria);
  }

}
