package ar.edu.utn.frba.dds.model.estadistica;

import java.util.ArrayList;
import java.util.List;

public class ComponenteEstadistico {
  private static ComponenteEstadistico INSTANCE;

  private List<Estadistica> estadisticas = new ArrayList<>();

  // 🔸 Constructor privado para control de singleton
  private ComponenteEstadistico(List<Estadistica> estadisticas) {
    this.estadisticas = new ArrayList<>(estadisticas);
  }

  /** 🔹 Inicializa la instancia global una sola vez */
  public static void inicializar(List<Estadistica> estadisticas) {
    if (INSTANCE == null) {
      INSTANCE = new ComponenteEstadistico(estadisticas);
    }
  }

  /** 🔹 Acceso global al componente */
  public static ComponenteEstadistico getInstance() {
    if (INSTANCE == null) {
      throw new IllegalStateException("ComponenteEstadistico no fue inicializado.");
    }
    return INSTANCE;
  }

  public void calcularEstadisticas() {
    estadisticas.forEach(Estadistica::calcularEstadistica);
  }

  public List<Estadistica> getEstadisticas() {
    return new ArrayList<>(estadisticas);
  }

  public <T extends Estadistica> T getEstadistica(Class<T> tipo) {
    return estadisticas.stream()
        .filter(tipo::isInstance)
        .map(tipo::cast)
        .findFirst()
        .orElse(null);
  }
}