package ar.edu.utn.frba.dds.dominio.filtrosagregador;

import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;

public class FiltroBaseAgregador implements FiltroAgregador {


  @Override
  public boolean filtrar(Fuente fuente) {
    return true;
  }
}
