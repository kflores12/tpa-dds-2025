package db;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaCategoriaMaxima;
import ar.edu.utn.frba.dds.model.entities.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.repositories.RepositorioHechos;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;

public class TestPrueba implements SimplePersistenceTest {
  @Test
  public void testEstadistica() {
    RepositorioHechos repositorio = new RepositorioHechos();
    Hecho hecho = new Hecho(
        "Corte de luz modificado",
        "Corte de luz en zona oeste",
        "cortes", 22.6, 29.3,
        LocalDateTime.of(2025, 1, 18,00,00),
        LocalDateTime.now(),
        TipoFuente.DINAMICA,
        "http://multimediavalue",
        Boolean.TRUE
    );

    Hecho hecho2 = new Hecho(
        "Corte de luz modificado",
        "Corte de luz en zona oeste",
        "cortes", 22.6, 29.3,
        LocalDateTime.of(2025, 1, 18,00,00),
        LocalDateTime.now(),
        TipoFuente.DINAMICA,
        "http://multimediavalue",
        Boolean.TRUE
    );

    Hecho hecho3 = new Hecho(
        "Corte de luz modificado",
        "Corte de luz en zona oeste",
        "incedio", 22.6, 29.3,
        LocalDateTime.of(2025, 1, 18,00,00),
        LocalDateTime.now(),
        TipoFuente.DINAMICA,
        "http://multimediavalue",
        Boolean.TRUE
    );
    repositorio.cargarHecho(hecho);
    repositorio.cargarHecho(hecho2);
    repositorio.cargarHecho(hecho3);

    EstadisticaCategoriaMaxima estadistica = new EstadisticaCategoriaMaxima ();
    estadistica.calcularEstadistica();

    Assertions.assertEquals("cortes", estadistica.getCategoriaMax());

  }
}
