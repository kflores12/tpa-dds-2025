package ar.edu.utn.frba.dds.dominio.criterios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import java.time.LocalDate;

public class CriterioRangoFechas implements Criterio {
  private LocalDate desde;
  private LocalDate hasta;

  public CriterioRangoFechas(LocalDate desde, LocalDate hasta) {
    this.desde = desde;
    this.hasta = hasta;
  }

  @Override
  public boolean aplicarFiltro(Hecho hecho) {
    LocalDate fecha = hecho.getFechaAcontecimiento();
    return (fecha != null) && !fecha.isBefore(desde) && !fecha.isAfter(hasta);
  }

 
}
