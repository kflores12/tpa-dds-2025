package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

public class SolicitudDeEliminacion {
  private Hecho hecho;
  private String motivo;
  private EstadoSolicitud estado;

  public void setEstado(EstadoSolicitud estado) {
    this.estado = estado;
  }

  public SolicitudDeEliminacion(Hecho hecho, String motivo) {
    this.hecho = requireNonNull(hecho);
    if (motivo.length() > 500) {
      throw new RuntimeException("El motivo es demasiado extenso.");
    }
    this.motivo = requireNonNull(motivo);
    this.estado = EstadoSolicitud.PENDIENTE;
    RepositorioSolicitudes.agregarSolicitud(this);
  }

  public EstadoSolicitud getEstado() {
    return estado;
  }

  public void evaluarSolicitud(EstadoSolicitud evaluacion) {
    if (!estado.equals(EstadoSolicitud.PENDIENTE)) {
      throw new IllegalStateException("La solicitud ya fue evaluada.");
    }
    this.estado = evaluacion;
    if (estado.equals(EstadoSolicitud.ACETADA)) {
      hecho.setDisponibilidad(Boolean.FALSE);
    }
  }

}
