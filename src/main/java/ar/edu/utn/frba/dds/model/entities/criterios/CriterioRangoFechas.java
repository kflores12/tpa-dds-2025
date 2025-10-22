package ar.edu.utn.frba.dds.model.entities.criterios;

import ar.edu.utn.frba.dds.model.entities.Hecho;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("RANGO")
public class CriterioRangoFechas extends Criterio {
  @Column
  private LocalDateTime desde;
  @Column
  private LocalDateTime hasta;

  public CriterioRangoFechas(LocalDateTime desde, LocalDateTime hasta) {
    this.desde = desde;
    this.hasta = hasta;
  }

  public CriterioRangoFechas() {
  }

  @Override
  public boolean aplicarFiltro(Hecho hecho) {
    LocalDateTime fecha = hecho.getFechaAcontecimiento();
    return (fecha != null) && !fecha.isBefore(desde) && !fecha.isAfter(hasta);
  }

 
}
