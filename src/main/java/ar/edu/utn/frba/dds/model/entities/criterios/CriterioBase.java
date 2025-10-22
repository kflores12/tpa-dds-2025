package ar.edu.utn.frba.dds.model.entities.criterios;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("BASE")
public class CriterioBase extends Criterio {

  public CriterioBase() {
  }

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getDisponibilidad();
  }


}



