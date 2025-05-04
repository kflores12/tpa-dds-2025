package ar.edu.utn.frba.dds.dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Visualizador {

  public void visualizarHecho() {
   //deberiamos pensar como los busca y los filtra
  }

  //suponemos que uno visualizador no va a filtrar por tipo de fuente de carga
  //pero se podria hacer agregando el parametro.
  //podemos suponer que algunos de estos filtros pueden quedar en null si no desea
  //filtrar por eso
  /*
  public void visualizarHechos(String titulo, String descripcion,
                               Double latitud, Double longitud,
                               LocalDate fechaCarga, LocalDate fechaAcontecimiento) {
    List<Coleccion> colecciones = RegistroDeColecciones.getColeccionesDisponibles();
    List<Hecho> hechosFiltrados = new ArrayList<>();
    //que pueda ver todos los hechos
    if (titulo != null) {
      List<Hecho> aux =
          colecciones.stream()
              .flatMap(cole -> cole.getListaHechos().stream()) // unificamos todas las listas
              .filter(hecho -> hecho.getTitulo().equals(titulo)) // filtramos por título
              .toList();
      hechosFiltrados = aux;
      aux = null;
    }

    // lo mismo para todos los otros filtros


  }
   */

}
