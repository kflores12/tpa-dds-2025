package ar.edu.utn.frba.dds.dominio.solicitudes;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioHechos;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SolicitudDeCarga implements Solicitud {
  //ATRIBUTOS DE UN HECHO A CREAR
  private String titulo;
  private String descripcion;
  private String categoria;
  private Double latitud;
  private Double longitud;
  private LocalDate fechaAcontecimiento;
  private LocalDate fechaCargaOriginal;
  private TipoFuente origen;
  private String multimedia;
  private Boolean disponibilidad = Boolean.TRUE;
  //FIN Atributos de un hecho

  private boolean registrado;
  private String sugerencia = "";
  private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;
  private RepositorioHechos repositorioH;
  private String evaluador;
  private Hecho hechoCreado;

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

  public String getEvaluador() {
    return evaluador;
  }

  //unicamente para test
  public void setFechaCargaOriginal(LocalDate fechaCargaMock) {
    this.fechaCargaOriginal = fechaCargaMock;
  }

  public void aprobar(String evaluador) {
    if (estado.equals(EstadoSolicitud.ACEPTADA)) {

      throw new IllegalStateException("La solicitud ya fue evaluada.");
    } else {

      this.fechaCargaOriginal = LocalDate.now();
      this.estado = EstadoSolicitud.ACEPTADA;
      this.evaluador = requireNonNull(evaluador);
      //cuando pensemos en la persistencia de hechos modificados
      //por trazabilidad aca podria ser guardado el hecho original
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

      repositorioH.cargarHecho(this.hechoCreado);

    }
  }

  public void rechazar(String evaluador) {
    this.estado = EstadoSolicitud.RECHAZADA;
    this.evaluador = requireNonNull(evaluador);
  }

  public void sugerir(String sugerencia) {
    this.sugerencia = sugerencia;
  }

  public void cambiarEstado(EstadoSolicitud evaluacion, String evaluador) {
  }

  public boolean puedeModificar() {
    if (estado.equals(EstadoSolicitud.ACEPTADA) && registrado
        && (ChronoUnit.DAYS.between(fechaCargaOriginal, LocalDate.now())) <= 7) {
      return true;
    } else {
      return false;
    }
  }

  public Hecho encontrarHecho() {
    if (puedeModificar()) {
      for (Hecho h : repositorioH.obtenerTodos()) {
        if (h.getTitulo().equals(this.titulo)
            && h.getDescripcion().equals(this.descripcion)
            && h.getCategoria().equals(this.categoria)
            && h.getLatitud().equals(this.latitud)
            && h.getLongitud().equals(this.longitud)
            && h.getFechaAcontecimiento().equals(this.fechaAcontecimiento)
            && h.getFechaDeCarga().equals(this.fechaCargaOriginal)
            && h.getOrigen().equals(this.origen)
            && h.getMultimedia().equals(this.multimedia)
            && h.getDisponibilidad().equals(this.disponibilidad)
        ) {
          return h;
        }
      }
    }
    throw new RuntimeException("No se puede modificar este hecho");
  }

  public void modificarHecho(Hecho h) {
    Hecho original = encontrarHecho();
    original.modificar(h);
  }


}
