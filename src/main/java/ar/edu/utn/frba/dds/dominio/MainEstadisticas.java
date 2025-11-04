package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.criterios.Criterio;
import ar.edu.utn.frba.dds.dominio.criterios.CriterioBase;
import ar.edu.utn.frba.dds.dominio.estadistica.ComponenteEstadistico;
import ar.edu.utn.frba.dds.dominio.estadistica.Estadistica;
import ar.edu.utn.frba.dds.dominio.estadistica.EstadisticaCantidadSpam;
import ar.edu.utn.frba.dds.dominio.estadistica.EstadisticaCategoriaMaxima;
import ar.edu.utn.frba.dds.dominio.estadistica.EstadisticaProvMaxHechosCategoria;
import ar.edu.utn.frba.dds.dominio.estadistica.EstadisticaProvMaxHechosColeccion;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDinamica;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainEstadisticas {
  @SuppressWarnings("checkstyle:WhitespaceAfter")
  public static void main(String[] args) {

    //Creacion de funciones para estadisticas
    FuenteDinamica dinamica = new FuenteDinamica();

    EstadisticaCategoriaMaxima estadisticaCm =
        new EstadisticaCategoriaMaxima();
    EstadisticaCantidadSpam estadisticaCs = new EstadisticaCantidadSpam();
    EstadisticaProvMaxHechosCategoria estadisticaPmhcat =
        new EstadisticaProvMaxHechosCategoria();
    EstadisticaProvMaxHechosColeccion estadisticaPmhcalt =
        new EstadisticaProvMaxHechosColeccion();


    //Agrego Estadisticas a la carga
    List<Estadistica> estadisticas = new ArrayList<>();
    estadisticas.add(estadisticaCm);
    estadisticas.add(estadisticaCs);
    estadisticas.add(estadisticaPmhcat);
    estadisticas.add(estadisticaPmhcalt);

    //Calculamos estadisticas
    ComponenteEstadistico componenteEstadistico = new ComponenteEstadistico(estadisticas);
    componenteEstadistico.calcularEstadisticas();
  }
}
