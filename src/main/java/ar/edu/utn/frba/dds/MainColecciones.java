package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.repositories.RepositorioColecciones;

public class MainColecciones {
  public static void main(String[] args) {
    RepositorioColecciones repositorioColecciones = RepositorioColecciones.getInstance();
    repositorioColecciones.consesuareEchos();
  }
}
