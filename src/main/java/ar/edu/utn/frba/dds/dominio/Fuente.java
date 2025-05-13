package ar.edu.utn.frba.dds.dominio;

import java.util.List;

public interface Fuente {
  List<Hecho> importarHechos(List<Criterio> criterios);
}
