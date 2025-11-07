package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.estadistica.ComponenteEstadistico;
import ar.edu.utn.frba.dds.model.estadistica.Estadistica;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaCantidadSpam;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaCategoriaMaxima;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaHoraHechosCategoria;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaProvMaxHechosCategoria;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaProvMaxHechosColeccion;
import ar.edu.utn.frba.dds.model.estadistica.LocalizadorDeProvincias;
import ar.edu.utn.frba.dds.repositories.RepositorioHechos;
import ar.edu.utn.frba.dds.repositories.RepositorioSolicitudesDeCarga;
import ar.edu.utn.frba.dds.repositories.RepositorioSolicitudesEliminacion;
import ar.edu.utn.frba.dds.server.AppRole;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EstadisticasController implements WithSimplePersistenceUnit {

  private static ComponenteEstadistico compEstadistico = new ComponenteEstadistico();

  static EstadisticaCantidadSpam estadisticaSpam = new EstadisticaCantidadSpam();
  static EstadisticaHoraHechosCategoria estadisticaHHC = new EstadisticaHoraHechosCategoria();
  static EstadisticaCategoriaMaxima estadisticaCM =
      new EstadisticaCategoriaMaxima();

  static EstadisticaProvMaxHechosCategoria estadisticaPMHCat =
      new EstadisticaProvMaxHechosCategoria();
  static EstadisticaProvMaxHechosColeccion estadisticaPMHCol =
      new EstadisticaProvMaxHechosColeccion();

  //Agrego Estadisticas a la carga

  public EstadisticasController() {
    this.compEstadistico = ComponenteEstadistico.getInstance();
    compEstadistico.agregarEstadisticas(estadisticaSpam);
    compEstadistico.agregarEstadisticas(estadisticaHHC);
    compEstadistico.agregarEstadisticas(estadisticaCM);
    compEstadistico.agregarEstadisticas(estadisticaPMHCat);
    compEstadistico.agregarEstadisticas(estadisticaPMHCol);
    compEstadistico.calcularEstadisticas();
  }

  // --- Mostrar Estadisticas ---
  public static Map<String, Object> mostrarSpam(Context ctx) throws IOException {

    compEstadistico.calcularEstadisticas();
    estadisticaSpam.exportarEstadistica("descargar/estadisticas_cantidad_spam.csv");

    var cantSpam =  estadisticaSpam.getCantidadSpam();
    var fechaAhora = LocalDateTime.now();

    Map<String, Object> model = Map.of(
        "fecha",fechaAhora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
        "cantidadSpam", cantSpam
    );

    ctx.render("dashboard/estadisticaSpam.hbs", model);
    return model;
  }

  public static Map<String, Object> mostrarHoraPico(Context ctx) throws IOException {
    estadisticaHHC.exportarEstadistica("descargar/estadisticas_categoria_horaspico.csv");

    var reporte =  estadisticaHHC.getReporte();
    var fechaAhora = LocalDateTime.now();

    Map<String, Object> model = Map.of(
        "fecha", fechaAhora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
        "reporte",reporte
    );

    ctx.render("dashboard/estadisticaHoraPico.hbs", model);
    return model;
  }

  public static Map<String, Object> mostrarCategoriaMaxima(Context ctx) throws IOException {
    estadisticaCM.exportarEstadistica("descargar/estadisticas_categoria_maxima.csv");

    var reporte =  estadisticaCM.getReporte();
    var fechaAhora = LocalDateTime.now();

    Map<String, Object> model = Map.of(
        "fecha", fechaAhora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
        "reporte",reporte
    );
    ctx.render("dashboard/estadisticaCategoriaMaxima.hbs", model);
    return model;
  }

  public static Map<String, Object> mostrarCategoriaProvinciaMaxHechos(Context ctx) throws IOException {
    estadisticaPMHCat.exportarEstadistica("descargar/estadisticas_categoria_hechosmaximos.csv");

    var reporte =  estadisticaPMHCat.getReporte();
    var fechaAhora = LocalDateTime.now();

    Map<String, Object> model = Map.of(
        "fecha", fechaAhora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
        "reporte",reporte
    );
    ctx.render("dashboard/estadisticaCategoriaProvinciaMax.hbs", model);
    return model;
  }

  public static Map<String, Object> mostrarColeccionProvinciaMaxHechos(Context ctx) throws IOException {
    estadisticaPMHCol.exportarEstadistica("descargar/estadisticas_coleccion_hechosmaximos.csv");

    var reporte =  estadisticaPMHCol.getReporte();
    var fechaAhora = LocalDateTime.now();

    Map<String, Object> model = Map.of(
        "fecha", fechaAhora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
        "reporte",reporte
    );
    ctx.render("dashboard/estadisticaColeccionProvinciaMax.hbs", model);
    return model;
  }


}
