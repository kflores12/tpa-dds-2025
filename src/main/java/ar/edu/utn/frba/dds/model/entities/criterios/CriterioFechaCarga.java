package ar.edu.utn.frba.dds.model.entities.criterios;

import ar.edu.utn.frba.dds.model.entities.Hecho;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("FECHA_CARGA")
public class CriterioFechaCarga extends Criterio {
  @Column
  LocalDateTime fecha;

  public CriterioFechaCarga() {
  }

  @Override
  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getFechaDeCarga().equals(fecha);
  }

  public CriterioFechaCarga(LocalDateTime fecha) {
    this.fecha = fecha;
  }


}
