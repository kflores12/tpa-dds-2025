package ar.edu.utn.frba.dds.dominio.algoritmosconcenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
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
  public boolean estaConsensuado(Hecho hecho, Fuente fuente) {
    List<Fuente> fuentes = descomponerFuente(fuente);

    // Fuentes que tienen un hecho exactamente igual
    long fuentesQueTienenHecho = fuentes.stream()
        .filter(f -> f.getHechos().stream().anyMatch(h -> h.esIgual(hecho)))
        .count();

    // Conflictos: otra fuente tiene un hecho con el mismo título pero atributos distintos
    boolean hayConflicto = fuentes.stream()
        .anyMatch(f -> f.getHechos().stream()
            .anyMatch(h -> mismoTitulo(h, hecho) && !h.esIgual(hecho)));

    return fuentesQueTienenHecho >= 2 && !hayConflicto;
  }


  private boolean mismoTitulo(Hecho a, Hecho b) {
    if (a == b) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    return Objects.equals(a.getTitulo(), b.getTitulo());
  }

}
