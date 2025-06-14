package ar.edu.utn.frba.dds.dominio;

import java.time.LocalDate;

public class CriterioFecha implements Criterio {
  LocalDate fecha;

  public boolean aplicarFiltro(Hecho hecho) {

    return hecho.getFechaAcontecimiento().equals(fecha);
  }

  public CriterioFecha(LocalDate fecha) {
    this.fecha = fecha;
  }

  public String toQuery() {
    return "fecha=" + fecha;
  }
}
