package ar.edu.utn.frba.dds.dominio.repositorios;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.dominio.Hecho;
import java.util.ArrayList;
import java.util.List;

public class RepositorioHechos {

  private List<Hecho> baseDeHechos;

  public RepositorioHechos() {
    this.baseDeHechos = new ArrayList<>();
  }

  public void cargarHecho(Hecho hecho) {
    baseDeHechos.add(requireNonNull(hecho));
  }

  public void borrarHecho(Hecho hecho) {
    baseDeHechos.remove(hecho);
  }

  public List<Hecho> obtenerTodos() {
    return new ArrayList<>(baseDeHechos);
  }

  public Hecho buscarHecho(Hecho hecho) {
    return baseDeHechos.stream().anyMatch(h -> h.equals(hecho)) ? hecho : null;
  }

  public void limpiarBaseDeHechos() {
    baseDeHechos.clear();
  }
}
