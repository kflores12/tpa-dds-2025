package ar.edu.utn.frba.dds.model;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.model.entities.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.model.entities.solicitudes.DetectorDeSpam;
import ar.edu.utn.frba.dds.model.entities.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.model.entities.solicitudes.FactorySolicitudDeEliminacion;
import ar.edu.utn.frba.dds.model.entities.solicitudes.SolicitudDeEliminacion;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestSolicituDeEliminacion {
  private Hecho hechoEjemplo;
  FactorySolicitudDeEliminacion factory;
  DetectorDeSpam inter = mock(DetectorDeSpam.class);

  @BeforeEach
  void setUp() {
    hechoEjemplo = new Hecho(
        "Título ejemplo",
        "Descripción ejemplo",
        "Categoría ejemplo",
        -34.6037,  // latitud (Buenos Aires)
        -58.3816,  // longitud
        LocalDateTime.of(2023, 1, 1,00,00),
        LocalDateTime.now(),
        TipoFuente.DATASET,
        null,
        true
    );
    factory = new FactorySolicitudDeEliminacion(inter);
  }

  @Test
  void seCreaCorrectamenteUnaSolicitudValida() {

    when(inter.esSpam("Motivo válido")).thenReturn(false);
    SolicitudDeEliminacion solicitud = factory.crear(hechoEjemplo,
        "Motivo válido");

    assertEquals("Motivo válido", solicitud.getMotivo());
    assertEquals(EstadoSolicitud.PENDIENTE, solicitud.getEstado());
  }

  @Test
  void lanzaExcepcionSiElMotivoEsMuyLargo() {
    when(inter.esSpam("")).thenReturn(true);

    String motivoLargo = "a".repeat(501);

    assertThrows(RuntimeException.class, () -> {
      factory.crear(hechoEjemplo, motivoLargo);
    });
  }

  @Test
  void puedeAceptarUnaSolicitudPendiente() {
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(
        hechoEjemplo,
        "Motivo válido",
        EstadoSolicitud.PENDIENTE, false
    );

    solicitud.cambiarEstado(EstadoSolicitud.ACEPTADA);

    assertEquals(EstadoSolicitud.ACEPTADA, solicitud.getEstado());
  }

  @Test
  void puedeRechazarUnaSolicitudPendiente() {
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(
        hechoEjemplo,
        "Motivo válido",
        EstadoSolicitud.PENDIENTE, true
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
        EstadoSolicitud.PENDIENTE, false
    );

    solicitud.cambiarEstado(EstadoSolicitud.ACEPTADA);

    assertThrows(IllegalStateException.class, () -> {
      solicitud.cambiarEstado(EstadoSolicitud.RECHAZADA);
    });
  }

}

