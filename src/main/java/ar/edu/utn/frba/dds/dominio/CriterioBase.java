package ar.edu.utn.frba.dds.dominio;

public class CriterioBase implements Criterio {

  public CriterioBase() {
  }

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getDisponibilidad();
  }

  public String toQuery() { 
    return "";
  }
}



