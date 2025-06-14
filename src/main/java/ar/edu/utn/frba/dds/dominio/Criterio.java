package ar.edu.utn.frba.dds.dominio;

public interface Criterio {

  boolean aplicarFiltro(Hecho hecho);

  String toQuery();


}
