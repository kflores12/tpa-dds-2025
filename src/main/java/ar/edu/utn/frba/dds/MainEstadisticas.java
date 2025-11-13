package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.estadistica.ComponenteEstadistico;
import ar.edu.utn.frba.dds.model.estadistica.Estadistica;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaCantidadSpam;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaCategoriaMaxima;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaHoraHechosCategoria;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaProvMaxHechosCategoria;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaProvMaxHechosColeccion;
import java.util.ArrayList;
import java.util.List;

public class MainEstadisticas {
  public static void main(String[] args) {

    EstadisticaCategoriaMaxima estadisticaCm =
        new EstadisticaCategoriaMaxima();
    EstadisticaCantidadSpam estadisticaCs = new EstadisticaCantidadSpam();
    EstadisticaProvMaxHechosCategoria estadisticaPmhcat =
        new EstadisticaProvMaxHechosCategoria();
    EstadisticaProvMaxHechosColeccion estadisticaPmhcalt =
        new EstadisticaProvMaxHechosColeccion();
    EstadisticaHoraHechosCategoria estadisticaHoraPico =
        new EstadisticaHoraHechosCategoria();

    //Agrego Estadisticas a la carga
    List<Estadistica> estadisticas = new ArrayList<>();
    estadisticas.add(estadisticaCm);
    estadisticas.add(estadisticaCs);
    estadisticas.add(estadisticaPmhcat);
    estadisticas.add(estadisticaPmhcalt);
    estadisticas.add(estadisticaHoraPico);

    ComponenteEstadistico.inicializar(estadisticas);

    //Calculamos estadisticas
    ComponenteEstadistico componenteEstadistico = ComponenteEstadistico.getInstance();
    componenteEstadistico.calcularEstadisticas();

    System.out.println("\n--- Estadísticas de hora pico (en memoria) ---");
    componenteEstadistico.getEstadistica(EstadisticaHoraHechosCategoria.class)
        .calcularEstadistica();
  }
}
