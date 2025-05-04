package ar.edu.utn.frba.dds.dominio;

import java.time.LocalDate;

public class FiltroUbicacion implements Filtro {
  private final Double latitud;
  private final Double longitud;

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getLatitud().equals(latitud) && hecho.getLongitud().equals(longitud);
  }

  public FiltroUbicacion(Double latitud, Double longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }
}
