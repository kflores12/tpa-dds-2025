package ar.edu.utn.frba.dds.dominio;

public class SolicitudAceptada implements EstadoSolicitud{
  @Override
  public void establecerDisponibilidadHecho(Hecho hecho) {
    hecho.setDisponibilidad(Boolean.FALSE);
  }
}
