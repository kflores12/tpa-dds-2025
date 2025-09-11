package ar.edu.utn.frba.dds.dominio.repositorios;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.ArrayList;
import java.util.List;

public class RepositorioHechos implements WithSimplePersistenceUnit {

  private List<Hecho> baseDeHechos;

  public RepositorioHechos() {
    this.baseDeHechos = new ArrayList<>();
  }

//////////////////////////////////// metodos de base de datos:

  public void cargarHecho(Hecho hecho){
    entityManager().persist(hecho);
  }

  public void borrarHecho(Hecho hecho){
    Hecho h = entityManager().getReference(Hecho.class, hecho.getId());
    entityManager().remove(h);
  }

  public List<Hecho> obtenerTodos() {
    return entityManager()
        .createQuery("from Hecho", Hecho.class).getResultList();
  }

  public Hecho buscarHecho(Hecho hecho) {
    Hecho h = entityManager().getReference(Hecho.class, hecho.getId());
    return h;
  }

  public Hecho modificarHecho(Hecho hecho) {
    return entityManager().merge(hecho);
  }

}