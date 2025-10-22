package ar.edu.utn.frba.dds.model.entities;

import ar.edu.utn.frba.dds.repositories.RepositorioHechos;
import java.util.List;

public class BusquedaTextoLibre {

  private final RepositorioHechos hechoRepositorio;

  public BusquedaTextoLibre(RepositorioHechos hechoRepositorio) {
    this.hechoRepositorio = hechoRepositorio;
  }

  public List<Hecho> realizarBusqueda(String textoLibre) {
    if (textoLibre == null || textoLibre.trim().isEmpty()) {
      return List.of();
    }
    return hechoRepositorio.buscarPorTextoLibre(textoLibre.trim());
  }
}


