package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.entities.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.model.entities.solicitudes.SolicitudDeEliminacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class RepositorioSolicitudesEliminacion implements WithSimplePersistenceUnit {
  static RepositorioSolicitudesEliminacion instance = new RepositorioSolicitudesEliminacion();

  public static RepositorioSolicitudesEliminacion getInstance() {
    return instance;
  }

  public RepositorioSolicitudesEliminacion() {
  }

  public void cargarSolicitudEliminacion(SolicitudDeEliminacion solicitud) {
    entityManager().persist(solicitud);
  }


  public List<SolicitudDeEliminacion> obtenerTodas() {
    return entityManager()
        .createQuery("from SolicitudDeEliminacion", SolicitudDeEliminacion.class).getResultList();
  }

  public List<SolicitudDeEliminacion> obtenerPendientesDeEliminacion() {
    return entityManager()
        .createQuery(
            "from SolicitudDeEliminacion s where s.estado = :estado", SolicitudDeEliminacion.class
        )
        .setParameter("estado", EstadoSolicitud.PENDIENTE)
        .getResultList();
  }

  public List<SolicitudDeEliminacion> obtenerAceptadasDeEliminacion() {
    return entityManager()
        .createQuery(
            "from SolicitudDeEliminacion s where s.estado = :estado", SolicitudDeEliminacion.class
        )
        .setParameter("estado", EstadoSolicitud.ACEPTADA)
        .getResultList();
  }

  public SolicitudDeEliminacion getSolicitudDeEliminacion(Long id) {
    return entityManager().find(SolicitudDeEliminacion.class, id);
  }

}
