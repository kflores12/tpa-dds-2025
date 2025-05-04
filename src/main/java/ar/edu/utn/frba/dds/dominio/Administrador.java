package ar.edu.utn.frba.dds.dominio;

import java.util.ArrayList;
import java.util.List;

public class Administrador extends Visualizador {

  //private List<SolicitudDeEliminacion> solicitudesPrndientes;

  public void traerColeccionDesdeDataSet(Categoria categoria,
                                         String titulo, String descripcion,
                                         List<Hecho> listaHechos,
                                         String fuente) {

    CargaDataset data = new CargaDataset();
    List<Hecho> todosLosHechos;
    List<Hecho> filtrados = new ArrayList<>();

    try {
      todosLosHechos = data.cargarHechosDesdeCsv(fuente);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }


    for (Hecho h : todosLosHechos) {
      if (h.getCategoria() == categoria) {
        filtrados.add(h);
      }
    }

    Coleccion nuevaColeccion = new Coleccion(titulo, descripcion, Fuente.DATASET,
        categoria, filtrados, fuente);

    RegistroDeColecciones.agregarColeccion(nuevaColeccion);

    System.out.println("Se extrajeron corrctamente los dados desde el archivo CSV");

  }


}
