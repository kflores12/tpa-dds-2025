package ar.edu.utn.frba.dds.model.entities.algoritmosconcenso;

import ar.edu.utn.frba.dds.model.entities.Coleccion;
import ar.edu.utn.frba.dds.model.entities.Hecho;
import java.util.List;

public class NavegacionIrrestricta implements ModoNavegacion {
  public List<Hecho> aplicar(Coleccion coleccion, AlgoritmoDeConsenso algoritmo) {
    return coleccion.obtnerHechos();
  }
}
