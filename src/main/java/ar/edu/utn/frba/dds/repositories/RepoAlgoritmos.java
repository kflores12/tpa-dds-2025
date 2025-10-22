package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.entities.algoritmosconcenso.AlgoritmoDeConsenso;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class RepoAlgoritmos implements WithSimplePersistenceUnit {
  static RepoAlgoritmos INSTANCE = new RepoAlgoritmos();

  public static RepoAlgoritmos getInstance() {
    return INSTANCE;
  }

  public RepoAlgoritmos() {

  }

  public void registrar(AlgoritmoDeConsenso algoritmo) {
    entityManager().persist(algoritmo);
  }

  public List<AlgoritmoDeConsenso> obtenerAlgoritmos() {
    return entityManager()
        .createQuery("from AlgoritmoDeConsenso",  AlgoritmoDeConsenso.class)
        .getResultList();
  }
}
