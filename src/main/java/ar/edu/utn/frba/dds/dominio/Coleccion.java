package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Coleccion {
  private final String titulo;
  private final String descripcion;
  private final Fuente fuente;
  private final List<Criterio> criterioPertenencia;
  private final String handler;

  public Coleccion(String titulo, String descripcion, Fuente fuente,
                   List<Criterio> criterioPertenencia, String handler) {
    this.titulo = requireNonNull(titulo);
    this.descripcion = requireNonNull(descripcion);
    this.fuente = requireNonNull(fuente);
    this.criterioPertenencia = new ArrayList<>(criterioPertenencia);

    if (!handler.matches("[a-zA-Z0-9]+")) {
      throw new IllegalArgumentException("El handle debe ser alfanumérico o con guiones.");
    }
    this.handler = handler;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public String getHandler() {
    return handler;
  }

  public List<Hecho> obtenerTodosLosHechos() {
    return fuente.importarHechos(criterioPertenencia);
  }

  public List<Hecho> listarHechosDisponibles() {
    return this.obtenerTodosLosHechos().stream().filter(Hecho::getDisponibilidad).toList();
  }
}
