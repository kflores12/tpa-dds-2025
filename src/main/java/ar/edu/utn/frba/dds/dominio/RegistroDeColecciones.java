package ar.edu.utn.frba.dds.dominio;

import java.util.ArrayList;
import java.util.List;

public class RegistroDeColecciones {

  private static List<Coleccion> coleccionesDisponibles = new ArrayList<>();

  private static List<Hecho> hechosNodisponibles = new ArrayList<>();

  public static List<Coleccion> getColeccionesDisponibles() {
    return new ArrayList<>(coleccionesDisponibles);  // Devuelves una copia de la lista
  }

  public static List<Hecho> getHechosNodisponibles() {
    return new ArrayList<>(hechosNodisponibles);  // Devuelves una copia de la lista
  }

  public static void agregarColeccion(Coleccion unaColeccicon) {
    coleccionesDisponibles.add(unaColeccicon);
  }
}
