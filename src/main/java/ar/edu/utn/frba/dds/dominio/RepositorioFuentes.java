package ar.edu.utn.frba.dds.dominio;

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

  public List<Fuente> buscarFuentes(List<Class<?>> tiposFuente) {
    return fuentes.stream()
        .filter(f -> tiposFuente.stream().anyMatch(t -> t.isInstance(f)))
        .toList();
  }


}