package ar.edu.utn.frba.dds.dominio.criterios;

import ar.edu.utn.frba.dds.dominio.Hecho;

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

 
}
