package ar.edu.utn.frba.dds.dominio.filtrosagregador;

import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import java.util.List;

public class FiltroPorTipo implements FiltroAgregador {
  public final List<Class<?>> tiposPermitidos;

  public FiltroPorTipo(List<Class<?>> tiposPermitidos) {
    this.tiposPermitidos = tiposPermitidos;
  }

  @Override
  public boolean filtrar(Fuente fuente) {
    return tiposPermitidos.stream().anyMatch(t -> t.isInstance(fuente));
  }
}
