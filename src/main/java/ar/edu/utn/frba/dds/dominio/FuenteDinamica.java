package ar.edu.utn.frba.dds.dominio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class FuenteDinamica implements Fuente {
  private RepositorioHechos repositorioDeHechos;

  public FuenteDinamica(RepositorioHechos repositorio) {
    this.repositorioDeHechos = repositorio;
  }

  @Override
  public List<Hecho> importarHechos(List<Criterio> criterios) {
    if (criterios == null || criterios.isEmpty()) {
      return new ArrayList<>(this.filtrarDuplicados(repositorioDeHechos.obtenerTodos()).values());
    }

    return repositorioDeHechos.obtenerTodos().stream().filter(h -> criterios.stream()
        .allMatch(c -> c.aplicarFiltro(h))).toList();

  }

  public Map<String, Hecho> filtrarDuplicados(List<Hecho> duplicados) { //EVALUAR DE JUNTAR ESTA LOGICA PARA NO REPETIR CODIGO
    Map<String, Hecho> hechosUnicos = new HashMap<>();
    for (Hecho hecho : duplicados) {
      hechosUnicos.put(hecho.getTitulo(), hecho);
    }
    return hechosUnicos;
  }



}
