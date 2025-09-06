package ar.edu.utn.frba.dds.dominio.repositorios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class RepoHechos implements WithSimplePersistenceUnit {

  public RepoHechos() {}

  public void registrar(Hecho hecho) {
    entityManager().persist(hecho);
  }

  public List<Hecho> obtenerHechos() {
    return entityManager().createQuery("from Hecho ", Hecho.class).getResultList();
  }
}
