package ar.edu.utn.frba.dds.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class TestSolicituDeEliminacion {
  private Hecho hechoEjemplo;

  @BeforeEach
  void setUp() {
    hechoEjemplo = new Hecho(
        "Título ejemplo",
        "Descripción ejemplo",
        "Categoría ejemplo",
        -34.6037,  // latitud (Buenos Aires)
        -58.3816,  // longitud
        LocalDate.of(2023, 1, 1),
        LocalDate.now(),
        TipoFuente.DATASET,
        null,
        true
    );
  }

  @Test
  void seCreaCorrectamenteUnaSolicitudValida() {
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(
        hechoEjemplo,
        "Motivo válido",
        EstadoSolicitud.PENDIENTE
    );

    assertEquals("Motivo válido", solicitud.getMotivo());
    assertEquals(EstadoSolicitud.PENDIENTE, solicitud.getEstado());
  }

  @Test
  void lanzaExcepcionSiElMotivoEsMuyLargo() {
    String motivoLargo = "a".repeat(501);

    assertThrows(RuntimeException.class, () -> {
      new SolicitudDeEliminacion(hechoEjemplo, motivoLargo, EstadoSolicitud.PENDIENTE);
    });
  }

  @Test
  void puedeAceptarUnaSolicitudPendiente() {
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(
        hechoEjemplo,
        "Motivo válido",
        EstadoSolicitud.PENDIENTE
    );

    solicitud.cambiarEstado(EstadoSolicitud.ACEPTADA);

    assertEquals(EstadoSolicitud.ACEPTADA, solicitud.getEstado());
  }

  @Test
  void puedeRechazarUnaSolicitudPendiente() {
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(
        hechoEjemplo,
        "Motivo válido",
        EstadoSolicitud.PENDIENTE
    );

    solicitud.cambiarEstado(EstadoSolicitud.RECHAZADA);

    assertEquals(EstadoSolicitud.RECHAZADA, solicitud.getEstado());
    assertTrue(hechoEjemplo.getDisponibilidad()); // No se desactiva
  }

  @Test
  void noPuedeEvaluarseDosVeces() {
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(
        hechoEjemplo,
        "Motivo válido",
        EstadoSolicitud.PENDIENTE
    );

    solicitud.cambiarEstado(EstadoSolicitud.ACEPTADA);

    assertThrows(IllegalStateException.class, () -> {
      solicitud.cambiarEstado(EstadoSolicitud.RECHAZADA);
    });
  }
}
