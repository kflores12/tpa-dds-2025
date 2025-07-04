package ar.edu.utn.frba.dds.dominio;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;



public class Aabsoluta implements AlgoritmoDeConsenso {
  @Override
  public boolean estaConsensuado(Hecho hecho, List<Hecho> hechosNodo) {

    Set<TipoFuente> fuentesDelNodo = hechosNodo.stream()
        .map(Hecho::getOrigen)
        .collect(Collectors.toSet());

    return fuentesDelNodo.stream()
        .allMatch(fuente ->
            hechosNodo.stream()
                .filter(h -> h.getOrigen().equals(fuente))
                .anyMatch(h -> sonIguales(h, hecho))
        );
  }

  private boolean sonIguales(Hecho a, Hecho b) {

    if (a == b) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }

    return Objects.equals(a.getTitulo(), b.getTitulo())
        &&
        Objects.equals(a.getDescripcion(), b.getDescripcion())
        &&
        Objects.equals(a.getCategoria(), b.getCategoria())
        &&
        Objects.equals(a.getLatitud(), b.getLatitud())
        &&
        Objects.equals(a.getLongitud(), b.getLongitud())
        &&
        Objects.equals(a.getFechaAcontecimiento(), b.getFechaAcontecimiento())
        &&
        Objects.equals(a.getFechaDeCarga(), b.getFechaDeCarga())
        &&
        Objects.equals(a.getMultimedia(), b.getMultimedia())
        &&
        Objects.equals(a.getDisponibilidad(), b.getDisponibilidad());
  }
}

