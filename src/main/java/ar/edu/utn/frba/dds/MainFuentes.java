package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.repositories.RepositorioFuentes;

public class MainFuentes {
  public static void main(String[] args) {
    RepositorioFuentes repositorioFuentes = RepositorioFuentes.getInstance();
    repositorioFuentes.actualizarHechos();
  }
}


