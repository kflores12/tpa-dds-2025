package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Contribuyente extends Visualizador implements CargaHecho {
  private String nombre;
  private String apellido;
  private Integer edad;

  public Contribuyente() {
    //para contribuyente anonimo
  }

  public Contribuyente(String nombre, String apellido, Integer edad) {
    this.nombre = requireNonNull(nombre,
        "El contribuyente registrado debe ingresa su nombre");
    this.apellido = apellido;
    this.edad = edad;
  }

  public boolean esContribuyenteAnonimo() {
    return nombre == null && apellido == null && edad == null;
  }

  @Override
  public void cargarHecho(Hecho hecho, Coleccion coleccion) {

  }
}
