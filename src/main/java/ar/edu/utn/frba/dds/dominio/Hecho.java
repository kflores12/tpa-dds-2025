package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

public class Hecho {
  private String titulo;
  private String descripcion;
  private String categoria;
  private Double latitud;
  private Double longitud;
  private LocalDate fechaAcontecimiento;
  private LocalDate fechaDeCarga;
  private TipoFuente origen;
  private Contribuyente contribuyente;
  private TipoDeHecho tipoDeHecho;
  private Boolean disponibilidad = Boolean.TRUE;

  public Hecho(String titulo, String descripcion, String categoria, Double latitud,
               Double longitud, LocalDate fechaAcontecimiento, LocalDate fechaDeCarga,
               TipoFuente origen, Contribuyente contribuyente, TipoDeHecho tipoDeHecho,
               Boolean disponibilidad) {
    this.titulo = requireNonNull(titulo);
    this.descripcion = requireNonNull(descripcion);
    this.categoria = requireNonNull(categoria);
    this.latitud = requireNonNull(latitud);
    this.longitud = requireNonNull(longitud);
    this.fechaAcontecimiento = requireNonNull(fechaAcontecimiento);
    this.fechaDeCarga = requireNonNull(fechaDeCarga);
    this.origen = requireNonNull(origen);
    this.contribuyente = contribuyente;
    this.tipoDeHecho = requireNonNull(tipoDeHecho);
    this.disponibilidad = requireNonNull(disponibilidad);
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public String getCategoria() {
    return categoria;
  }

  public Double getLatitud() {
    return latitud;
  }

  public LocalDate getFechaAcontecimiento() {
    return fechaAcontecimiento;
  }

  public Double getLongitud() {
    return longitud;
  }

  public LocalDate getFechaDeCarga() {
    return fechaDeCarga;
  }

  public TipoFuente getOrigen() {
    return origen;
  }

  public Contribuyente getContribuyente() {
    return contribuyente;
  }

  public TipoDeHecho getTipoDeHecho() {
    return tipoDeHecho;
  }

  public Boolean getDisponibilidad() {
    return disponibilidad;
  }

  public void setDisponibilidad(Boolean disponibilidad) {
    this.disponibilidad = disponibilidad;
  }
}
