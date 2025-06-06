package ar.edu.utn.frba.dds.dominio;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SolicitudDeCarga implements Solicitud {
  private Hecho hecho;
  private boolean registrado;
  private String sugerencia = "";
  private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;
  private RepositorioHechos repositorioH;
  private RepositorioSolicitudes repositorioS;

  public SolicitudDeCarga(Hecho h, boolean registerBoolean,
                          RepositorioHechos rh, RepositorioSolicitudes rs) {
    this.hecho = new Hecho(h);
    this.registrado = registerBoolean;
    this.repositorioH = rh;
    this.repositorioS = rs;
    repositorioS.agregarSolicitudDeCarga(this);
  }

  public EstadoSolicitud getEstado() {
    return estado;
  }

  public String getSugerencia() {
    return sugerencia;
  }

  public void setEstado(EstadoSolicitud estado) {
    this.estado = estado;
  }

  public void setSugerencia(String s) {
    this.sugerencia = s;
  }

  public void evaluarSolicitud(EstadoSolicitud evaluacion) {
    if (!estado.equals(EstadoSolicitud.PENDIENTE)) {
      throw new IllegalStateException("La solicitud ya fue evaluada.");
    }

    this.estado = evaluacion;
    if (estado.equals(EstadoSolicitud.ACEPTADA)) {
      repositorioH.cargarHecho(hecho);
    }

  }

  public void modificarHecho(Hecho hechoModificador) {
    if (estado.equals(EstadoSolicitud.ACEPTADA) && registrado
        && (ChronoUnit.DAYS.between(hecho.getFechaDeCarga(), LocalDate.now())) <= 7) {
      hecho.modificar(hechoModificador);
    } else {
      throw new RuntimeException("No se puede modificar este hecho");
    }
  }

}
