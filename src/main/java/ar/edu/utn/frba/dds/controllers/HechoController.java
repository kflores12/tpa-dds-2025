package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.model.entities.criterios.Criterio;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioBase;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioCategoria;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioDescripcion;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioFecha;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioFechaCarga;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioRangoFechas;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioTitulo;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioUbicacion;
import ar.edu.utn.frba.dds.model.entities.solicitudes.DetectorDeSpam;
import ar.edu.utn.frba.dds.model.entities.solicitudes.DetectorDeSpamInteligente;
import ar.edu.utn.frba.dds.model.entities.solicitudes.SolicitudDeCarga;
import ar.edu.utn.frba.dds.repositories.RepositorioHechos;
import ar.edu.utn.frba.dds.repositories.RepositorioSolicitudesDeCarga;
import ar.edu.utn.frba.dds.server.AppRole;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class HechoController implements WithSimplePersistenceUnit {

  private final RepositorioSolicitudesDeCarga repoSolicitudes =
      RepositorioSolicitudesDeCarga.getInstance();
  private final RepositorioHechos repoHechos = RepositorioHechos.getInstance();
  private final DetectorDeSpam detectorDeSpam = new DetectorDeSpamInteligente();

  private static final String UPLOAD_DIR = "uploads/uploads";

  // --- Mostrar formulario de creación de hecho ---
  public Map<String, Object> showCreationForm(@NotNull Context ctx) {
    AppRole role = ctx.attribute("userRole");
    boolean esRegistrado = role == AppRole.USER || role == AppRole.ADMIN;

    Map<String, Object> model = new HashMap<>();
    model.put("titulo", "Cargar Nuevo Hecho");
    model.put("esRegistrado", esRegistrado);
    return model;
  }

  // --- Procesar creación ---
  public void create(@NotNull Context ctx) {
    AppRole role = ctx.attribute("userRole");
    boolean esRegistrado = role == AppRole.USER || role == AppRole.ADMIN;

    try {
      String titulo = ctx.formParam("titulo");
      String descripcion = ctx.formParam("descripcion");
      String categoria = ctx.formParam("categoria");
      String fechaAcontecimientoStr = ctx.formParam("fechaAcontecimiento");
      String latitudStr = ctx.formParam("latitud");
      String longitudStr = ctx.formParam("longitud");

      if (latitudStr == null || longitudStr == null
          || latitudStr.isBlank() || longitudStr.isBlank()) {
        ctx.sessionAttribute("flash_error", "Debe ingresar latitud y longitud válidas.");
        ctx.status(400);
        ctx.redirect("/hechos/nuevo");
        return;
      }

      Double latitud = Double.parseDouble(latitudStr);
      Double longitud = Double.parseDouble(longitudStr);

      LocalDateTime fechaAcontecimiento = LocalDateTime.parse(fechaAcontecimientoStr);

      // --- Guardar multimedia ---
      UploadedFile multimediaFile = ctx.uploadedFile("multimedia");
      String multimediaUrl =
          (multimediaFile != null
              && multimediaFile.filename() != null && !multimediaFile.filename().isBlank()
          )
          ? saveUploadedFile(multimediaFile)
          : null;

      boolean esSpam = detectorDeSpam.esSpam(titulo + " " + descripcion);

      if (esSpam) {
        ctx.sessionAttribute("flash_error",
            "Tu solicitud fue detectada como SPAM y no se registró.");
        ctx.redirect("/hechos/nuevo");
        return;
      }

      // --- Crear y persistir solicitud ---
      SolicitudDeCarga solicitud = new SolicitudDeCarga(
          titulo,
          descripcion,
          categoria,
          latitud,
          longitud,
          fechaAcontecimiento,
          LocalDateTime.now(),
          multimediaUrl,
          esRegistrado
      );

      withTransaction(() -> repoSolicitudes.registrar(solicitud));

      ctx.sessionAttribute("flash_message", "Hecho cargado. Pendiente de revisión.");
      ctx.redirect("/hechos/confirmacion/" + solicitud.getId());

    } catch (Exception e) {
      e.printStackTrace();
      ctx.sessionAttribute("flash_error",
          "Error al procesar el formulario: " + e.getMessage());
      ctx.status(400);
      ctx.redirect("/hechos/nuevo");
    }
  }

  // --- Confirmación ---
  public void showConfirmation(@NotNull Context ctx) {
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

  // --- Guardar archivo subido ---
  private String saveUploadedFile(@NotNull UploadedFile file) throws IOException {
    File uploadDir = new File(UPLOAD_DIR);
    if (!uploadDir.exists() && !uploadDir.mkdirs()) {
      throw new IOException(
          "No se pudo crear el directorio de subida: " + uploadDir.getAbsolutePath()
      );
    }

    String extension = "";
    int dotIndex = file.filename().lastIndexOf('.');
    if (dotIndex > 0) {
      extension = file.filename().substring(dotIndex);
    }

    String uniqueName = UUID.randomUUID() + extension;
    File targetFile = new File(uploadDir, uniqueName);

    try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
      file.content().transferTo(outputStream);
    }

    return "/uploads/uploads/" + uniqueName;
  }

  // --- Formulario de búsqueda ---
  public Map<String, Object> showBusquedaForm(@NotNull Context ctx) {
    Map<String, Object> modelo = new HashMap<>();

    Map<String, String> filtros = ctx.queryParamMap().entrySet().stream()
        .filter(e ->
            !e.getValue().isEmpty()
                && e.getValue().get(0) != null
                && !e.getValue().get(0).isBlank())
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

    modelo.put("filtros", filtros);
    List<Criterio> criterios = new ArrayList<>();

    if (!filtros.isEmpty()) {
      filtros.forEach((clave, valor) -> {
        switch (clave) {
          case "titulo" -> criterios.add(new CriterioTitulo(valor));
          case "categoria" -> criterios.add(new CriterioCategoria(valor));
          case "descripcion" -> criterios.add(new CriterioDescripcion(valor));
          case "latitud", "longitud" -> {
            if (filtros.containsKey("latitud") && filtros.containsKey("longitud")) {
              criterios.add(new CriterioUbicacion(
                  Double.parseDouble(filtros.get("latitud")),
                  Double.parseDouble(filtros.get("longitud"))
              ));
            }
          }
          case "fechaAcontecimiento" -> criterios.add(new CriterioFecha(LocalDate.parse(valor)));
          case "fechaCarga" -> criterios.add(new CriterioFechaCarga(LocalDate.parse(valor)));
          case "fechaDesde", "fechaHasta" -> {
            if (filtros.containsKey("fechaDesde") && filtros.containsKey("fechaHasta")) {
              criterios.add(new CriterioRangoFechas(
                  LocalDate.parse(filtros.get("fechaDesde")),
                  LocalDate.parse(filtros.get("fechaHasta"))
              ));
            }
          }
          default -> {
            // No hacer nada, clave no reconocida.
          }
        }
      });


    } else {
      criterios.add(new CriterioBase());
    }

    List<Hecho> resultados = repoHechos.obtenerDisponibles().stream()
        .filter(hecho ->
            criterios
                .stream().allMatch(c -> c.aplicarFiltro(hecho)) && hecho.getDisponibilidad())
        .toList();

    modelo.put("resultadosBusqueda", true);
    modelo.put("hechos", resultados);
    modelo.put("cantidadResultados", resultados.size());

    return modelo;
  }
}
