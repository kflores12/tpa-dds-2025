package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;


public class RepositorioHechos implements WithSimplePersistenceUnit {
  static RepositorioHechos instance = new RepositorioHechos();

  public static RepositorioHechos getInstance() {
    return instance;
  }

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

  public Hecho buscarHechoPorId(Long idHecho) {
    return entityManager().find(Hecho.class, idHecho);
  }

  public Hecho modificarHecho(Hecho hecho) {
    return entityManager().merge(hecho);
  }

  public List<Hecho> buscarPorTextoLibre(String textoLibre) {
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager());
        
    QueryBuilder queryBuilder = fullTextEntityManager
        .getSearchFactory()
        .buildQueryBuilder()
        .forEntity(Hecho.class)
        .get();
        
        
    Query luceneQuery = queryBuilder
        .keyword()
        .fuzzy()
        .onFields("titulo", "descripcion")
        .matching(textoLibre)
        .createQuery();
        
    return fullTextEntityManager.createFullTextQuery(luceneQuery, Hecho.class).getResultList();
  }

}
