package ar.edu.utn.frba.dds.dominio;

public class FiltroDescripcion implements Filtro {

  String descripcion;

  public FiltroDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getDescripcion().equals(descripcion);
  }

}
