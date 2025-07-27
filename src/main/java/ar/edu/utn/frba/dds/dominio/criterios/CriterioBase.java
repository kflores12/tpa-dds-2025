package ar.edu.utn.frba.dds.dominio.criterios;

import ar.edu.utn.frba.dds.dominio.Hecho;

public class CriterioBase implements Criterio {

  public CriterioBase() {
  }

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getDisponibilidad();
  }


}



