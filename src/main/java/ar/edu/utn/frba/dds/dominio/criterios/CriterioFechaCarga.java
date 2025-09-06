package ar.edu.utn.frba.dds.dominio.criterios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("FECHA_CARGA")
public class CriterioFechaCarga extends Criterio {
  @Column
  LocalDate fecha;

  public CriterioFechaCarga() {
  }

  @Override
  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getFechaDeCarga().equals(fecha);
  }

  public CriterioFechaCarga(LocalDate fecha) {
    this.fecha = fecha;
  }


}
