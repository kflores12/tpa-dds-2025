package ar.edu.utn.frba.dds.dominio;

public interface Solicitud {
  public void cambiarEstado(EstadoSolicitud evaluacion, String evaluador);
}
