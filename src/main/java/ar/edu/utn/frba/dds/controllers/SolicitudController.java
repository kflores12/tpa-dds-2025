package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.model.entities.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.model.entities.solicitudes.FactorySolicitudDeEliminacion;
import ar.edu.utn.frba.dds.model.entities.solicitudes.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.repositories.RepositorioHechos;
import ar.edu.utn.frba.dds.repositories.RepositorioSolicitudesEliminacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static java.util.List.of;

public class SolicitudController implements WithSimplePersistenceUnit {
  private  RepositorioHechos repoHechos;
  private  RepositorioSolicitudesEliminacion repoSolicitudes;
  private  FactorySolicitudDeEliminacion solicitudFactory;

  public SolicitudController() {
    // CORRECCIÓN: Asegúrate de que TODAS las instancias se obtengan aquí
    this.repoHechos = RepositorioHechos.getInstance();
    this.repoSolicitudes = RepositorioSolicitudesEliminacion.getInstance();
    //this.solicitudFactory = null; // Reemplazar con tu inicialización real o comentarla por ahora.
  }

  public Map<String, Object> showSolicitudForm(@NotNull Context ctx) {
    Long hechoId = ctx.pathParamAsClass("id", Long.class)
        .check(id -> id > 0, "ID debe ser positivo")
        .get();

    Hecho hecho = repoHechos.buscarHechoPorId(hechoId);

    if (hecho == null) {
      ctx.status(404);
      ctx.result("Error 404: Hecho no encontrado para solicitar eliminación.");
      throw new RuntimeException("El hecho no existe");
    }

    Map<String, Object> model = Map.of(
        "titulo", "Solicitar Eliminación",
        "hecho", hecho,
        "hechoId", hechoId
    );

    return Map.of(
        "titulo", "Solicitar Eliminación",
        "hecho", hecho,
        "hechoId", hechoId
    );

  }

  // === CREACIÓN DE SOLICITUD ===
  public void createSolicitudEliminacion(@NotNull Context ctx) {
    try {
      Long hechoId = Long.parseLong(ctx.formParam("hechoId"));
      String motivo = ctx.formParam("motivo");
      boolean esSpam = Boolean.parseBoolean(ctx.formParam("esSpam"));

      if (motivo == null || motivo.isBlank() || motivo.length() > 500) {
        throw new IllegalArgumentException("El motivo debe tener entre 1 y 500 caracteres.");
      }

      Hecho hecho = repoHechos.buscarHechoPorId(hechoId);
      if (hecho == null) {
        throw new IllegalArgumentException("Hecho no encontrado.");
      }

      EstadoSolicitud estado = esSpam ? EstadoSolicitud.RECHAZADA : EstadoSolicitud.PENDIENTE;

      SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(hecho, motivo, estado, esSpam);

      withTransaction(() -> repoSolicitudes.cargarSolicitudEliminacion(solicitud));

      String mensaje = esSpam
          ? "❌ Solicitud rechazada automáticamente por detección de spam."
          : "✅ Solicitud de eliminación enviada. Pendiente de revisión.";

      ctx.sessionAttribute("flash_message", mensaje);
      ctx.redirect("/solicitudes/resultado/" + solicitud.getId());

    } catch (Exception e) {
      ctx.status(400);
      ctx.sessionAttribute("flash_error", "Error al procesar la solicitud: " + e.getMessage());
      ctx.redirect("/home");
    }
  }

  // === MOSTRAR RESULTADO ===
  public Map<String, Object> showResultado(@NotNull Context ctx) {
    Long solicitudId = ctx.pathParamAsClass("solicitudId", Long.class).get();

    SolicitudDeEliminacion solicitud = repoSolicitudes.getSolicitudDeEliminacion(solicitudId);

    if (solicitud == null) {
      ctx.status(404);
      throw new RuntimeException("Solicitud de eliminación no encontrada.");
    }

    return Map.of(
        "titulo", "Resultado de Solicitud de Eliminación",
        "solicitud", solicitud,
        "flash_message", ctx.sessionAttribute("flash_message")
    );
  }
}
