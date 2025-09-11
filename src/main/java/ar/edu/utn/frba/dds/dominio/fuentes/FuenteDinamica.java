package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioHechos;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "fuentes_dinamicas")
public class FuenteDinamica extends Fuente {
  @Transient
  private RepositorioHechos repositorioDeHechos;


  public FuenteDinamica(RepositorioHechos repositorio) {
    this.repositorioDeHechos = repositorio;
  }

  @Override
  public List<Hecho> getHechos() {
    return repositorioDeHechos.obtenerTodos();
  }

  @Override
  public void actualizarHechos() {

  }

}
