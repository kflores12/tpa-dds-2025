package ar.edu.utn.frba.dds.dominio.criterios;

import ar.edu.utn.frba.dds.dominio.Hecho;
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

    return hecho.getFechaAcontecimiento().equals(fecha);
  }

  public CriterioFecha(LocalDate fecha) {
    this.fecha = fecha;
  }

  
}
