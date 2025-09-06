package ar.edu.utn.frba.dds.dominio.fuentes;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.dominio.Hecho;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "agregadores")
public class Agregador extends Fuente {
  @ManyToMany
  private List<Fuente> fuentes;
  @ManyToMany
  private List<Hecho> copiaLocal = new ArrayList<>();

  public Agregador(List<Fuente>  fuentes) {
    requireNonNull(fuentes);
    this.fuentes = new ArrayList<>(fuentes);
  }

  public Agregador() {
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
