package ar.edu.utn.frba.dds.model.entities.fuentes;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
@DiscriminatorValue("FUENTE_AGREGADOR")
public class Agregador extends Fuente {
  @ManyToMany
  @JoinTable(
      name = "agregador_fuentes",
      joinColumns = @JoinColumn(name = "agregador_id"),
      inverseJoinColumns = @JoinColumn(name = "fuente_id")
  )
  private List<Fuente> fuentes;
  @ManyToMany
  @JoinTable(
      name = "agregador_hechos",
      joinColumns = @JoinColumn(name = "agregador_id"),
      inverseJoinColumns = @JoinColumn(name = "hecho_id")
  )
  private List<Hecho> copiaLocal = new ArrayList<>();

  public Agregador(List<Fuente> fuentes) {
    this.fuentes = new ArrayList<>(requireNonNull(fuentes));
  }

  public Agregador() {
  }

  public List<Fuente> getFuentes() {
    return new ArrayList<>(fuentes);
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
