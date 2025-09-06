package ar.edu.utn.frba.dds.dominio.criterios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("UBICACION")
public class CriterioUbicacion extends Criterio {
  @Column
  Double latitud;
  @Column
  Double longitud;

  public CriterioUbicacion() {
  }

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getLatitud().equals(latitud) && hecho.getLongitud().equals(longitud);
  }

  public CriterioUbicacion(Double latitud, Double longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }

 
}
