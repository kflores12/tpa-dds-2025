package ar.edu.utn.frba.dds.dominio;

public class CriterioUbicacion implements Criterio {
  private final Double latitud;
  private final Double longitud;

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getLatitud().equals(latitud) && hecho.getLongitud().equals(longitud);
  }

  public CriterioUbicacion(Double latitud, Double longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }
}
