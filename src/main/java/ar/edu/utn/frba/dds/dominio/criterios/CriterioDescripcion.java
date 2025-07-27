package ar.edu.utn.frba.dds.dominio.criterios;

import ar.edu.utn.frba.dds.dominio.Hecho;

public class CriterioDescripcion implements Criterio {

  String descripcion;

  public CriterioDescripcion(String descripcion) {
    this.descripcion = descripcion.toLowerCase();
  }

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getDescripcion().toLowerCase().contains(descripcion);
  }


}
