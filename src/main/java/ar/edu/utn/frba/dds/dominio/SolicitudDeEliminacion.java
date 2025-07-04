package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;


public class SolicitudDeEliminacion implements Solicitud {
  private Hecho hecho;
  private String motivo;
  private EstadoSolicitud estado;

  public void setEstado(EstadoSolicitud estado) {
    this.estado = estado;
  }

  public SolicitudDeEliminacion(Hecho hecho, String motivo, EstadoSolicitud estado) {
    if (motivo.length() > 500) {
      throw new RuntimeException("El motivo es demasiado extenso.");
    }
    this.hecho = new Hecho(hecho);
    this.motivo = requireNonNull(motivo);
    this.estado = requireNonNull(estado);
  }

  public EstadoSolicitud getEstado() {
    return estado;
  }

  public String getMotivo() {
    return motivo;
  }


  @Override
  public void cambiarEstado(EstadoSolicitud evaluacion) {
    if (!estado.equals(EstadoSolicitud.PENDIENTE)) {
      throw new IllegalStateException("La solicitud ya fue evaluada.");
    }
    this.estado = evaluacion;
    if (estado.equals(EstadoSolicitud.ACEPTADA)) {
      hecho.setDisponibilidad(Boolean.FALSE);
    }
  }

}
