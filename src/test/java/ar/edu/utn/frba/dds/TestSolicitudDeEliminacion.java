package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.dominio.Contribuyente;
import ar.edu.utn.frba.dds.dominio.EstadoSolicitud;
import ar.edu.utn.frba.dds.dominio.Etiqueta;
import ar.edu.utn.frba.dds.dominio.Fuente;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.dominio.TipoDeHecho;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class TestSolicitudDeEliminacion {
  @Test
  void testAceptarSolicitudEliminacion() {
    Hecho hecho = new Hecho("Prueba", "Descripción", new Etiqueta("Categoria"),
        0.0, 0.0, LocalDate.of(2024,10,02),
        LocalDate.of(2024,9,5), Fuente.DATASET,
        null, TipoDeHecho.TEXTO);
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(hecho, "Motivo válido");

    solicitud.solicitudAceptada();

    assertEquals(EstadoSolicitud.ACEPTADA, solicitud.getEstado());
  }

  @Test
  void testSolicitudMotivoMuyExtenso() {
    Hecho hecho = new Hecho("Prueba", "Descripción", new Etiqueta("Categoria"),
        0.0, 0.0, LocalDate.of(2024,10,02),
        LocalDate.of(2024,9,5), Fuente.DATASET,
        null, TipoDeHecho.TEXTO);

    String motivo = "Motivo invalido".repeat(50);


    assertThrows(RuntimeException.class, () -> new SolicitudDeEliminacion(hecho, motivo));
  }
}
