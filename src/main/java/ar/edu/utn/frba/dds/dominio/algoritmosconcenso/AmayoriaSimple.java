package ar.edu.utn.frba.dds.dominio.algoritmosconcenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


public class AmayoriaSimple implements AlgoritmoDeConsenso {
  @Override
  public boolean estaConsensuado(Hecho hecho, List<Hecho> hechosNodo) {

    Set<TipoFuente> fuentesDelNodo = hechosNodo.stream()
        .map(Hecho::getOrigen)
        .collect(Collectors.toSet());

    int totalFuentes = fuentesDelNodo.size();
    if (totalFuentes == 0) {
      return false;
    }

    Set<TipoFuente> fuentesQueContienenElHecho = hechosNodo.stream()
        .filter(h -> sonIguales(h, hecho))
        .map(Hecho::getOrigen)
        .collect(Collectors.toSet());


    return fuentesQueContienenElHecho.size() >= Math.ceil(totalFuentes / 2.0);
  }

  private boolean sonIguales(Hecho a, Hecho b) {
    if (a == b) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }

    return Objects.equals(a.getTitulo(), b.getTitulo())
        && Objects.equals(a.getDescripcion(), b.getDescripcion())
        && Objects.equals(a.getCategoria(), b.getCategoria())
        && Objects.equals(a.getLatitud(), b.getLatitud())
        && Objects.equals(a.getLongitud(), b.getLongitud())
        && Objects.equals(a.getFechaAcontecimiento(), b.getFechaAcontecimiento())
        && Objects.equals(a.getFechaDeCarga(), b.getFechaDeCarga())
        && Objects.equals(a.getMultimedia(), b.getMultimedia())
        && Objects.equals(a.getDisponibilidad(), b.getDisponibilidad());
  }
}


