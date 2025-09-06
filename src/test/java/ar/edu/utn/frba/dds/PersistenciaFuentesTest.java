package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteApi;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDataSet;
import ar.edu.utn.frba.dds.dominio.repositorios.RepoFuentes;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PersistenciaFuentesTest implements SimplePersistenceTest {
  RepoFuentes repoFuentes = new RepoFuentes();
  @Test
  public void guardarFuente() {

    withTransaction(() -> {
      FuenteApi fuenteApi = new FuenteApi();
      fuenteApi.setNombre("fuente api");
      fuenteApi.setApellido("fuente apellido");
      repoFuentes.registrar(fuenteApi);

      FuenteDataSet dataSet = new FuenteDataSet();
      dataSet.setRuta("ruta");
      repoFuentes.registrar(dataSet);
    });

    /*
    FuenteApi fuenteApi = new FuenteApi();
    fuenteApi.setNombre("fuente api");
    fuenteApi.setApellido("fuente apellido");
    repoFuentes.registrar(fuenteApi);

     */

    List<Fuente> apis = repoFuentes.obtenerFuentes();

  }
}
