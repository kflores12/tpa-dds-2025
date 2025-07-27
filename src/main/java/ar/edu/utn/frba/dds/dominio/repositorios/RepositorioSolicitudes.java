package ar.edu.utn.frba.dds.dominio.repositorios;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.dominio.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudDeCarga;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudDeEliminacion;
import java.util.ArrayList;
import java.util.List;


public class RepositorioSolicitudes {
  private final List<SolicitudDeEliminacion> solicitudesEliminacion = new ArrayList<>();
  private final List<SolicitudDeCarga> solicitudesCarga = new ArrayList<>();
  
  public void agregarSolicitudDeEliminacion(SolicitudDeEliminacion solicitud) {
    solicitudesEliminacion.add(requireNonNull(solicitud));
  }

  public void agregarSolicitudDeCarga(SolicitudDeCarga solicitud) {
    solicitudesCarga.add(requireNonNull(solicitud));
  }

  public List<SolicitudDeEliminacion> obtenerTodasDeEliminacion() {
    return new ArrayList<>(solicitudesEliminacion);
  }

  public List<SolicitudDeCarga> obtenerTodasDeCarga() {
    return new ArrayList<>(solicitudesCarga);
  }

  public List<SolicitudDeEliminacion> obtenerPendientes() {
    return obtenerTodasDeEliminacion().stream()
        .filter(solicitud ->
            solicitud.getEstado().equals(EstadoSolicitud.PENDIENTE))
        .toList();
  }

  public List<SolicitudDeCarga> obtenerPendientesDeCarga() {
    return obtenerTodasDeCarga().stream()
        .filter(solicitud ->
            solicitud.getEstado().equals(EstadoSolicitud.PENDIENTE))
        .toList();
  }

  public List<SolicitudDeEliminacion> obtenerAtendidas() {
    return obtenerTodasDeEliminacion().stream()
        .filter(s -> !s.getEstado().equals(EstadoSolicitud.PENDIENTE))
        .toList();
  }

  public List<SolicitudDeCarga> obtenerAtendidasDeCarga() {
    return obtenerTodasDeCarga().stream()
        .filter(s -> !s.getEstado().equals(EstadoSolicitud.PENDIENTE))
        .toList();
  }

  public void limpiarListas() {
    solicitudesEliminacion.clear();
    solicitudesCarga.clear();
  }
}
