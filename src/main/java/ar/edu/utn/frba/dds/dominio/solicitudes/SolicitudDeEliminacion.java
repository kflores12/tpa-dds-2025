package ar.edu.utn.frba.dds.dominio.solicitudes;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.dominio.Hecho;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;


@Entity
public class SolicitudDeEliminacion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @OneToOne
  private Hecho hecho;
  @Column
  private String motivo;
  @Enumerated(EnumType.STRING)
  private EstadoSolicitud estado;
  @Column
  private Boolean esSpam;

  public SolicitudDeEliminacion(Hecho hecho, String motivo,
      EstadoSolicitud estado,  Boolean esSpam) {
    if (motivo.length() > 500) {
      throw new RuntimeException("El motivo es demasiado extenso.");
    }
    this.hecho = new Hecho(hecho);
    this.motivo = requireNonNull(motivo);
    this.estado = requireNonNull(estado);
    this.esSpam = esSpam;
  }

  public SolicitudDeEliminacion() {
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


  public Hecho getHecho() {
    return new Hecho(hecho);
  }

  public Boolean getEsSpam() {
    return esSpam;
  }

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
