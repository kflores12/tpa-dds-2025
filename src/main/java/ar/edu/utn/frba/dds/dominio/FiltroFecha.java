package ar.edu.utn.frba.dds.dominio;

import java.time.LocalDate;

public class FiltroFecha implements Filtro {
  LocalDate fecha;

  public boolean aplicarFiltro(Hecho hecho) {

    return hecho.getFechaAcontecimiento().equals(fecha);
  }

  public FiltroFecha(LocalDate fecha) {
    this.fecha = fecha;
  }
}
