package db;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.estadistica.EstadisticaCantidadSpam;
import ar.edu.utn.frba.dds.dominio.estadistica.EstadisticaCategoriaMaxima;
import ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioHechos;
import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioSolicitudesEliminacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.solicitudes.FactorySolicitudDeEliminacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudDeEliminacion;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContextTest implements SimplePersistenceTest {

  @Test
  void contextUp() {
    assertNotNull(entityManager());
  }

  @Test
  void contextUpWithTransaction() throws Exception {
    withTransaction(() -> {
    });
  }

  @Test
  public void testEstadisticaCategoriaMaxima() {
    RepositorioHechos repositorio = new RepositorioHechos();
    Hecho hecho = new Hecho(
        "Corte de luz modificado",
        "Corte de luz en zona oeste",
        "cortes", 22.6, 29.3,
        LocalDate.of(2025, 1, 18),
        LocalDate.now(),
        TipoFuente.DINAMICA,
        "http://multimediavalue",
        Boolean.TRUE
    );

    Hecho hecho2 = new Hecho(
        "Corte de luz modificado",
        "Corte de luz en zona oeste",
        "cortes", 22.6, 29.3,
        LocalDate.of(2025, 1, 18),
        LocalDate.now(),
        TipoFuente.DINAMICA,
        "http://multimediavalue",
        Boolean.TRUE
    );

    Hecho hecho3 = new Hecho(
        "Corte de luz modificado",
        "Corte de luz en zona oeste",
        "incedio", 22.6, 29.3,
        LocalDate.of(2025, 1, 18),
        LocalDate.now(),
        TipoFuente.DINAMICA,
        "http://multimediavalue",
        Boolean.TRUE
    );
    repositorio.cargarHecho(hecho);
    repositorio.cargarHecho(hecho2);
    repositorio.cargarHecho(hecho3);

    EstadisticaCategoriaMaxima estadisticaCM = new EstadisticaCategoriaMaxima ();
    estadisticaCM.calcularEstadistica();

    Assertions.assertEquals("cortes", estadisticaCM.getCategoriaMax());

  }

  @Test
  public void testEstadisticaCantidadSpam() {
    FactorySolicitudDeEliminacion factory;
    DetectorDeSpam inter = mock(DetectorDeSpam.class);
    when(inter.esSpam("Motivo válido")).thenReturn(false);
    when(inter.esSpam("Motivo invalido")).thenReturn(true);
    RepositorioSolicitudesEliminacion  repositorio = new RepositorioSolicitudesEliminacion();

    Hecho hecho = new Hecho(
        "Corte de luz modificado",
        "Corte de luz en zona oeste",
        "cortes", 22.6, 29.3,
        LocalDate.of(2025, 1, 18),
        LocalDate.now(),
        TipoFuente.DINAMICA,
        "http://multimediavalue",
        Boolean.TRUE
    );

    factory = new FactorySolicitudDeEliminacion(inter);

    SolicitudDeEliminacion solicitud1 = factory.crear(hecho, "Motivo invalido");
    repositorio.cargarSolicitudEliminacion(solicitud1);


    EstadisticaCantidadSpam estadisticaCS = new EstadisticaCantidadSpam();
    estadisticaCS.calcularEstadistica();

    Assertions.assertEquals(1, estadisticaCS.getCantidadSpam());

  }
}