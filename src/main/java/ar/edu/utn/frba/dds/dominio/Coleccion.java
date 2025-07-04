package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Coleccion implements Fuente {
  private final String titulo;
  private final String descripcion;
  private final Fuente fuente;
  private final List<Criterio> criterioPertenencia;
  private final String handler;
  private final AlgoritmoDeConsenso algoritmo;
  private final List<Hecho> hechosConsensuados = new ArrayList<Hecho>();

  public Coleccion(String titulo,
                   String descripcion,
                   Fuente fuente,
                   List<Criterio> criterioPertenencia,
                   String handler,
                   AlgoritmoDeConsenso algoritmo) {
    this.titulo = requireNonNull(titulo);
    this.descripcion = requireNonNull(descripcion);
    this.fuente = requireNonNull(fuente);
    this.criterioPertenencia = new ArrayList<>(requireNonNull(criterioPertenencia));

    if (!handler.matches("[a-zA-Z0-9]+")) {
      throw new IllegalArgumentException("El handle debe ser alfanumérico o con guiones.");
    }
    this.handler = handler;
    //if (algoritmo == null) {
    //  navegacionIrrestricta(fuente.getHechos());
    //}
    this.algoritmo = algoritmo;
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

  @Override
  public List<Hecho> getHechos() {
    List<Hecho> hechosAgregados = new ArrayList<>();
    hechosAgregados.addAll(fuente.getHechos());
    return this.filtrarPorCriteriosColeccion(hechosAgregados);
  }

  private List<Hecho> filtrarPorCriteriosColeccion(List<Hecho> hechos) {
    if (criterioPertenencia.isEmpty()) {
      return new ArrayList<>(this.filtrarDuplicados(hechos).values());
    }
    List<Hecho> filtrados = hechos.stream().filter(h -> criterioPertenencia.stream()
        .allMatch(c -> c.aplicarFiltro(h))).toList();

    return new ArrayList<>(filtrarDuplicados(filtrados).values());
  }

  public Map<String, Hecho> filtrarDuplicados(List<Hecho> duplicados) {
    Map<String, Hecho> hechosUnicos = new HashMap<>();
    for (Hecho hecho : duplicados) {
      hechosUnicos.put(hecho.getTitulo(), hecho);
    }
    return hechosUnicos;
  }

  public List<Hecho> listarHechosDisponibles(List<Criterio> criteriosUsuario) {
    List<Hecho> filtradosColeccion = this.getHechos()
        .stream()
        .filter(Hecho::getDisponibilidad)
        .toList();
    return filtradosColeccion
        .stream()
        .filter(h -> criteriosUsuario.stream()
        .allMatch(c -> c.aplicarFiltro(h)))
        .toList();
  }

  public void actualizarHechosConsensuados(List<Hecho> hechosNodo) {
    if (this.algoritmo == null) {
      navegacionIrrestricta(this.fuente.getHechos());
    } else {
      for (Hecho hecho : fuente.getHechos()) {
        if (this.algoritmo.estaConsensuado(hecho, hechosNodo)) {
          this.hechosConsensuados.add(hecho);
        }
      }
    }
  }

  private void navegacionIrrestricta(List<Hecho> hechos) {
    this.hechosConsensuados.addAll(hechos);
  }
}
