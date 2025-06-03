package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

public class FactorySolicitudDeEliminacion {
  private final DetectorDeSpam detector;

  public FactorySolicitudDeEliminacion(DetectorDeSpam detector) {
    this.detector = requireNonNull(detector);
  }

  public SolicitudDeEliminacion crear(Hecho hecho, String motivo) {
    EstadoSolicitud estadoInicial;

    if (detector.esSpam(motivo)) {
      estadoInicial = EstadoSolicitud.RECHAZADA;
    } else {
      estadoInicial = EstadoSolicitud.PENDIENTE;
    }

    return new SolicitudDeEliminacion(hecho, motivo, estadoInicial);
  }
}
