package ar.edu.utn.frba.dds.dominio.fuentes;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.filtrosagregador.FiltroAgregador;
import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioFuentes;
import java.util.ArrayList;
import java.util.List;


public class Agregador implements Fuente {
  private final RepositorioFuentes repositorio;
  private final FiltroAgregador filtro;
  private final List<Hecho> copiaLocal = new ArrayList<>();

  public Agregador(RepositorioFuentes repositorio, FiltroAgregador filtro) {
    this.repositorio = requireNonNull(repositorio);
    this.filtro = requireNonNull(filtro);
  }


  @Override
  public List<Hecho> getHechos() {
    return new ArrayList<>(copiaLocal);
  }

  public void agregarHechos() {
    List<Fuente> fuentesSeleccionadas = repositorio.buscarFuentes(filtro);

    List<Hecho> hechosDeFuentes = fuentesSeleccionadas.stream()
        .flatMap(f -> f.getHechos().stream())
        .toList();

    List<Hecho> hechosTotales = new ArrayList<>();
    hechosTotales.addAll(hechosDeFuentes);
    copiaLocal.clear();
    copiaLocal.addAll(hechosTotales);
  }


}
