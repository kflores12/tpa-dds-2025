package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hechos")
public class Hecho {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
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
  private LocalDate fechaDeCarga;
  @Enumerated(EnumType.STRING)
  private TipoFuente origen;
  @Column
  private String multimedia;
  @Column
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

  public Hecho() {
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

  public Long getId() {
    return id;
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

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public void setLatitud(Double latitud) {
    this.latitud = latitud;
  }

  public void setLongitud(Double longitud) {
    this.longitud = longitud;
  }

  //solo para testear
  public void setId(Long id) {
    this.id = id;
  }

  public void setFechaAcontecimiento(LocalDate fechaAcontecimiento) {
    this.fechaAcontecimiento = fechaAcontecimiento;
  }

  public void setFechaDeCarga(LocalDate fechaDeCarga) {
    this.fechaDeCarga = fechaDeCarga;
  }

  public void setOrigen(TipoFuente origen) {
    this.origen = origen;
  }

  public void setMultimedia(String multimedia) {
    this.multimedia = multimedia;
  }

}
