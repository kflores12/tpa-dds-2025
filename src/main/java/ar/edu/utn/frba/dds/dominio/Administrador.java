package ar.edu.utn.frba.dds.dominio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Administrador extends Visualizador implements CargaHecho {


  public Coleccion crearNuevaColeccion(ColeccionBuilder borrador) {
    return borrador.crearColeccion();
  }


  public void cargarColeccionDesdeDataSet(String fuente, ColeccionBuilder borrador) {

    CargaDataset data = new CargaDataset();
    List<Hecho> todosLosHechos;
    //List<Hecho> filtrados = new ArrayList<>();

    try {
      todosLosHechos = data.cargarHechosDesdeCsv(fuente);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    List<Hecho> filtrados = todosLosHechos.stream().filter(h ->
        h.getCategoria().getCriterioPertenencia()
            .equals(borrador.getCriterioPertenencia().getCriterioPertenencia())).toList();

    // Si ya existe, lo pisa
    Map<String, Hecho> hechosUnicos = new HashMap<>();
    for (Hecho hecho : filtrados) {
      hechosUnicos.put(hecho.getTitulo(), hecho); // Si ya existe, lo pisa
    }


    borrador.setFuente(fuente);
    borrador.setFuenteTipo(Fuente.DATASET);
    borrador.setListaHechos(new ArrayList<>(hechosUnicos.values()));

    Coleccion nuevaColeccion = this.crearNuevaColeccion(borrador);

    RepositorioDeColecciones.agregarColeccion(nuevaColeccion);

    System.out.println("Se extrajeron corrctamente los dados desde el archivo CSV");

  }

  public void aceptarSolicitud(SolicitudDeEliminacion solicitud) {
    solicitud.solicitudAceptada();

    RegistroDeColecciones.addHechoNoDisp(solicitud.getHecho());
    RegistroDeColecciones.removeHechoColeccionesDisp(solicitud.getHecho());
  }

  public void rechazarSolicitud(SolicitudDeEliminacion solicitud) {
    solicitud.solicitudRechazada();
  }

//  public void revisarSolicitudes(RepositorioSolicitudes repositorio) {
//    repositorio.obtenerSolicitudesPendientes()
//  }

  @Override
  public void cargarHecho(Hecho hecho, Coleccion coleccion) {
  
  }
  
}
