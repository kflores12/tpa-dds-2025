package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioHechos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "fuentes_dinamicas")
public class FuenteDinamica extends Fuente {
  @OneToMany
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
