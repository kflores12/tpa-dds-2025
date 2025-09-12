package ar.edu.utn.frba.dds.dominio.estadistica;

import java.util.*;
import java.util.stream.Collectors;
import ar.edu.utn.frba.dds.dominio.Hecho;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;

public class EstadisticaCategoriaMaxima implements Estadistica, WithSimplePersistenceUnit {
  private String categoriaMax;


  @Override public void calcularEstadistica() {
    List<Hecho> hechos = entityManager()
        .createQuery("from Hecho", Hecho.class).getResultList();

    this.categoriaMax = hechos.stream()
        .map(Hecho::getCategoria)
        .collect(Collectors.toMap(
            c -> c,
            c -> 1L,
            Long::sum
        ))
        .entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse(null);
  }

  @Override public void exportarEstadistica() {}

  public String getCategoriaMax() {
    return categoriaMax;
  }

}


