package ar.edu.utn.frba.dds.dominio.algoritmosconcenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A_ABSOLUTAS")
public class Aabsoluta extends AlgoritmoDeConsenso {
  public Aabsoluta() {
  }

  @Override
  public boolean estaConsensuado(Hecho hecho, Fuente fuente) {
    List<Fuente> fuentes = descomponerFuente(fuente);

    // El hecho debe aparecer en todas las fuentes
    return fuentes.stream()
        .allMatch(f -> f.getHechos().stream().anyMatch(h -> h.esIgual(hecho)));
  }

}

