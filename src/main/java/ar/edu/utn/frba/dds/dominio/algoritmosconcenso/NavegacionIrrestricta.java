package ar.edu.utn.frba.dds.dominio.algoritmosconcenso;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import java.util.List;

public class NavegacionIrrestricta implements ModoNavegacion {
  public List<Hecho> aplicar(Coleccion coleccion, AlgoritmoDeConsenso algoritmo) {
    return coleccion.obtnerHechos();
  }
}
