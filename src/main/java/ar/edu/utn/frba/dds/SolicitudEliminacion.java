package ar.edu.utn.frba.dds;

public class SolicitudEliminacion {
  public Hecho hecho;
  public String motivo;
  public String estadoSolicitud;

  public SolicitudEliminacion(Hecho hecho, String motivo, String estadoSolicitud) {
    this.hecho = hecho;
    this.motivo = motivo;
    this.estadoSolicitud = estadoSolicitud;
  }
}
