package ar.edu.utn.frba.dds.model.estadistica;

import java.util.ArrayList;
import java.util.List;

public class ComponenteEstadistico {

  public List<Estadistica> estadisticas = new ArrayList<>();

  public ComponenteEstadistico(List<Estadistica> estadisticas) {
    this.estadisticas = new ArrayList<>(estadisticas);
  }

  public void calcularEstadisticas() {
    estadisticas.forEach(Estadistica::calcularEstadistica);
  }

  public List<Estadistica> getEstadisticas() {
    return new ArrayList<>(estadisticas);
  }

  public void setEstadisticas(List<Estadistica> estadisticas) {
    this.estadisticas = estadisticas;
  }

}
