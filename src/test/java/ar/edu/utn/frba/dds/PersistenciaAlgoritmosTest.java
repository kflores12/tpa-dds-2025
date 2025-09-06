package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dominio.algoritmosconcenso.Aabsoluta;
import ar.edu.utn.frba.dds.dominio.algoritmosconcenso.AlgoritmoDeConsenso;
import ar.edu.utn.frba.dds.dominio.repositorios.RepoAlgoritmos;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.util.List;
import org.junit.Test;

public class PersistenciaAlgoritmosTest implements SimplePersistenceTest {
  RepoAlgoritmos repoAlgoritmos = new RepoAlgoritmos();
  @Test
  public void createAlgoritmoDeConsenso() {
    withTransaction(() -> {
      Aabsoluta aabsoluta = new Aabsoluta();
      repoAlgoritmos.registrar(aabsoluta);
    });

    List<AlgoritmoDeConsenso> algoritmo = repoAlgoritmos.obtenerAlgoritmos();
  }
}
