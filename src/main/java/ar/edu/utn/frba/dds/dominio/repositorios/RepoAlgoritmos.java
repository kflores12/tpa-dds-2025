package ar.edu.utn.frba.dds.dominio.repositorios;

import ar.edu.utn.frba.dds.dominio.algoritmosconcenso.AlgoritmoDeConsenso;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;

public class RepoAlgoritmos implements WithSimplePersistenceUnit {
  public RepoAlgoritmos() {}

  public void registrar(AlgoritmoDeConsenso algoritmo) {
    entityManager().persist(algoritmo);
  }

  public List<AlgoritmoDeConsenso> obtenerAlgoritmos() {
    return entityManager()
        .createQuery("from AlgoritmoDeConsenso",  AlgoritmoDeConsenso.class)
        .getResultList();
  }
}
