package ar.edu.utn.frba.dds.dominio;

import static ar.edu.utn.frba.dds.dominio.estadistica.LocalizadorDeProvincias.getProvincia;
import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;


@Entity
@Table(name = "hechos")
@Indexed
public class Hecho {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column
  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
  private String titulo;
  @Column
  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
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
  private LocalDateTime fechaDeCarga;
  @Enumerated(EnumType.STRING)
  private TipoFuente origen;
  @Column
  private String multimedia;
  @Column
  private Boolean disponibilidad = Boolean.TRUE;
  @Column
  private String provincia;


  public Hecho(String titulo, String descripcion, String categoria, Double latitud,
               Double longitud, LocalDateTime fechaAcontecimiento, LocalDateTime fechaDeCarga,
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
    this.provincia = getProvincia(this.latitud, this.longitud);
  }

  public Hecho() {
  }

  //constructor para crear hecho en la solicitud de elminiacion
  public Hecho(Hecho otro) {
    this.id = otro.getId();
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
    this.provincia = otro.provincia;
  }

  public Long getId() {
    return id;
  }

  public boolean esIgual(Hecho otro) {
    return Objects.equals(this.titulo, otro.titulo)
        && Objects.equals(this.descripcion, otro.descripcion)
        && Objects.equals(this.categoria, otro.categoria)
        && Objects.equals(this.latitud, otro.latitud)
        && Objects.equals(this.longitud, otro.longitud)
        && Objects.equals(this.provincia, otro.provincia)
        && Objects.equals(this.fechaAcontecimiento, otro.fechaAcontecimiento);
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

  public LocalDateTime getFechaAcontecimiento() {
    return fechaAcontecimiento;
  }

  public Double getLongitud() {
    return longitud;
  }

  public LocalDateTime getFechaDeCarga() {
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

  public void setFechaAcontecimiento(LocalDateTime fechaAcontecimiento) {
    this.fechaAcontecimiento = fechaAcontecimiento;
  }

  public void setFechaDeCarga(LocalDateTime fechaDeCarga) {
    this.fechaDeCarga = fechaDeCarga;
  }

  public void setOrigen(TipoFuente origen) {
    this.origen = origen;
  }

  public void setMultimedia(String multimedia) {
    this.multimedia = multimedia;
  }

}
