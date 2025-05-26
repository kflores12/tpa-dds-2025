package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioSolicitudes {
  private static List<SolicitudDeEliminacion> solicitudes = new ArrayList<>();

  public static void agregarSolicitud(SolicitudDeEliminacion solicitud) {
    solicitudes.add(requireNonNull(solicitud));
  }

  public static List<SolicitudDeEliminacion> obtenerTodas() {
    return new ArrayList<>(solicitudes);
  }

  public static List<SolicitudDeEliminacion> obtenerPendientes() {
    return obtenerTodas().stream()
        .filter(solicitud ->
            solicitud.getEstado().equals(EstadoSolicitud.PENDIENTE))
        .toList();
  }

  public static List<SolicitudDeEliminacion> obtenerAtendidas() {
    return obtenerTodas().stream()
        .filter(s -> !s.getEstado().equals(EstadoSolicitud.PENDIENTE))
        .toList();
  }
}
