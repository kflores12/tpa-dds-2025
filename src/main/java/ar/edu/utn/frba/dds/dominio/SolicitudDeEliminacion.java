package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

public class SolicitudDeEliminacion {
  private Hecho hecho;
  private String motivo;
  private EstadoSolicitud estado = new SolicitudPendiente();

  public void setEstado(EstadoSolicitud estado) {
    this.estado = estado;
  }

  public SolicitudDeEliminacion(Hecho hecho, String motivo) {
    this.hecho = requireNonNull(hecho);
    if (motivo.length() > 500) {
      throw new RuntimeException("El motivo es demasiado extenso.");
    }
    this.motivo = requireNonNull(motivo);
    RepositorioSolicitudes.agregarSolicitud(this);
  }

  public boolean estaPendiente() {
    return estado instanceof SolicitudPendiente;
  }


  public void evaluarSolicitud(EstadoSolicitud evaluacion) {
    if (!estaPendiente()) {
      throw new IllegalStateException("La solicitud ya fue evaluada.");
    }
    evaluacion.establecerDisponibilidadHecho(hecho);
    this.estado = requireNonNull(evaluacion);
  }

}
