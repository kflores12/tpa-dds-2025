package ar.edu.utn.frba.dds.controllers;


import ar.edu.utn.frba.dds.repositories.RepositorioSolicitudesDeCarga;
import ar.edu.utn.frba.dds.repositories.RepositorioSolicitudesEliminacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.util.Map;

public class GestionSolicitudesController implements WithSimplePersistenceUnit {

  private RepositorioSolicitudesDeCarga repoCarga;
  private RepositorioSolicitudesEliminacion repoEliminacion;

  public GestionSolicitudesController() {
    this.repoCarga = RepositorioSolicitudesDeCarga.getInstance();
    this.repoEliminacion = RepositorioSolicitudesEliminacion.getInstance();
  }

  public Map<String, Object> mostrarSolicitudes(Context ctx) {

    var pendientesCarga = repoCarga.obtenerPendientesDeCarga();
    var pendientesEliminacion = repoEliminacion.obtenerPendientesDeEliminacion();


    Map<String, Object> model = Map.of(
        "solicitudesCarga", pendientesCarga,
        "solicitudesEliminacion", pendientesEliminacion
    );

    ctx.render("gestion-solicitudes.hbs", model);
    return model;
  }

}