package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.dominio.solicitudes.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.FactorySolicitudDeEliminacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudDeEliminacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

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
        LocalDate.of(2023, 1, 1),
        LocalDate.now(),
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
        EstadoSolicitud.PENDIENTE
    );

    solicitud.cambiarEstado(EstadoSolicitud.ACEPTADA, "unEvaluador");

    assertEquals(EstadoSolicitud.ACEPTADA, solicitud.getEstado());
  }

  @Test
  void puedeRechazarUnaSolicitudPendiente() {
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(
        hechoEjemplo,
        "Motivo válido",
        EstadoSolicitud.PENDIENTE
    );

    solicitud.cambiarEstado(EstadoSolicitud.RECHAZADA, "unEvaluador");

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

    solicitud.cambiarEstado(EstadoSolicitud.ACEPTADA, "unEvaluador");

    assertThrows(IllegalStateException.class, () -> {
      solicitud.cambiarEstado(EstadoSolicitud.RECHAZADA, "unEvaluador");
    });
  }

  @Test
  void registraCorrectamenteElEvaluadorAlAceptar() {
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(
        hechoEjemplo, "Motivo válido", EstadoSolicitud.PENDIENTE
    );

    solicitud.cambiarEstado(EstadoSolicitud.ACEPTADA, "admin123");

    assertEquals("admin123", solicitud.getEvaluador());
  }
}
/*
  @Test
  void puedeApelarUnaSolicitudRechazada() {
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(
        hechoEjemplo, "Motivo válido", EstadoSolicitud.PENDIENTE
    );
    solicitud.cambiarEstado(EstadoSolicitud.RECHAZADA, "admin");
    solicitud.apelar("No estoy de acuerdo");

    assertEquals(EstadoSolicitud.APELADA, solicitud.getEstado());
    assertEquals("No estoy de acuerdo", solicitud.getMotivoApelacion());
  }


}*/
