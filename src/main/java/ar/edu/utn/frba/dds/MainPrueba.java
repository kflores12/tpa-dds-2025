package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.entities.BusquedaTextoLibre;
import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.model.entities.fuentes.FuenteDataSet;
import ar.edu.utn.frba.dds.repositories.RepositorioHechos;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;


public class MainPrueba {

  public static void main(String[] args) {

    RepositorioHechos repo = RepositorioHechos.getInstance();
    FuenteDataSet fuenteCsv = new FuenteDataSet("hechos.csv", "yyyy-MM-dd'T'HH:mm", ',');
    List<Hecho> hechos = fuenteCsv.getHechos();
    EntityManager em = repo.entityManager();
    EntityTransaction tx = em.getTransaction();

    try {
      tx.begin();
      em.createQuery("DELETE FROM Hecho").executeUpdate();
      for (Hecho h : hechos) {
        repo.cargarHecho(h);
      }
      tx.commit();

      // Inicializar Hibernate Search
      // y crear índices ya que al agregar datos por primera vez no existen los indices

      FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
      fullTextEntityManager.createIndexer().startAndWait();

      Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
      BusquedaTextoLibre buscador = new BusquedaTextoLibre(repo);

      while (true) {
        System.out.println("\nIngrese un texto para buscar o 'salir' para finalizar: ");
        String textoBusqueda = scanner.nextLine();

        if (textoBusqueda.equals("salir")) {
          break;
        }

        List<Hecho> resultados = buscador.realizarBusqueda(textoBusqueda);

        System.out.println("Resultados encontrados:");
        for (Hecho h : resultados) {
          System.out.println(h.getTitulo() + " | " + h.getDescripcion());
        }
      }
      scanner.close();

    } catch (Exception e) {
      if (tx.isActive()) {
        tx.rollback();
      }
      e.printStackTrace();
    } finally {
      em.close();
    }
  }
}
