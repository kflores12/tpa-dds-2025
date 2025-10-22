package ar.edu.utn.frba.dds.model.entities.algoritmosconcenso;

import ar.edu.utn.frba.dds.model.entities.Coleccion;
import ar.edu.utn.frba.dds.model.entities.Hecho;
import java.util.List;

public interface ModoNavegacion {
  List<Hecho> aplicar(Coleccion coleccion, AlgoritmoDeConsenso algoritmo);
}
