package ar.edu.utn.frba.dds.dominio;

import java.util.List;
import java.util.Objects;

public interface AlgoritmoDeConsenso {

  boolean estaConsensuado(Hecho hecho, List<Hecho> hechosNodo);

}