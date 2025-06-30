package ar.edu.utn.frba.dds.dominio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
