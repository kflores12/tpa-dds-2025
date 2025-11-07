package ar.edu.utn.frba.dds.model.estadistica;

import java.util.ArrayList;
import java.util.List;

public class ComponenteEstadistico {
  static ComponenteEstadistico INSTANCE; // = new ComponenteEstadistico();

  public List<Estadistica> estadisticas = new ArrayList<>();

  public static ComponenteEstadistico getInstance() {
    return INSTANCE;
  }

  public ComponenteEstadistico() {
    this.estadisticas = new ArrayList<>(estadisticas);
  }

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

  public void agregarEstadisticas(Estadistica estadisticas) {
    this.estadisticas.add(estadisticas);
  }

}
