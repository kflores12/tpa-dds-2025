package ar.edu.utn.frba.dds.dominio.criterios;

import ar.edu.utn.frba.dds.dominio.Hecho;
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



