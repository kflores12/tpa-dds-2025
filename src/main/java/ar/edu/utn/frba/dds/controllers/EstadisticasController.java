package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.estadistica.ComponenteEstadistico;
import ar.edu.utn.frba.dds.model.estadistica.Estadistica;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaCantidadSpam;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaCategoriaMaxima;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaHoraHechosCategoria;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaProvMaxHechosCategoria;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaProvMaxHechosColeccion;
import ar.edu.utn.frba.dds.model.estadistica.LocalizadorDeProvincias;
import ar.edu.utn.frba.dds.repositories.RepositorioSolicitudesDeCarga;
import ar.edu.utn.frba.dds.repositories.RepositorioSolicitudesEliminacion;
import ar.edu.utn.frba.dds.server.AppRole;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EstadisticasController implements WithSimplePersistenceUnit {

  //LocalDateTime fechaAhora = LocalDateTime.now();

  EstadisticaCantidadSpam estadisticaSpam = new EstadisticaCantidadSpam();
  EstadisticaCategoriaMaxima estadisticaCm =
      new EstadisticaCategoriaMaxima();
  //EstadisticaCantidadSpam estadisticaCs = new EstadisticaCantidadSpam();
  //EstadisticaProvMaxHechosCategoria estadisticaPmhcat =
    //  new EstadisticaProvMaxHechosCategoria();
  //EstadisticaProvMaxHechosColeccion estadisticaPmhcalt =
    //  new EstadisticaProvMaxHechosColeccion();

  //Agrego Estadisticas a la carga
  List<Estadistica> estadisticas = new ArrayList<>();
    //estadisticas.add(estadisticaSpam);
    //estadisticas.add(estadisticaCm);
    //estadisticas.add(estadisticaPmhcat);
    //estadisticas.add(estadisticaPmhcalt);

  public EstadisticasController() {
    //this.repoCarga = RepositorioSolicitudesDeCarga.getInstance();
    //this.repoEliminacion = RepositorioSolicitudesEliminacion.getInstance();
  }

  // --- Mostrar Estadisticas ---
  public static Map<String, Object> mostrarSpam(Context ctx) throws IOException {

    EstadisticaCantidadSpam estadisticaSpam = new EstadisticaCantidadSpam();
    estadisticaSpam.calcularEstadistica();
    estadisticaSpam.exportarEstadistica("estadisticas_cantidad_spam.csv");

    var cantSpam =  estadisticaSpam.getCantidadSpam();
    var fechaAhora = LocalDateTime.now();

    Map<String, Object> model = Map.of(
        "cantidadSpam", cantSpam,
        "fecha",fechaAhora
    );

    ctx.render("dashboard/estadisticaSpam.hbs", model);
    return model;
  }

  public static Map<String, Object> mostrarHoraPico(Context ctx) throws IOException {


    EstadisticaHoraHechosCategoria estadisticaHHC = new EstadisticaHoraHechosCategoria();
    estadisticaHHC.calcularEstadistica();

    estadisticaHHC.exportarEstadistica("estadisticas_categoria_horaspico.csv");

    var reporte =  estadisticaHHC.getReporte();
    var fechaAhora = LocalDateTime.now();

    Map<String, Object> model = Map.of(
        "fecha", fechaAhora,
        "reporte",reporte
    );

    ctx.render("dashboard/estadisticaHoraPico.hbs", model);
    return model;
  }

  public static Map<String, Object> mostrarCategoriaMaxima(Context ctx) throws IOException {


    EstadisticaCategoriaMaxima estadisticaCM = new EstadisticaCategoriaMaxima();
    estadisticaCM.calcularEstadistica();

    estadisticaCM.exportarEstadistica("estadisticas_categoria_horaspico.csv");

    var reporte =  estadisticaCM.getReporte();
    var fechaAhora = LocalDateTime.now();

    Map<String, Object> model = Map.of(
        "fecha", fechaAhora,
        "reporte",reporte
    );
    ctx.render("dashboard/estadisticaCategoriaMaxima.hbs", model);
    return model;
  }

  public static Map<String, Object> mostrarCategoriaProvinciaMaxHechos(Context ctx) throws IOException {

    EstadisticaProvMaxHechosCategoria estadisticaPMHCat = new EstadisticaProvMaxHechosCategoria();
    estadisticaPMHCat.calcularEstadistica();

    estadisticaPMHCat.exportarEstadistica("estadisticas_categoria_hechosmaximos.csv");

    var reporte =  estadisticaPMHCat.getReporte();
    var fechaAhora = LocalDateTime.now();

    Map<String, Object> model = Map.of(
        "fecha", fechaAhora,
        "reporte",reporte
    );
    ctx.render("dashboard/estadisticaCategoriaProvinciaMax.hbs", model);
    return model;
  }

  public static Map<String, Object> mostrarColeccionProvinciaMaxHechos(Context ctx) throws IOException {

    EstadisticaProvMaxHechosColeccion estadisticaPMHCol = new EstadisticaProvMaxHechosColeccion();
    estadisticaPMHCol.calcularEstadistica();

    estadisticaPMHCol.exportarEstadistica("estadisticas_coleccion_hechosmaximos.csv");

    var reporte =  estadisticaPMHCol.getReporte();
    var fechaAhora = LocalDateTime.now();

    Map<String, Object> model = Map.of(
        "fecha", fechaAhora,
        "reporte",reporte
    );
    ctx.render("dashboard/estadisticaColeccionProvinciaMax.hbs", model);
    return model;
  }


}
