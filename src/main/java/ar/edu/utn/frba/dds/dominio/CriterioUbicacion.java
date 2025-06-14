package ar.edu.utn.frba.dds.dominio;

public class CriterioUbicacion implements Criterio {
  Double latitud;
  Double longitud;

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getLatitud().equals(latitud) && hecho.getLongitud().equals(longitud);
  }

  public CriterioUbicacion(Double latitud, Double longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }

  public String toQuery() {
    return "latitud=" + latitud + "&longitud=" + longitud;
  }
}
