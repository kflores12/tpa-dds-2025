package ar.edu.utn.frba.dds.dominio.algoritmosconcenso;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
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
