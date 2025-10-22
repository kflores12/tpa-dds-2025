package ar.edu.utn.frba.dds.model.entities.fuentes;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.repositories.RepositorioHechos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
@DiscriminatorValue("FUENTE_DINAMICA")
public class FuenteDinamica extends Fuente {
  @ManyToMany
  @JoinTable(
      name = "dinamica_hechos",  // nombre de la tabla intermedia
      joinColumns = @JoinColumn(name = "dinamica_id"),
      inverseJoinColumns = @JoinColumn(name = "hecho_id") // FK hacia Hecho
  )
  private List<Hecho> listaDeHechos = new ArrayList<>();

  public FuenteDinamica() {
  }

  @Override
  public List<Hecho> getHechos() {
    return new ArrayList<>(listaDeHechos);
  }

  public void setListaDeHechos(List<Hecho> listaDeHechos) {
    this.listaDeHechos = new ArrayList<>(listaDeHechos);
  }

  public void actualiza(RepositorioHechos repositorio) {
    this.setListaDeHechos(repositorio.obtenerTodos());
  }

  @Override
  public void actualizarHechos() {

  }

}
