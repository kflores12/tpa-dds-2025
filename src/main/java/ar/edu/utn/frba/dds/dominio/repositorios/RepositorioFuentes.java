package ar.edu.utn.frba.dds.dominio.repositorios;

import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import java.util.ArrayList;
import java.util.List;

public class RepositorioFuentes {
  private final List<Fuente> fuentes = new ArrayList<>();

  public void registrarFuente(Fuente fuente) {
    fuentes.add(fuente);
  }

  public void eliminarFuente(Fuente fuente) {
    fuentes.remove(fuente);
  }

  public List<Fuente> getFuentes() {
    return new ArrayList<>(fuentes);
  }
}