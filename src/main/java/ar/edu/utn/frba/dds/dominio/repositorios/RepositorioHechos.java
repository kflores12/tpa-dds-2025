package ar.edu.utn.frba.dds.dominio.repositorios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import java.util.List;

public class RepositorioHechos implements WithSimplePersistenceUnit {

  public RepositorioHechos() {
  }


  public void cargarHecho(Hecho hecho) {
    entityManager().persist(hecho);
  }

  public void borrarHecho(Hecho hecho) {
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

  public List<Hecho> buscarPorTextoLibre(String textoLibre) {
    SearchSession searchSession = Search.session(entityManager());

     return searchSession.search(Hecho.class)
        .where(f -> f.match()
            .fields("titulo", "descripcion")
            .matching(textoLibre)
            .fuzzy())
            .fetchAllHits(); 
  }
}
