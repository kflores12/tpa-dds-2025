package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.entities.fuentes.FuenteDinamica;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class RepositorioFuentes implements WithSimplePersistenceUnit {
  static RepositorioFuentes INSTANCE = new RepositorioFuentes();

  public static RepositorioFuentes getInstance() {
    return INSTANCE;
  }

  public void registrarFuente(Fuente fuente) {
    entityManager().persist(fuente);
  }

  public void eliminarFuente(Fuente fuente) {
    Fuente fuente2 = entityManager().getReference(Fuente.class, fuente.getId());
    entityManager().remove(fuente2);
  }

  public List<Fuente> getFuentes() {
    return entityManager()
        .createQuery("from Fuente", Fuente.class).getResultList();
  }

  public Fuente getFuente(Long id) {
    return entityManager().find(Fuente.class, id);
  }

  public void actualizarHechos() {
    List<Fuente> fuentes = getFuentes();
    fuentes.forEach(Fuente::actualizarHechos);
  }

  private RepositorioFuentes() {
  }

  public List<FuenteDinamica> getFuentesDinamicas() {
    return entityManager()
        .createQuery("FROM FuenteDinamica", FuenteDinamica.class).getResultList();
  }
}