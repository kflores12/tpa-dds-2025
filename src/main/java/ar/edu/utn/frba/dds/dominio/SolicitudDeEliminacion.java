package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;


public class SolicitudDeEliminacion implements Solicitud {
  private Hecho hecho;
  private String motivo;
  private EstadoSolicitud estado;
  private String evaluador;
  private String motivoApelacion;

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

  public void setEstado(EstadoSolicitud estado) {
    this.estado = estado;
  }

  public String getMotivo() {
    return motivo;
  }

  public String getEvaluador() {
    return evaluador;
  }

  public String getMotivoApelacion() {
    return motivoApelacion;
  }

  public Hecho getHecho() {
    return new Hecho(hecho);
  }

  @Override
  public void cambiarEstado(EstadoSolicitud evaluacion, String evaluador) {
    if (!estado.equals(EstadoSolicitud.PENDIENTE)) {
      throw new IllegalStateException("La solicitud ya fue evaluada.");
    }
    this.estado = evaluacion;
    this.evaluador = evaluador;
    if (estado.equals(EstadoSolicitud.ACEPTADA)) {
      hecho.setDisponibilidad(Boolean.FALSE);
    }
  }

  public void apelar(String motivoApelacion) {
    if (!estado.equals(EstadoSolicitud.RECHAZADA)) {
      throw new IllegalStateException("Solo se puede apelar una solicitud rechazada.");
    }
    if (motivoApelacion == null || motivoApelacion.isBlank()) {
      throw new IllegalArgumentException("Debe indicar un motivo de apelación.");
    }
    if (motivoApelacion.length() > 500) {
      throw new RuntimeException("El motivo es demasiado extenso.");
    }
    this.estado = EstadoSolicitud.PENDIENTE;
    this.motivoApelacion = motivoApelacion;
  }


}
