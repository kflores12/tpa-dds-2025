package ar.edu.utn.frba.dds.dominio.filtrosagregador;

import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;

public interface FiltroAgregador {
  boolean filtrar(Fuente fuente);
}
