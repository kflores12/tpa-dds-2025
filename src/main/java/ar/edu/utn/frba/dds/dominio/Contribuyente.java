package ar.edu.utn.frba.dds.dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Contribuyente extends Visualizador {
  private String nombre;
  private String apellido;
  private Integer edad;

  public Contribuyente() {
    //para contribuyente anonimo
  }

  public Contribuyente(String nombre, String apellido, Integer edad) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.edad = edad;
  }

  public boolean esContribuyenteAnonimo() {
    return nombre == null && apellido == null && edad == null;

}
