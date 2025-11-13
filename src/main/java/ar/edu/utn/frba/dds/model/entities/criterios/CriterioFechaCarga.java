package ar.edu.utn.frba.dds.model.entities.criterios;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("FECHA_CARGA")
public class CriterioFechaCarga extends Criterio {
  @Column
  LocalDate fechaCarga;

  public CriterioFechaCarga() {
    this.fechaCarga = LocalDate.now();
  }

  public void setFechaCarga(LocalDate fechaCarga) {
    this.fechaCarga = fechaCarga;
  }

  @Override
  public boolean aplicarFiltro(Hecho hecho) {
    if (hecho.getFechaDeCarga() == null) {
      return false;
    }
    return hecho.getFechaDeCarga().toLocalDate().equals(fechaCarga);
  }

  public CriterioFechaCarga(LocalDate fecha) {
    this.fechaCarga = fecha;
  }


}
