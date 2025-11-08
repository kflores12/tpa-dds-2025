package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.entities.Coleccion;
import ar.edu.utn.frba.dds.model.entities.GeneradorHandleUuid;
import ar.edu.utn.frba.dds.model.entities.algoritmosconcenso.AlgoritmoDeConsenso;
import ar.edu.utn.frba.dds.model.entities.criterios.*;
import ar.edu.utn.frba.dds.model.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.entities.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.repositories.RepositorioColecciones;
import ar.edu.utn.frba.dds.repositories.RepositorioCriterios;
import ar.edu.utn.frba.dds.repositories.RepositorioFuentes;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ColeccionController implements WithSimplePersistenceUnit {

  private final RepositorioColecciones repoColecciones = RepositorioColecciones.getInstance();
  private final RepositorioFuentes repoFuentes = RepositorioFuentes.getInstance();
  private final RepositorioCriterios repoCriterios = RepositorioCriterios.getInstance();

  public void mostrarFormularioCreacion(Context ctx) {
    var criteriosDesdeBD = repoCriterios.obtenerTodos();
    var todosLosAlgoritmos = List.of(AlgoritmoDeConsenso.values());
    var todosLosCriterios = criteriosDesdeBD.stream()
        .map(criterio -> Map.of(
            "id", criterio.getId(),
            "nombre", getHardcodedNombreCriterio(criterio.getId())
        ))
        .collect(Collectors.toList());

    Map<String, Object> model = new HashMap<>();
    model.put("todosLosCriterios", todosLosCriterios);
    model.put("todosLosAlgoritmos", todosLosAlgoritmos);

    String flashMessage = ctx.sessionAttribute("flash_message");
    if (flashMessage != null) {
      model.put("flash_message", flashMessage);
      ctx.sessionAttribute("flash_message", null);
    }

    String flashError = ctx.sessionAttribute("flash_error");
    if (flashError != null) {
      model.put("flash_error", flashError);
      ctx.sessionAttribute("flash_error", null);
    }

    ctx.render("/dashboard/creacion-coleccion.hbs", model);
  }

  public void crearColeccion(Context ctx) {
    String titulo = ctx.formParam("titulo");
    String descripcion = ctx.formParam("descripcion");
    AlgoritmoDeConsenso algoritmo = AlgoritmoDeConsenso.valueOf(ctx.formParam("algoritmo"));

    try {
      withTransaction(() -> {
        // ⚙️ Buscar automáticamente la FuenteDinamica
        Fuente fuente = repoFuentes.getFuentes().stream()
            .filter(f -> f instanceof FuenteDinamica)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No existe una FuenteDinamica en el sistema"));

        List<Criterio> criterios = new ArrayList<>();

        // --- Criterios de pertenencia seleccionados ---

        if (ctx.formParam("criterioTitulo") != null) {
          String valor = ctx.formParam("criterioTituloValor");
          if (valor != null && !valor.isEmpty()) {
            criterios.add(new CriterioTitulo(valor));

          }
        }

        if (ctx.formParam("criterioCategoria") != null) {
          String valor = ctx.formParam("criterioCategoriaValor");
          if (valor != null && !valor.isEmpty()) {
            criterios.add(new CriterioCategoria(valor));
          }
        }

        if (ctx.formParam("criterioDescripcion") != null) {
          String valor = ctx.formParam("criterioDescripcionValor");
          if (valor != null && !valor.isEmpty()) {
            criterios.add(new CriterioDescripcion(valor));
          }
        }

        if (ctx.formParam("criterioFecha") != null) {
          String valor = ctx.formParam("criterioFechaValor");
          if (valor != null && !valor.isEmpty()) {
            criterios.add(new CriterioFecha(LocalDate.parse(valor)));
          }
        }

        if (ctx.formParam("criterioFechaCarga") != null) {
          String valor = ctx.formParam("criterioFechaCargaValor");
          if (valor != null && !valor.isEmpty()) {
            criterios.add(new CriterioFechaCarga(LocalDate.parse(valor)));
          }
        }

        if (ctx.formParam("criterioRango") != null) {
          String desdeStr = ctx.formParam("criterioRangoDesde");
          String hastaStr = ctx.formParam("criterioRangoHasta");
          if (desdeStr != null && hastaStr != null && !desdeStr.isEmpty() && !hastaStr.isEmpty()) {
            criterios.add(new CriterioRangoFechas(LocalDate.parse(desdeStr),
                LocalDate.parse(hastaStr)));
          }
        }

        if (ctx.formParam("criterioUbicacion") != null) {
          String latStr = ctx.formParam("criterioLatitudValor");
          String lonStr = ctx.formParam("criterioLongitudValor");
          if (latStr != null && lonStr != null && !latStr.isEmpty() && !lonStr.isEmpty()) {
            criterios.add(
                new CriterioUbicacion(Double.parseDouble(latStr), Double.parseDouble(lonStr))
            );
          }
        }

        // --- Crear la colección ---
        GeneradorHandleUuid generador = new GeneradorHandleUuid();
        Coleccion nueva = new Coleccion(titulo, descripcion, fuente, criterios, generador.generar(), algoritmo);
        nueva.actualizarHechosConsensuados();
        repoColecciones.cargarColeccion(nueva);
      });

      ctx.sessionAttribute("flash_message", "Colección creada exitosamente!");
    } catch (Exception e) {
      e.printStackTrace();
      ctx.sessionAttribute("flash_error", "Error al crear la colección: " + e.getMessage());
    }

    ctx.redirect("/dashboard/colecciones/crear");
  }


  public void mostrarColecciones(Context ctx) {
    var colecciones = repoColecciones.getColecciones();

    Map<String, Object> model = new HashMap<>();
    model.put("colecciones", colecciones);

    String flashMessage = ctx.sessionAttribute("flash_message");
    if (flashMessage != null) {
      model.put("flash_message", flashMessage);
      ctx.sessionAttribute("flash_message", null);
    }

    ctx.render("/dashboard/listado-colecciones.hbs", model);
  }

  public void mostrarFormularioEdicion(Context ctx) {
    Long id = ctx.pathParamAsClass("id", Long.class).get();
    Coleccion coleccionAeditar = repoColecciones.getColeccionById(id);

    if (coleccionAeditar == null) {
      ctx.status(404).result("Colección no encontrada");
      return;
    }

    var todosLosAlgoritmos = Stream.of(AlgoritmoDeConsenso.values())
        .map(algoritmo -> Map.of(
            "name", algoritmo.name(),
            "seleccionado", algoritmo.equals(coleccionAEditar.getAlgoritmo())
        ))
        .toList();

    Map<String, Object> model = Map.of(
        "coleccion", coleccionAeditar,
        "todosLosAlgoritmos", todosLosAlgoritmos
    );

    ctx.render("/dashboard/modificacion-coleccion.hbs", model);
  }

  public void editarColeccion(Context ctx) {
    Long id = ctx.pathParamAsClass("id", Long.class).get();
    AlgoritmoDeConsenso nuevoAlgoritmo = AlgoritmoDeConsenso.valueOf(ctx.formParam("algoritmo"));

    try {
      withTransaction(() -> {
        Coleccion coleccion = repoColecciones.getColeccionById(id);
        coleccion.setAlgoritmo(nuevoAlgoritmo);
        entityManager().merge(coleccion);
      });

      ctx.sessionAttribute("flash_message", "Colección actualizada exitosamente!");
    } catch (Exception e) {
      e.printStackTrace();
      ctx.sessionAttribute("flash_error", "Error al actualizar la colección: " + e.getMessage());
    }

    ctx.redirect("/dashboard/colecciones/modificar");
  }

  private String getHardcodedNombreCriterio(Long id) {
    switch (id.intValue()) {
      case 1: return "Por Categoría";
      case 2: return "Por Descripción";
      case 3: return "Por Fecha";
      case 4: return "Por Fecha de Carga";
      case 5: return "Por Rango de Fechas";
      case 6: return "Por Ubicación";
      case 7: return "Por Título";
      default: return "Criterio Desconocido (ID: " + id + ")";
    }
  }
}