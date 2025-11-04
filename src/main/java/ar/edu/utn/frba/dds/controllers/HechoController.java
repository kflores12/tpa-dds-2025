package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.entities.solicitudes.SolicitudDeCarga;
import ar.edu.utn.frba.dds.repositories.RepositorioFuentes;
import ar.edu.utn.frba.dds.repositories.RepositorioSolicitudesDeCarga;
import ar.edu.utn.frba.dds.server.AppRole;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HechoController implements WithSimplePersistenceUnit {

  private final RepositorioSolicitudesDeCarga repoSolicitudes;
  private final RepositorioFuentes repoFuentes;

  private static final String UPLOAD_DIR = "uploads/uploads";

  private static final Map<String, Long> CONTEXTO_A_FUENTE = Map.of(
      "anonimo", 1L,
      "registrado", 2L
  );


  public HechoController() {
    this.repoSolicitudes = RepositorioSolicitudesDeCarga.getInstance();
    this.repoFuentes = RepositorioFuentes.getInstance();
  }

  // --- Mostrar formulario de creación de hecho ---
  public Map<String, Object> showCreationForm(@NotNull Context ctx) {
    boolean esRegistrado =
        ctx.attribute("userRole") == AppRole.USER || ctx.attribute("userRole") == AppRole.ADMIN;
    String contexto = esRegistrado ? "registrado" : "anonimo";


    Long fuenteId = CONTEXTO_A_FUENTE.get(contexto);
    Fuente fuente = repoFuentes.getFuente(fuenteId);

    if (fuente == null) {
      ctx.status(500);
      return Map.of(
          "titulo", "Error interno",
          "mensaje", "No se encontró la fuente configurada para el contexto: " + contexto
      );
    }


    return Map.of(
        "titulo", "Cargar Nuevo Hecho",
        "fuente", fuente,
        "esRegistrado", esRegistrado
    );
  }

  public void create(@NotNull Context ctx) {
    boolean esRegistrado = ctx.sessionAttribute("usuarioRegistrado") != null;
    String contexto = esRegistrado ? "registrado" : "anonimo";
    Long fuenteId = CONTEXTO_A_FUENTE.get(contexto);

    try {
      String titulo = ctx.formParam("titulo");
      String descripcion = ctx.formParam("descripcion");
      String categoria = ctx.formParam("categoria");
      String fechaAcontecimientoStr = ctx.formParam("fechaAcontecimiento");

      Double latitud = Double.parseDouble(ctx.formParam("latitud"));
      Double longitud = Double.parseDouble(ctx.formParam("longitud"));
      LocalDateTime fechaAcontecimiento = LocalDateTime.parse(fechaAcontecimientoStr);

      UploadedFile multimediaFile = ctx.uploadedFile("multimedia");
      String multimediaUrl = null;

      if (multimediaFile != null && multimediaFile.filename() != null && !multimediaFile.filename().isBlank()) {
        multimediaUrl = saveUploadedFile(multimediaFile);
      }

      Fuente fuenteAsociada = repoFuentes.getFuente(fuenteId);
      if (fuenteAsociada == null) {
        ctx.sessionAttribute("flash_error", "Error interno: fuente no encontrada.");
        ctx.redirect("/home");
        return;
      }

      SolicitudDeCarga solicitud = new SolicitudDeCarga(
          titulo, descripcion, categoria, latitud, longitud,
          fechaAcontecimiento, multimediaUrl, esRegistrado, fuenteAsociada
      );

      withTransaction(() -> repoSolicitudes.registrar(solicitud));

      ctx.sessionAttribute("flash_message", "Hecho cargado. Pendiente de revisión.");
      ctx.redirect("/hechos/confirmacion/" + solicitud.getId());

    } catch (Exception e) {
      ctx.sessionAttribute("flash_error", "Error al procesar el formulario: " + e.getMessage());
      ctx.status(400);
      ctx.redirect("/hechos/nuevo");
    }
  }

  // --- Confirmación ---
  public void showConfirmation(Context ctx) {
    Long solicitudId = ctx.pathParamAsClass("solicitudId", Long.class)
        .check(id -> id > 0, "ID de solicitud debe ser positivo")
        .get();

    SolicitudDeCarga solicitud = repoSolicitudes.getSolicitud(solicitudId);

    if (solicitud == null) {
      ctx.status(404);
      ctx.result("Solicitud no encontrada con ID " + solicitudId);
      return;
    }

    Map<String, Object> model = new HashMap<>();
    model.put("flash_message", ctx.sessionAttribute("flash_message"));
    model.put("solicitud", solicitud);
    model.put("registrado", solicitud.esRegistrado());

    ctx.render("confirmacion-solicitudCarga.hbs", model);
    ctx.sessionAttribute("flash_message", null);
  }

  private String saveUploadedFile(@NotNull UploadedFile file) throws IOException {
    // Directorio de destino (fuera de src/main/resources)
    File uploadDir = new File(UPLOAD_DIR);
    if (!uploadDir.exists() && !uploadDir.mkdirs()) {
      throw new IOException("No se pudo crear el directorio de subida: " + uploadDir.getAbsolutePath());
    }

    // Nombre original y extensión
    String originalName = file.filename();
    String extension = "";
    int dotIndex = originalName.lastIndexOf('.');
    if (dotIndex > 0) {
      extension = originalName.substring(dotIndex);
    }

    // Nombre único para evitar colisiones
    String uniqueName = UUID.randomUUID() + extension;
    File targetFile = new File(uploadDir, uniqueName);

    // Guardar archivo en disco
    try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
      file.content().transferTo(outputStream);
    }

    // URL pública servida por Javalin (/uploads)
    return "/uploads/uploads/" + uniqueName;
  }
}
