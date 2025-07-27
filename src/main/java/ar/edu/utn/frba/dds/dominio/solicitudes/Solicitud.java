package ar.edu.utn.frba.dds.dominio.solicitudes;

public interface Solicitud {
  public void cambiarEstado(EstadoSolicitud evaluacion, String evaluador);
}
