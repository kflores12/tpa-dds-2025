package ar.edu.utn.frba.dds.dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioSolicitudes {
  private static List<SolicitudDeEliminacion> solicitudes = new ArrayList<>();

  public void agregarSolicitud(SolicitudDeEliminacion solicitud) {
    solicitudes.add(solicitud);
  }

  public List<SolicitudDeEliminacion> obtenerSolicitudesPendientes() {
    return solicitudes.stream().filter(solicitud
        -> solicitud.esSolicitudPendiente()).collect(Collectors.toList());
  }

}
