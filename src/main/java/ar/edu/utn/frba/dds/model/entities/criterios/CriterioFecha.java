package ar.edu.utn.frba.dds.model.entities.criterios;

import ar.edu.utn.frba.dds.model.entities.Hecho;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("FECHA")
public class CriterioFecha extends Criterio {
  @Column
  LocalDateTime fecha;

  public CriterioFecha() {
  }

  public boolean aplicarFiltro(Hecho hecho) {

    return hecho.getFechaAcontecimiento().equals(fecha);
  }

  public CriterioFecha(LocalDateTime fecha) {
    this.fecha = fecha;
  }

  
}
