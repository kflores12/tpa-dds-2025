package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.repositorios.RepoHechos;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class PersistenciaHechosTest implements SimplePersistenceTest {
  RepoHechos repoHechos = new RepoHechos();
  @Test
  public void guardarHecho() {
    withTransaction(() -> {
      Hecho hecho = new Hecho();
      hecho.setTitulo("unHecho");
      hecho.setDescripcion("unHecho descripcion");
      repoHechos.registrar(hecho);
    });

    List<Hecho> hechos = repoHechos.obtenerHechos();

    //assertEquals(1, hechos.size());
    assertEquals("unHecho", hechos.get(0).getTitulo());
    assertEquals("unHecho descripcion", hechos.get(0).getDescripcion());
  }
}
