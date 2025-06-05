package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioSolicitudes {
  private static final List<SolicitudDeEliminacion> solicitudesEliminacion = new ArrayList<>();
  private static final List<SolicitudDeCarga> solicitudesCarga = new ArrayList<>();
  
  public static void agregarSolicitudDeEliminacion(SolicitudDeEliminacion solicitud) {
    solicitudesEliminacion.add(requireNonNull(solicitud));
  }

  public static void agregarSolicitudDeCarga(SolicitudDeCarga solicitud) {
    solicitudesCarga.add(requireNonNull(solicitud));
  }

  public static List<SolicitudDeEliminacion> obtenerTodasDeEliminacion() {
    return new ArrayList<>(solicitudesEliminacion);
  }

  public static List<SolicitudDeCarga> obtenerTodasDeCarga() {
    return new ArrayList<>(solicitudesCarga);
  }

  public static List<SolicitudDeEliminacion> obtenerPendientes() {
    return obtenerTodasDeEliminacion().stream()
        .filter(solicitud ->
            solicitud.getEstado().equals(EstadoSolicitud.PENDIENTE))
        .toList();
  }

  public static List<SolicitudDeEliminacion> obtenerAtendidas() {
    return obtenerTodasDeEliminacion().stream()
        .filter(s -> !s.getEstado().equals(EstadoSolicitud.PENDIENTE))
        .toList();
  }
}
