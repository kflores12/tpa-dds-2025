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
  private List<Hecho> listaHechos;

  public Coleccion(String titulo, String descripcion, Fuente fuente,
                   List<Criterio> criterioPertenencia, List<Hecho> listaHechos) {
    this.titulo = requireNonNull(titulo);
    this.descripcion = requireNonNull(descripcion);
    this.fuente = requireNonNull(fuente);
    List<Criterio> criterios = new ArrayList<>(requireNonNull(criterioPertenencia));
    if (criterios.isEmpty()) {
      criterios.add(new CriterioBase());
    } else {
      criterios.removeIf(c -> c instanceof CriterioBase);
    }
    this.criterioPertenencia = criterios;
    this.listaHechos = new ArrayList<>(requireNonNull(listaHechos));
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setListaHechos(List<Hecho> listaHechos) {
    this.listaHechos = new ArrayList<>(requireNonNull(listaHechos));
  }

  public List<Hecho> getListaHechos() {
    return new ArrayList<>(listaHechos);
  }

  public List<Hecho> obtenerHechos() {
    this.setListaHechos(fuente.importarHechos(criterioPertenencia));
    return this.getListaHechos();
  }

  public List<Hecho> listarHechos() {
    List<Hecho> hechos =  new ArrayList<>(listaHechos);
    return hechos.stream().filter(Hecho::getDisponibilidad).toList();
  }
}
