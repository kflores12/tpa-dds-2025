package ar.edu.utn.frba.dds.dominio.fuentes;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.dominio.Hecho;
import java.util.ArrayList;
import java.util.List;


public class Agregador implements Fuente {
  private final List<Fuente> fuentes;
  private final List<Hecho> copiaLocal = new ArrayList<>();

  public Agregador(List<Fuente>  fuentes) {
    requireNonNull(fuentes);
    this.fuentes = new ArrayList<>(fuentes);
  }


  @Override
  public List<Hecho> getHechos() {
    return new ArrayList<>(copiaLocal);
  }

  @Override
  public void actualizarHechos() {

    List<Hecho> hechosDeFuentes = fuentes.stream()
        .flatMap(f -> f.getHechos().stream())
        .toList();

    copiaLocal.clear();
    copiaLocal.addAll(hechosDeFuentes);
  }

  public void registrarFuente(Fuente fuente) {
    this.fuentes.add(fuente);
  }

  public void eliminarFuente(Fuente fuente) {
    this.fuentes.remove(fuente);
  }
}
