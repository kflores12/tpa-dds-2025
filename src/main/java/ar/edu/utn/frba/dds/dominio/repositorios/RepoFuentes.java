package ar.edu.utn.frba.dds.dominio.repositorios;

import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class RepoFuentes implements WithSimplePersistenceUnit {
  public RepoFuentes() {
  }

  public void registrar(Fuente fuente) {
    entityManager().persist(fuente);
  }

  public List<Fuente> obtenerFuentes() {
    return entityManager()
        .createQuery("from Fuente", Fuente.class).getResultList();
  }
}
