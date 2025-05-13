package ar.edu.utn.frba.dds.dominio;

import java.time.LocalDate;

public class CriterioFechaCarga implements Criterio {
  private LocalDate fecha;

  @Override
  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getFechaDeCarga().equals(fecha);
  }

  public CriterioFechaCarga(LocalDate fecha) {
    this.fecha = fecha;
  }
}
