package ar.edu.utn.frba.dds.model.entities.algoritmosconcenso;

import ar.edu.utn.frba.dds.model.entities.Coleccion;
import ar.edu.utn.frba.dds.model.entities.Hecho;
import java.util.List;

public class NavegacionConsensuada implements ModoNavegacion {
  @Override
  public List<Hecho> aplicar(Coleccion coleccion, AlgoritmoDeConsenso algoritmo) {
    if (algoritmo == null) {
      return coleccion.obtnerHechos();
    }
    return coleccion.obtnerHechos().stream()
        .filter(h -> algoritmo.estaConsensuado(h, coleccion.getFuente())).toList();
  }
}
