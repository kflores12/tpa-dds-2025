package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Coleccion {
  private String titulo;
  private String descripcion;
  private Fuente fuente;
  private List<Criterio> criterioPertenencia;

  public Coleccion(String titulo, String descripcion, Fuente fuente,
                   List<Criterio> criterioPertenencia) {
    this.titulo = requireNonNull(titulo);
    this.descripcion = requireNonNull(descripcion);
    this.fuente = requireNonNull(fuente);
    this.criterioPertenencia = criterioPertenencia;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public List<Hecho> obtenerTodosLosHechos() {
    return fuente.importarHechos(criterioPertenencia);
  }

  public List<Hecho> listarHechosDisponibles() {
    return this.obtenerTodosLosHechos().stream().filter(Hecho::getDisponibilidad).toList();
  }
}
