package ar.edu.utn.frba.dds;

import java.util.List;

public class Coleccion {
  public String titulo;
  public String descripcion;
  public String fuenteDeHechos;
  public String criterioDePertenencia;
  public List<Hecho> hechos;

  public Coleccion(String titulo, String descripcion, String fuenteDeHechos, String criterioDePertenencia, List<Hecho> hechos) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.fuenteDeHechos = fuenteDeHechos;
    this.criterioDePertenencia = criterioDePertenencia;
    this.hechos = hechos;
  }
}
