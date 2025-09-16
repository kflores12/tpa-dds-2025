package ar.edu.utn.frba.dds.dominio.algoritmosconcenso;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import java.util.List;

public interface ModoNavegacion {
  List<Hecho> aplicar(Coleccion coleccion, AlgoritmoDeConsenso algoritmo);
}
