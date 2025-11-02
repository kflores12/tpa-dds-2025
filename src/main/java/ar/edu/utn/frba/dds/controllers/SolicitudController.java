package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.model.entities.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.model.entities.solicitudes.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.repositories.RepositorioHechos;
import ar.edu.utn.frba.dds.repositories.RepositorioSolicitudesEliminacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class SolicitudController implements WithSimplePersistenceUnit {

  private final RepositorioHechos repoHechos;
  private final RepositorioSolicitudesEliminacion repoSolicitudes;

  public SolicitudController() {
    this.repoHechos = RepositorioHechos.getInstance();
    this.repoSolicitudes = RepositorioSolicitudesEliminacion.getInstance();
  }

  // --- Mostrar formulario de solicitud ---
  public Map<String, Object> showSolicitudForm(@NotNull Context ctx) {
    Map<String, Object> model = new HashMap<>();
    Long hechoId = null;

    try {
      hechoId = ctx.pathParamAsClass("id", Long.class)
          .check(id -> id > 0, "ID debe ser positivo")
          .get();

      Hecho hecho = repoHechos.buscarHechoPorId(hechoId);
      if (hecho == null) {
        ctx.status(404);
        ctx.result("Error 404: Hecho no encontrado para solicitar eliminación.");
        return model;
      }

      // Se podría agregar control de permisos en el futuro:
      // Usuario usuario = ctx.sessionAttribute("usuario");
      // if (!puedeSolicitar(usuario, hecho)) { ... }

      model.put("titulo", "Solicitar Eliminación");
      model.put("hecho", hecho);
      model.put("hechoId", hechoId);
      return model;

    } catch (Exception e) {
      ctx.status(400);
      ctx.result("ID de hecho no válido.");
      return model;
    }
  }

  // --- Procesar creación de solicitud ---
  public void createSolicitudEliminacion(@NotNull Context ctx) {
    String hechoIdStr = ctx.formParam("hechoId");
    Long hechoId = null;

    try {
      // --- Validaciones de parámetros ---
      if (hechoIdStr == null || hechoIdStr.isBlank()) {
        throw new IllegalArgumentException("ID de hecho faltante o vacío.");
      }

      hechoId = Long.parseLong(hechoIdStr);
      String motivo = ctx.formParam("motivo");
      boolean esSpam = Boolean.parseBoolean(ctx.formParam("esSpam"));

      if (motivo == null || motivo.isBlank()) {
        ctx.sessionAttribute("flash_error", "Debe ingresar un motivo para la solicitud.");
        ctx.status(400);
        ctx.redirect("/hechos/" + hechoId + "/solicitud");
        return;
      }
      if (motivo.length() > 500) {
        ctx.sessionAttribute("flash_error", "El motivo no puede superar los 500 caracteres.");
        ctx.status(400);
        ctx.redirect("/hechos/" + hechoId + "/solicitud");
        return;
      }

      // --- Validación del hecho ---
      Hecho hecho = repoHechos.buscarHechoPorId(hechoId);
      if (hecho == null) {
        ctx.status(404);
        ctx.sessionAttribute("flash_error", "Hecho no encontrado.");
        ctx.redirect("/home");
        return;
      }

      // --- Estado inicial según si es spam ---
      EstadoSolicitud estadoInicial = esSpam ? EstadoSolicitud.RECHAZADA : EstadoSolicitud.PENDIENTE;

      // --- Crear y persistir la solicitud ---
      SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(hecho, motivo, estadoInicial, esSpam);
      withTransaction(() -> repoSolicitudes.cargarSolicitudEliminacion(solicitud));

      // --- Feedback para el usuario ---
      String mensaje = esSpam
          ? "❌ Solicitud clasificada como SPAM y rechazada automáticamente."
          : "✅ Solicitud enviada. Pendiente de revisión.";

      ctx.sessionAttribute("flash_message", mensaje);
      ctx.redirect("/solicitudes/resultado/" + solicitud.getId());

    } catch (NumberFormatException e) {
      ctx.sessionAttribute("flash_error", "El ID del hecho debe ser un número válido.");
      ctx.status(400);
      ctx.redirect("/home");

    } catch (IllegalArgumentException e) {
      ctx.sessionAttribute("flash_error", e.getMessage());
      ctx.status(400);
      ctx.redirect("/home");

    } catch (Exception e) {
      ctx.sessionAttribute("flash_error", "Error inesperado al procesar la solicitud: " + e.getMessage());
      ctx.status(500);
      ctx.redirect("/home");
    }
  }

  // --- Mostrar resultado de la solicitud ---
  public Map<String, Object> showResultado(@NotNull Context ctx) {
    Map<String, Object> model = new HashMap<>();

    try {
      Long solicitudId = ctx.pathParamAsClass("solicitudId", Long.class)
          .check(id -> id > 0, "ID debe ser positivo")
          .get();

      SolicitudDeEliminacion solicitud = repoSolicitudes.getSolicitudDeEliminacion(solicitudId);
      if (solicitud == null) {
        ctx.status(404);
        ctx.result("Solicitud no encontrada.");
        return model;
      }

      model.put("titulo", "Resultado de Solicitud de Eliminación");
      model.put("solicitud", solicitud);
      model.put("esSpam", solicitud.getEsSpam());
      model.put("estado", solicitud.getEstado());
      model.put("mensajeFlash", ctx.sessionAttribute("flash_message"));

      ctx.sessionAttribute("flash_message", null);
      return model;

    } catch (Exception e) {
      ctx.status(400);
      ctx.result("ID de solicitud no válido o error interno.");
      return model;
    }
  }
}