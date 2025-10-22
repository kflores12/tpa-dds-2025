package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.entities.fuentes.Fuente;
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

  public Fuente getFuente(Fuente fuente) {
    Fuente f = entityManager().getReference(Fuente.class, fuente.getId());
    return f;
  }

  public void actualizarHechos() {
    List<Fuente> fuentes = getFuentes();
    fuentes.forEach(Fuente::actualizarHechos);
  }

  private RepositorioFuentes() {
  }

}