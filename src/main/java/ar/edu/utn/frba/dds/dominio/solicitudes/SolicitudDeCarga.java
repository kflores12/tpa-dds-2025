package ar.edu.utn.frba.dds.dominio.solicitudes;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioHechos;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Entity;
import javax.persistence.Transient;


@Entity
@DiscriminatorValue("CARGA")
public class SolicitudDeCarga extends Solicitud {
  @Transient
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

  @Transient
  private RepositorioHechos repositorioH;

  public SolicitudDeCarga(String titulo,
                          String descripcion,
                          String categoria,
                          Double latitud,
                          Double longitud,
                          LocalDate fechaAcontecimiento,
                          String multimedia,
                          boolean registerBoolean,
                          RepositorioHechos rh) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.latitud = latitud;
    this.longitud = longitud;
    this.fechaAcontecimiento = fechaAcontecimiento;
    this.origen = TipoFuente.DINAMICA;
    this.multimedia = multimedia;
    this.registrado = registerBoolean;
    this.repositorioH = rh;
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

  public void aprobar() {
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

      repositorioH.cargarHecho(hechoCreado);
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

  public boolean puedeModificar() {
    if (estado.equals(EstadoSolicitud.ACEPTADA) && registrado
        && (ChronoUnit.DAYS.between(hechoCreado.getFechaDeCarga(), LocalDate.now())) <= 7) {
      return true;
    } else {
      throw new RuntimeException("No se puede modificar este hecho");
    }
  }

  public void modificarHecho(Hecho h) {
    if (puedeModificar()) {
      if (!h.getId().equals(this.hechoCreado.getId())) {
        throw new IllegalArgumentException("El hecho modificado no corresponde al registrado");
      }

      this.hechoCreado = repositorioH.modificarHecho(h);
    } else {
      throw new RuntimeException("No se puede modificar este hecho");
    }
  }


  /*
  public void modificarHecho(Hecho h) {
    if (puedeModificar()) {
      repositorioH.modificarHecho(h);
      this.hechoCreado = h; //EVALUAR SI ESTO ES CORRECTO.
      //(ENTIENDO QUE HAY QUE ACTUALIZAR LA REFERENCIA QUE SE TIENE DEL HECHO UNA VEZ MODIFICADO)
      //EN to do CASO SE PUEDE VOLVER A BUSCAR A LA DATABASE.
    }
    else {
      throw new RuntimeException("No se puede modificar este hecho");
    }
    //repensar este metodo
    //Hecho original = encontrarHecho();
    //original.modificar(h);
  }

   */

  public void setFechaCargaOriginal (LocalDate fechaCargaOriginal) {
    this.fechaCargaOriginal = fechaCargaOriginal;
  }

}
