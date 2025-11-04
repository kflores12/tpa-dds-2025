package ar.edu.utn.frba.dds.model.entities.criterios;

import ar.edu.utn.frba.dds.model.entities.Hecho;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("RANGO")
public class CriterioRangoFechas extends Criterio {
  @Column
  private LocalDate desde;
  @Column
  private LocalDate hasta;

  public CriterioRangoFechas(LocalDate desde, LocalDate hasta) {
    this.desde = desde;
    this.hasta = hasta;
  }

  public CriterioRangoFechas() {
  }

  @Override
  public boolean aplicarFiltro(Hecho hecho) {
    if (hecho.getFechaAcontecimiento() == null) {
      return false;
    }
    LocalDate fecha = hecho.getFechaAcontecimiento().toLocalDate();
    return !fecha.isBefore(desde) && !fecha.isAfter(hasta);
  }

 
}
