package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioSolicitudes {
  private static final List<SolicitudDeEliminacion> solicitudesEliminacion = new ArrayList<>();
  private final List<SolicitudDeCarga> solicitudesCarga = new ArrayList<>();
  
  public static void agregarSolicitudDeEliminacion(SolicitudDeEliminacion solicitud) {
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
