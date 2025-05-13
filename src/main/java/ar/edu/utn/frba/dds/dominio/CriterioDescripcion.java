package ar.edu.utn.frba.dds.dominio;

public class CriterioDescripcion implements Criterio {

  String descripcion;

  public CriterioDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getDescripcion().toLowerCase().contains(descripcion.toLowerCase());
  }

}
