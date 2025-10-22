package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.entities.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.model.entities.solicitudes.SolicitudDeCarga;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class RepositorioSolicitudesDeCarga implements WithSimplePersistenceUnit {
  static RepositorioSolicitudesDeCarga instance = new RepositorioSolicitudesDeCarga();

  public static RepositorioSolicitudesDeCarga getInstance() {
    return instance;
  }

  public void registrar(SolicitudDeCarga solicitud) {
    entityManager().persist(solicitud);
  }

  public List<SolicitudDeCarga> obtenerPendientesDeCarga() {
    return entityManager()
        .createQuery("from SolicitudDeCarga s where s.estado = :estado", SolicitudDeCarga.class)
        .setParameter("estado", EstadoSolicitud.PENDIENTE)
        .getResultList();
  }

  public List<SolicitudDeCarga> obtenerAceptadas() {
    return entityManager()
        .createQuery("from SolicitudDeCarga s where s.estado = :estado", SolicitudDeCarga.class)
        .setParameter("estado", EstadoSolicitud.ACEPTADA)
        .getResultList();
  }

  public List<SolicitudDeCarga> obtenerTodas() {
    return entityManager()
        .createQuery("from SolicitudDeCarga s", SolicitudDeCarga.class)
        .getResultList();
  }

}
