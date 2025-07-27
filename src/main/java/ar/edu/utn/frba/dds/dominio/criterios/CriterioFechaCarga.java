package ar.edu.utn.frba.dds.dominio.criterios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import java.time.LocalDate;

public class CriterioFechaCarga implements Criterio {
  LocalDate fecha;

  @Override
  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getFechaDeCarga().equals(fecha);
  }

  public CriterioFechaCarga(LocalDate fecha) {
    this.fecha = fecha;
  }


}
