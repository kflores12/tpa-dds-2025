package ar.edu.utn.frba.dds.dominio;

import java.util.List;

public class FiltroPorTipo implements FiltroAgregador {
  private final List<Class<?>> tiposPermitidos;

  public FiltroPorTipo(List<Class<?>> tiposPermitidos) {
    this.tiposPermitidos = tiposPermitidos;
  }

  @Override
  public boolean cumple(Fuente fuente) {
    return tiposPermitidos.stream().anyMatch(t -> t.isInstance(fuente));
  }
}
