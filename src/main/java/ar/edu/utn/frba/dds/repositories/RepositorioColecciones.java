package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.entities.Coleccion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class RepositorioColecciones implements WithSimplePersistenceUnit  {
  static RepositorioColecciones INSTANCE = new RepositorioColecciones();

  public static RepositorioColecciones getInstance() {
    return INSTANCE;
  }

  public void cargarColeccion(Coleccion coleccion) {
    entityManager().persist(coleccion);
  }

  public void borrarColeccion(Coleccion coleccion) {
    Coleccion coleccion2 = entityManager().getReference(Coleccion.class, coleccion.getId());
    entityManager().remove(coleccion2);
  }

  public List<Coleccion> getColecciones() {
    return entityManager()
            .createQuery("from Coleccion", Coleccion.class).getResultList();
  }

  public Coleccion getColeccion(Coleccion coleccion) {
    Coleccion c = entityManager().getReference(Coleccion.class, coleccion.getId());
    return c;
  }

  public void consesuareEchos() {
    List<Coleccion> colecciones = getColecciones();
    colecciones.forEach(Coleccion::actualizarHechosConsensuados);
  }

  public Coleccion getColeccionById(Long id) {
    return entityManager().find(Coleccion.class, id);
  }

  public RepositorioColecciones() {
  }
}
