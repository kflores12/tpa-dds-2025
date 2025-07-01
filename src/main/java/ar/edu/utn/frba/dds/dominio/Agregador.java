package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

public class Agregador implements  Fuente {
  private final RepositorioFuentes repositorio;
  private final List<Class<?>> tiposFuente;
  private final List<Hecho> copiaLocal = new ArrayList<>();

  public Agregador(RepositorioFuentes repositorio, List<Class<?>> tiposFuente) {
    this.repositorio = requireNonNull(repositorio);
    this.tiposFuente =  tiposFuente;
  }


  @Override
  public List<Hecho> getHechos() {
    return new ArrayList<>(copiaLocal);
  }

  public void agregarHechos() {
    List<Fuente> fuentesSeleccionadas = repositorio.buscarFuentes(tiposFuente);

    List<Hecho> hechosDeFuentes = fuentesSeleccionadas.stream()
        .flatMap(f -> f.getHechos().stream())
        .toList();

    List<Hecho> hechosTotales = new ArrayList<>(copiaLocal);
    hechosTotales.addAll(hechosDeFuentes);
    copiaLocal.clear();
    copiaLocal.addAll(hechosTotales);
  }


}
