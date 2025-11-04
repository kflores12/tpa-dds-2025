package ar.edu.utn.frba.dds.model.entities.criterios;

import ar.edu.utn.frba.dds.model.entities.Hecho;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("FECHA")
public class CriterioFecha extends Criterio {
  @Column
  LocalDate fecha;

  public CriterioFecha() {
  }

  public boolean aplicarFiltro(Hecho hecho) {
    if (hecho.getFechaAcontecimiento() == null) {
      return false;
    }
    return hecho.getFechaAcontecimiento().toLocalDate().equals(fecha);
  }

  public CriterioFecha(LocalDate fecha) {
    this.fecha = fecha;
  }

  
}
