package ar.edu.utn.frba.dds.dominio;

import java.util.List;

public class Visualizador {

  // hechosExtraidos = new manejoDeHechos();

  public List<Hecho> visualizarHechos(Filtro filtro, Coleccion coleccion) {
    return coleccion.visualizarHechos(filtro);
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

//Visualizador (Poder obtener los hechos y filtrarlos.)
//Los Filtros.
//Como hacer que cada uno visualice.

//Como modelamos las Categorias (Strings Admin).
//Solicitud de eliminacion del contribuyente y Administrador


//Colleccion "De hechos no visible"




//Interfaz con los metodos para navegar hechos y solicitar la eliminacion de un hecho
// (esta serviria para visualizador)
//Segunda interfaz que haga extends de la anterior y agregue el metodo subirHecho
// (esta serviria para contribuyente y administrador)