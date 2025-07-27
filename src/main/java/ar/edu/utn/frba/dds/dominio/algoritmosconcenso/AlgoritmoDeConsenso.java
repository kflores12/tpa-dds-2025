package ar.edu.utn.frba.dds.dominio.algoritmosconcenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import java.util.List;

public interface AlgoritmoDeConsenso {

  boolean estaConsensuado(Hecho hecho, List<Hecho> hechosNodo);

}