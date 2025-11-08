package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.estadistica.Estadistica;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaCantidadSpam;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaCategoriaMaxima;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaHoraHechosCategoria;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaProvMaxHechosCategoria;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaProvMaxHechosColeccion;
import ar.edu.utn.frba.dds.repositories.RepositorioColecciones;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EstadisticasController implements WithSimplePersistenceUnit {

  static RepositorioColecciones repoC = RepositorioColecciones.getInstance();

  public EstadisticasController() {
  }

  // --- Mostrar Estadisticas ---
  public static Map<String, Object> mostrarSpam(Context ctx) throws IOException {
    repoC.consesuareEchos();
    EstadisticaCantidadSpam estadisticaSpam = new EstadisticaCantidadSpam();
    estadisticaSpam.calcularEstadistica();
    estadisticaSpam.exportarEstadistica("descargar/estadisticas_cantidad_spam.csv");

    var cantSpam =  estadisticaSpam.getCantidadSpam();
    var fechaAhora = LocalDateTime.now();

    Map<String, Object> model = Map.of(
        "cantidadSpam", cantSpam,
        "fecha", fechaAhora
    );

    ctx.render("dashboard/estadisticaSpam.hbs", model);
    return model;
  }

  public static Map<String, Object> mostrarHoraPico(Context ctx) throws IOException {
    repoC.consesuareEchos();
    EstadisticaHoraHechosCategoria estadisticaHhc = new EstadisticaHoraHechosCategoria();
    estadisticaHhc.calcularEstadistica();

    estadisticaHhc.exportarEstadistica("descargar/estadisticas_categoria_horaspico.csv");

    var reporte =  estadisticaHhc.getReporte();
    var fechaAhora = LocalDateTime.now();

    Map<String, Object> model = Map.of(
        "fecha", fechaAhora,
        "reporte", reporte
    );

    ctx.render("dashboard/estadisticaHoraPico.hbs", model);
    return model;
  }

  public static Map<String, Object> mostrarCategoriaMaxima(Context ctx) throws IOException {
    repoC.consesuareEchos();
    EstadisticaCategoriaMaxima estadisticaCm = new EstadisticaCategoriaMaxima();
    estadisticaCm.calcularEstadistica();

    estadisticaCm.exportarEstadistica("descargar/estadisticas_categoria_maxima.csv");

    var reporte =  estadisticaCm.getReporte();
    var fechaAhora = LocalDateTime.now();

    Map<String, Object> model = Map.of(
        "fecha", fechaAhora,
        "reporte", reporte
    );
    ctx.render("dashboard/estadisticaCategoriaMaxima.hbs", model);
    return model;
  }

  public static Map<String, Object> mostrarCategoriaProvinciaMaxHechos(Context ctx)
      throws IOException {
    repoC.consesuareEchos();
    EstadisticaProvMaxHechosCategoria estadisticaPmhCat = new EstadisticaProvMaxHechosCategoria();
    estadisticaPmhCat.calcularEstadistica();

    estadisticaPmhCat.exportarEstadistica("descargar/estadisticas_categoria_hechosmaximos.csv");

    var reporte =  estadisticaPmhCat.getReporte();
    var fechaAhora = LocalDateTime.now();

    Map<String, Object> model = Map.of(
        "fecha", fechaAhora,
        "reporte", reporte
    );
    ctx.render("dashboard/estadisticaCategoriaProvinciaMax.hbs", model);
    return model;
  }

  public static Map<String, Object> mostrarColeccionProvinciaMaxHechos(Context ctx)
      throws IOException {
    repoC.consesuareEchos();
    EstadisticaProvMaxHechosColeccion estadisticaPmhCol = new EstadisticaProvMaxHechosColeccion();
    estadisticaPmhCol.calcularEstadistica();

    estadisticaPmhCol.exportarEstadistica("descargar/estadisticas_coleccion_hechosmaximos.csv");

    var reporte =  estadisticaPmhCol.getReporte();
    var fechaAhora = LocalDateTime.now();

    Map<String, Object> model = Map.of(
        "fecha", fechaAhora,
        "reporte", reporte
    );
    ctx.render("dashboard/estadisticaColeccionProvinciaMax.hbs", model);
    return model;
  }


}
