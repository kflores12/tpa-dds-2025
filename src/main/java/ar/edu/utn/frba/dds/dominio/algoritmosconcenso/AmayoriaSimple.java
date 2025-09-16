package ar.edu.utn.frba.dds.dominio.algoritmosconcenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A_MAYORIA_SIMPLE")
public class AmayoriaSimple extends AlgoritmoDeConsenso {
  public AmayoriaSimple() {
  }

  @Override
  public boolean estaConsensuado(Hecho hecho, Fuente fuente) {
    List<Fuente> fuentes = descomponerFuente(fuente);
    int total = fuentes.size();
    if (total == 0) {
      return false;
    }
    long conteo = fuentes.stream()
        .filter(f -> f.getHechos().stream().anyMatch(h -> h.esIgual(hecho)))
        .count();

    // Al menos la mitad de las fuentes deben tenerlo
    return conteo >= Math.ceil(total / 2.0);
  }

}


