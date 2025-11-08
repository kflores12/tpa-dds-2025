package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.entities.solicitudes.SolicitudDeCarga;
import ar.edu.utn.frba.dds.model.entities.solicitudes.SolicitudDeEliminacion;
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

  public void mostrarSolicitudes(Context ctx) {
    var pendientesCarga = repoCarga.obtenerPendientesDeCarga();
    var pendientesEliminacion = repoEliminacion.obtenerPendientesDeEliminacion();
    Map<String, Object> model = Map.of(
        "solicitudesCarga", pendientesCarga,
        "solicitudesEliminacion", pendientesEliminacion
    );
    ctx.render("/dashboard/gestion-solicitudes.hbs", model);
  }

  public void aceptarSolicitudCarga(Context ctx) {
    Long id = ctx.pathParamAsClass("id", Long.class).get();
    withTransaction(() -> {
      SolicitudDeCarga solicitud = repoCarga.getSolicitud(id);
      if (solicitud != null) {
        solicitud.aprobar();
        entityManager().merge(solicitud);
      }
    });

    ctx.redirect("/dashboard/solicitudes");
  }

  public void rechazarSolicitudCarga(Context ctx) {
    Long id = ctx.pathParamAsClass("id", Long.class).get();
    withTransaction(() -> {
      SolicitudDeCarga solicitud = repoCarga.getSolicitud(id);
      if (solicitud != null) {
        solicitud.rechazar();
        entityManager().merge(solicitud);
      }
    });

    ctx.redirect("/dashboard/solicitudes");
  }

  public void aceptarSolicitudEliminacion(Context ctx) {
    Long id = ctx.pathParamAsClass("id", Long.class).get();
    withTransaction(() -> {
      SolicitudDeEliminacion solicitud = repoEliminacion.getSolicitudDeEliminacion(id);
      if (solicitud != null) {
        solicitud.aprobar();
        entityManager().merge(solicitud);
      }
    });

    ctx.redirect("/dashboard/solicitudes");
  }

  public void rechazarSolicitudEliminacion(Context ctx) {
    Long id = ctx.pathParamAsClass("id", Long.class).get();
    withTransaction(() -> {
      SolicitudDeEliminacion solicitud = repoEliminacion.getSolicitudDeEliminacion(id);
      if (solicitud != null) {
        solicitud.rechazar();
        entityManager().merge(solicitud);
      }
    });

    ctx.redirect("/dashboard/solicitudes");
  }
}