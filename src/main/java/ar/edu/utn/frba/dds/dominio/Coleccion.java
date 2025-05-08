package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Coleccion {
  private String titulo;
  private String descripcion;
  private Fuente fuenteTipo;
  private Etiqueta criterioPertenencia;
  private List<Hecho> listaHechos;
  private String fuente;

  public Coleccion(String titulo, String descripcion, Fuente fuenteTipo,
                   Etiqueta criterioPertenencia, List<Hecho> listaHechos, String fuente) {
    this.titulo = requireNonNull(titulo);
    this.descripcion = requireNonNull(descripcion);
    this.fuenteTipo = requireNonNull(fuenteTipo);
    this.criterioPertenencia = requireNonNull(criterioPertenencia);
    this.listaHechos = new ArrayList<>(listaHechos);
    this.fuente = fuente;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public Fuente getFuenteTipo() {
    return fuenteTipo;
  }

  public String getFuente() {
    return fuente;
  }

  public void removerHecho(Hecho h) {
    listaHechos.removeIf(hecho -> hecho.equals(h));
  }

  public List<Hecho> visualizarHechos(Filtro filtro) {
    return listaHechos.stream().filter(hecho ->
        (filtro.aplicarFiltro(hecho))).collect(Collectors.toList());
  }

  public List<Hecho> visualizarMultiplesfiltros(List<Filtro> filtros) {
    return listaHechos.stream()
        .filter(hecho -> filtros.stream()
            .anyMatch(filtro -> filtro.aplicarFiltro(hecho))).toList();
  }

  public List<Hecho> getListaHechos() {
    return new ArrayList<>(listaHechos);
  }

  public Etiqueta getCriterioPertenencia() {
    return criterioPertenencia;
  }

  public void agregarHecho(Hecho h) {
    listaHechos.add(h);
  }
}
