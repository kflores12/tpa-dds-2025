package ar.edu.utn.frba.dds.model.entities.solicitudes;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.model.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.entities.fuentes.TipoFuente;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SolicitudDeCarga {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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
  private LocalDateTime fechaAcontecimiento;
  @Column
  private LocalDateTime fechaCargaOriginal;
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
  @ManyToOne
  @JoinColumn(name = "fuente_id")
  private Fuente fuente;

  public SolicitudDeCarga(String titulo,
                          String descripcion,
                          String categoria,
                          Double latitud,
                          Double longitud,
                          LocalDateTime fechaAcontecimiento,
                          String multimedia,
                          boolean registerBoolean,
                          Fuente fuente) {
    this.hechoCreado = null;
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.latitud = latitud;
    this.longitud = longitud;
    this.fechaAcontecimiento = fechaAcontecimiento;
    this.origen = TipoFuente.DINAMICA;
    this.multimedia = multimedia;
    this.registrado = registerBoolean;
    this.fuente = fuente;
  }

  public SolicitudDeCarga() {
  }

  public String getTitulo() {
    return titulo;
  }

  public String getCategoria() {
    return categoria;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public Double getLatitud() {
    return latitud;
  }

  public Double getLongitud() {
    return longitud;
  }

  public LocalDateTime getFechaAcontecimiento() {
    return fechaAcontecimiento;
  }

  public LocalDateTime getFechaCargaOriginal() {
    return fechaCargaOriginal;
  }

  public TipoFuente getOrigen() {
    return origen;
  }

  public String getMultimedia() {
    return multimedia;
  }

  public Boolean getDisponibilidad() {
    return disponibilidad;
  }

  public boolean isRegistrado() {
    return registrado;
  }

  public Fuente getFuente() {
    return fuente;
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

  public Hecho getHechoCreado() {
    return hechoCreado;
  }

  public boolean esRegistrado() {
    return registrado;
  }

  public void aprobar() {
    if (estado.equals(EstadoSolicitud.ACEPTADA)) {
      throw new IllegalStateException("La solicitud ya fue evaluada.");
    }

    this.fechaCargaOriginal = LocalDateTime.now();
    this.estado = EstadoSolicitud.ACEPTADA;

    this.hechoCreado = new Hecho(
        this.titulo,
        this.descripcion,
        this.categoria,
        this.latitud,
        this.longitud,
        this.fechaAcontecimiento,
        fechaCargaOriginal,
        this.origen,
        this.multimedia,
        this.disponibilidad,
        this.fuente
    );

  }

  public void rechazar() {
    this.estado = EstadoSolicitud.RECHAZADA;
  }

  public void sugerir(String sugerencia) {
    this.sugerencia = sugerencia;
  }

  public void cambiarEstado(EstadoSolicitud evaluacion) {
  }

  public Long getId() {
    return id;
  }

  public boolean puedeModificar(Hecho h) {
    if (estado.equals(EstadoSolicitud.ACEPTADA) && registrado
        && (ChronoUnit.DAYS.between(hechoCreado.getFechaDeCarga(), LocalDateTime.now())) <= 7) {
      return true;
    } else {
      return false;
    } //no hace falta el if
  }


  public void modificarHecho(Hecho hechoModificado) {
    if (!puedeModificar(hechoModificado)) {
      throw new RuntimeException("No se puede modificar este hecho");
    }
    // si pasa la validación, actualizo la referencia
    this.hechoCreado.setTitulo(hechoModificado.getTitulo());
    this.hechoCreado.setDescripcion(hechoModificado.getDescripcion());
    this.hechoCreado.setCategoria(hechoModificado.getCategoria());
    this.hechoCreado.setLatitud(hechoModificado.getLatitud());
    this.hechoCreado.setLongitud(hechoModificado.getLongitud());
    this.hechoCreado.setFechaAcontecimiento(hechoModificado.getFechaAcontecimiento());
    this.hechoCreado.setOrigen(hechoModificado.getOrigen());
    this.hechoCreado.setMultimedia(hechoModificado.getMultimedia());
    this.hechoCreado.setFechaDeCarga(hechoModificado.getFechaDeCarga());
    this.hechoCreado.setFuenteOrigen(hechoModificado.getFuenteOrigen());
  }


  public void setFechaCargaOriginal(LocalDateTime fechaCargaOriginal) {
    this.fechaCargaOriginal = fechaCargaOriginal;
  }

  @Override
  public String toString() {
    return "SolicitudDeCarga{" +
        "id=" + id +
        ", hechoCreado=" + hechoCreado +
        ", titulo='" + titulo + '\'' +
        ", descripcion='" + descripcion + '\'' +
        ", categoria='" + categoria + '\'' +
        ", latitud=" + latitud +
        ", longitud=" + longitud +
        ", fechaAcontecimiento=" + fechaAcontecimiento +
        ", fechaCargaOriginal=" + fechaCargaOriginal +
        ", origen=" + origen +
        ", multimedia='" + multimedia + '\'' +
        ", disponibilidad=" + disponibilidad +
        ", registrado=" + registrado +
        ", sugerencia='" + sugerencia + '\'' +
        ", estado=" + estado +
        ", fuente=" + fuente +
        '}';
  }
}
