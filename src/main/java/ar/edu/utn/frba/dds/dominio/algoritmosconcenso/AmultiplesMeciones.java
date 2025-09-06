package ar.edu.utn.frba.dds.dominio.algoritmosconcenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import java.util.List;
import java.util.Objects;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A_MULTIPLES_MENCIONES")
public class AmultiplesMeciones extends AlgoritmoDeConsenso {

  public AmultiplesMeciones() {
  }

  @Override
  public boolean estaConsensuado(Hecho hecho, List<Hecho> hechosNodo) {
    List<Hecho> igualesDeOtrasFuentes = hechosNodo.stream()
        .filter(h -> sonIguales(h, hecho) && !mismaFuente(h, hecho))
        .toList();

    boolean hayConflictoDeTitulos = hechosNodo.stream()
        .anyMatch(h -> mismoTitulo(h, hecho) && !sonIguales(h, hecho) && !mismaFuente(h, hecho));

    return igualesDeOtrasFuentes.size() >= 1  && !hayConflictoDeTitulos;
  }

  private static boolean sonIguales(Hecho a, Hecho b) {
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

  private static boolean mismoTitulo(Hecho a, Hecho b) {
    if (a == b) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }

    return Objects.equals(a.getTitulo(), b.getTitulo()) && !sonIguales(a, b);
  }

  private boolean mismaFuente(Hecho a, Hecho b) {
    return Objects.equals(a.getOrigen(), b.getOrigen());
  }
}
