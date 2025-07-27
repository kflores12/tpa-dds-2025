package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioHechos;
import java.util.List;

public class FuenteDinamica implements Fuente {
  private RepositorioHechos repositorioDeHechos;

  public FuenteDinamica(RepositorioHechos repositorio) {
    this.repositorioDeHechos = repositorio;
  }

  @Override
  public List<Hecho> getHechos() {
    return repositorioDeHechos.obtenerTodos();
  }

}
