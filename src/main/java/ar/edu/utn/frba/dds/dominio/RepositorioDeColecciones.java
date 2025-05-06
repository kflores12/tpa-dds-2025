package ar.edu.utn.frba.dds.dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class RepositorioDeColecciones {

  private static List<Coleccion> coleccionesDisponibles = new ArrayList<>();


  private static List<Hecho> hechosNoDisponibles = new ArrayList<>();


  public static List<Coleccion> getColeccionesDisponibles() {
    return new ArrayList<>(coleccionesDisponibles);  // Devuelves una copia de la lista
  }

  public static List<Hecho> getHechosNodisponibles() {
    return new ArrayList<>(hechosNoDisponibles);  // Devuelves una copia de la lista
  }

  public static void agregarColeccion(Coleccion unaColeccicon) {
    coleccionesDisponibles.add(unaColeccicon);
  }

  public static void removeHechoColeccionesDisp(Hecho hecho) {
    coleccionesDisponibles.forEach(coleccion -> coleccion.removerHecho(hecho));
  }

  public static void addHechoNoDisp(Hecho hecho) {
    hechosNoDisponibles.add(hecho);
  }
  
}
