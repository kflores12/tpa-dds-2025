package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.dominio.algoritmosconcenso.AlgoritmoDeConsenso;
import ar.edu.utn.frba.dds.dominio.algoritmosconcenso.ModoNavegacion;
import ar.edu.utn.frba.dds.dominio.algoritmosconcenso.NavegacionConsensuada;
import ar.edu.utn.frba.dds.dominio.algoritmosconcenso.NavegacionIrrestricta;
import ar.edu.utn.frba.dds.dominio.criterios.Criterio;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "colecciones")
public class Coleccion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column
  private String titulo;
  @Column
  private String descripcion;
  @ManyToOne
  private Fuente fuente;
  @ManyToMany
  private List<Criterio> criteriosPertenencia;
  @Column
  private String handler;
  @ManyToOne
  private AlgoritmoDeConsenso algoritmo;
  @ManyToMany
  private List<Hecho> hechosConsensuados = new ArrayList<Hecho>();

  public Coleccion(String titulo,
                   String descripcion,
                   Fuente fuente,
                   List<Criterio> criterioPertenencia,
                   String handler,
                   AlgoritmoDeConsenso algoritmo) {
    this.titulo = requireNonNull(titulo);
    this.descripcion = requireNonNull(descripcion);
    this.fuente = requireNonNull(fuente);
    this.criteriosPertenencia = new ArrayList<>(requireNonNull(criterioPertenencia));

    if (!handler.matches("[a-zA-Z0-9]+")) {
      throw new IllegalArgumentException("El handle debe ser alfanumérico o con guiones.");
    }
    this.handler = handler;
    this.algoritmo = algoritmo;
  }

  public Coleccion() {
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

  public Long getId() {
    return id;
  }

  public Fuente getFuente() {
    return fuente;
  }

  public List<Hecho> obtnerHechos() {
    List<Hecho> hechosAgregados = new ArrayList<>();

    hechosAgregados.addAll(fuente.getHechos());

    return this.filtrarPorCriteriosColeccion(hechosAgregados);
  }

  private List<Hecho> filtrarPorCriteriosColeccion(List<Hecho> hechos) {
    if (criteriosPertenencia.isEmpty()) {
      return new ArrayList<>(this.filtrarDuplicados(hechos).values());
    }
    List<Hecho> filtrados = hechos.stream().filter(h -> criteriosPertenencia.stream()
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

  public List<Hecho> listarHechosDisponibles(List<Criterio> criteriosUsuario,
                                             ModoNavegacion modo) {
    List<Hecho> hechosNodo = modo.aplicar(this, this.algoritmo);
    List<Hecho> filtradosColeccion = hechosNodo
        .stream()
        .filter(Hecho::getDisponibilidad)
        .toList();
    return filtradosColeccion
        .stream()
        .filter(h -> criteriosUsuario.stream()
        .allMatch(c -> c.aplicarFiltro(h)))
        .toList();
  }

  public void actualizarHechosConsensuados() {

    ModoNavegacion modo;
    if (this.algoritmo == null) {
      modo = new NavegacionIrrestricta();
    } else {
      modo = new NavegacionConsensuada();
    }
    this.hechosConsensuados = modo.aplicar(this, this.algoritmo);
  }

}
