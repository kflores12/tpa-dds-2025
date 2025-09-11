package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

@Entity
public class SolicitudDeCarga extends Solicitud {
  @OneToOne
  private Hecho hechoCreado;
  //ATRIBUTOS DE UN HECHO A CREAR
  @Column
  private String titulo;
  @Column
  private String descripcion;
  @Column
  private String categoria;
  @Column
  private Double latitud;
  @Column
  private Double longitud;
  @Column
  private LocalDate fechaAcontecimiento;
  @Column
  private LocalDate fechaCargaOriginal;
  @Enumerated(EnumType.STRING)
  private TipoFuente origen;
  @Column
  private String multimedia;
  @Column
  private Boolean disponibilidad = Boolean.TRUE;
  //FIN Atributos de un hecho
  @Column
  private boolean registrado;
  @Column
  private String sugerencia = "";
  @Enumerated(EnumType.STRING)
  private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

  public SolicitudDeCarga(String titulo,
                          String descripcion,
                          String categoria,
                          Double latitud,
                          Double longitud,
                          LocalDate fechaAcontecimiento,
                          String multimedia,
                          boolean registerBoolean) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.latitud = latitud;
    this.longitud = longitud;
    this.fechaAcontecimiento = fechaAcontecimiento;
    this.origen = TipoFuente.DINAMICA;
    this.multimedia = multimedia;
    this.registrado = registerBoolean;
  }

  public SolicitudDeCarga() {
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

  public Hecho aprobar() {
    System.out.printf("Terrible%n%n%n%n%n%n");

    if (estado.equals(EstadoSolicitud.ACEPTADA)) {

      throw new IllegalStateException("La solicitud ya fue evaluada.");
    } else {

      this.fechaCargaOriginal = LocalDate.now();
      this.estado = EstadoSolicitud.ACEPTADA;

      this.hechoCreado = new Hecho(this.titulo,
          this.descripcion,
          this.categoria,
          this.latitud,
          this.longitud,
          this.fechaAcontecimiento,
          fechaCargaOriginal,
          this.origen,
          this.multimedia,
          this.disponibilidad);
      //todo esto genera el error en el test
      return new Hecho(hechoCreado);
    }
  }

  public void rechazar() {
    this.estado = EstadoSolicitud.RECHAZADA;
  }

  public void sugerir(String sugerencia) {
    this.sugerencia = sugerencia;
  }

  public void cambiarEstado(EstadoSolicitud evaluacion) {
  }

  public boolean puedeModificar(Hecho h) {
    if (estado.equals(EstadoSolicitud.ACEPTADA) && registrado
        && (ChronoUnit.DAYS.between(hechoCreado.getFechaDeCarga(), LocalDate.now())) <= 7
        && (h.getId().equals(this.hechoCreado.getId()))) {
      return true;
    } else {
      throw new RuntimeException("No se puede modificar este hecho");
    }
  }

  public void modificarHecho(Hecho hechoModificado) {
    if (!puedeModificar(hechoModificado)) {
      throw new RuntimeException("No se puede modificar este hecho");
    }
    // si pasa la validación, actualizo la referencia
    this.hechoCreado = new Hecho(hechoModificado);
  }


  public void setFechaCargaOriginal(LocalDate fechaCargaOriginal) {
    this.fechaCargaOriginal = fechaCargaOriginal;
  }

}
