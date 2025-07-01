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
  private String multimedia;
  private Boolean disponibilidad = Boolean.TRUE;

  public Hecho(String titulo, String descripcion, String categoria, Double latitud,
               Double longitud, LocalDate fechaAcontecimiento, LocalDate fechaDeCarga,
               TipoFuente origen, String multimedia, Boolean disponibilidad) {
    this.titulo = requireNonNull(titulo);
    this.descripcion = requireNonNull(descripcion);
    this.categoria = requireNonNull(categoria);
    this.latitud = requireNonNull(latitud);
    this.longitud = requireNonNull(longitud);
    this.fechaAcontecimiento = requireNonNull(fechaAcontecimiento);
    this.fechaDeCarga = requireNonNull(fechaDeCarga);
    this.origen = requireNonNull(origen);
    this.multimedia = multimedia;
    this.disponibilidad = requireNonNull(disponibilidad);
  }

  //constructor para crear hecho en la solicitud de elminiacion
  public Hecho(Hecho otro) {
    this.titulo = otro.titulo;
    this.descripcion = otro.descripcion;
    this.categoria = otro.categoria;
    this.latitud = otro.latitud;
    this.longitud = otro.longitud;
    this.fechaAcontecimiento = otro.fechaAcontecimiento;
    this.fechaDeCarga = otro.fechaDeCarga;
    this.origen = otro.origen;
    this.multimedia = otro.multimedia;
    this.disponibilidad = otro.disponibilidad;
  }

  public void modificar(Hecho otro) {
    this.titulo = otro.titulo;
    this.descripcion = otro.descripcion;
    this.categoria = otro.categoria;
    this.latitud = otro.latitud;
    this.longitud = otro.longitud;
    this.fechaAcontecimiento = otro.fechaAcontecimiento;
    //this.fechaDeCarga = otro.fechaDeCarga;
    this.origen = otro.origen;
    this.multimedia = otro.multimedia;
    this.disponibilidad = otro.disponibilidad;
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

  public String getMultimedia() {
    return multimedia;
  }

  public Boolean getDisponibilidad() {
    return disponibilidad;
  }

  public TipoFuente getFuente() {
    return origen;
  }

  public void setDisponibilidad(Boolean disponibilidad) {
    this.disponibilidad = disponibilidad;
  }

}
