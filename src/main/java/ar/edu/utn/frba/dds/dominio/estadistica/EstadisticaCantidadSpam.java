package ar.edu.utn.frba.dds.dominio.estadistica;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudDeEliminacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;

public class EstadisticaCantidadSpam implements Estadistica, WithSimplePersistenceUnit {
   int cantidadSpam;


  @Override public void calcularEstadistica() {
    List<SolicitudDeEliminacion> solicitudes = entityManager()
        .createQuery("from SolicitudDeEliminacion", SolicitudDeEliminacion.class)
        .getResultList();

    cantidadSpam = solicitudes.size();
  }

  @Override public void exportarEstadistica() {

  }

  public int getCantidadSpam() {
    return cantidadSpam;
  }
}
