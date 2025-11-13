package ar.edu.utn.frba.dds.model.estadistica;

import java.io.IOException;

public interface Estadistica {
  public void calcularEstadistica();

  public void exportarEstadistica(String path) throws IOException;

}


