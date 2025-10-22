package ar.edu.utn.frba.dds.model.entities.criterios;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DESC")
public class CriterioDescripcion extends Criterio {
  @Column
  String descripcion;

  public CriterioDescripcion() {
  }

  public CriterioDescripcion(String descripcion) {
    this.descripcion = descripcion.toLowerCase();
  }

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getDescripcion().toLowerCase().contains(descripcion);
  }


}
