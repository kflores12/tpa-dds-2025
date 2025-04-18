package ar.edu.utn.frba.dds;

import java.time.LocalDate;


public class Hecho {
  public String titulo;
  public String descripcion;
  public String categoria;
  public int latitud;
  public int longitud;
  public LocalDate fechaDelHecho;
  public String origen;
  public Boolean esVisible;

  public Hecho(String titulo, String descripcion, String categoria, int latitud, int longitud, LocalDate fechaDelHecho, String origen, Boolean esVisible) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.latitud = latitud;
    this.longitud = longitud;
    this.origen = origen;
    this.fechaDelHecho = fechaDelHecho;
    this.esVisible = esVisible;
  }
}


