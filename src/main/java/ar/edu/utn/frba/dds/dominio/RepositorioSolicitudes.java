package ar.edu.utn.frba.dds.dominio;

import java.util.ArrayList;
import java.util.List;

public class RepositorioSolicitudes {
  private List<SolicitudDeEliminacion> solicitudes = new ArrayList<>();

  public void agregarSolicitud(SolicitudDeEliminacion solicitud) {
    solicitudes.add(solicitud);
  }

  public List<SolicitudDeEliminacion> obtenerSolicitudes() {
    return new ArrayList<>(solicitudes);
  }

  public void eliminarSolicitud(SolicitudDeEliminacion solicitud) {
    solicitudes.remove(solicitud);
  }



}
