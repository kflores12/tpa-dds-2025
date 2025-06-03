package ar.edu.utn.frba.dds.dominio;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestsColeccion {
  Fuente dataset;
  Fuente datavacio;
  Fuente dataDesogranizada;
  List<Criterio> criterios;
  List<Criterio> criterios2;
  Criterio rango;
  Criterio titulo;
  Criterio CCategoria;
  @BeforeEach
  public void fixtureColeccion() {
    rango = new CriterioRangoFechas(LocalDate.of(2023, 1, 1),
        LocalDate.of(2025,12,30));
    titulo = new CriterioTitulo("incendio");
    dataset = new FuenteDataSet("datos.csv","yyyy-MM-dd",',');
    datavacio = new FuenteDataSet("vacio.csv","yyyy-MM-dd",',');
    dataDesogranizada = new FuenteDataSet("EjHechos.csv","d/M/yyyy",';');
    CCategoria = new CriterioCategoria("Ruta Provincial");
    criterios = new ArrayList<>(Arrays.asList(titulo,rango));
    criterios2 = new ArrayList<>(Arrays.asList(CCategoria));

  }

  @Test
  public void importarDesdeDataset() {
    Coleccion coleccion = new Coleccion("incendios forestales",
        "incendios en la patagonia", dataset, criterios);

    List<Hecho> hechos = coleccion.obtenerTodosLosHechos();



    //System.out.printf(" %s \n", hechos.get(0).getTitulo());
    //System.out.printf(" %s \n", hechos.get(1).getTitulo());

    Assertions.assertEquals(2, hechos.size());
  }

  @Test
  public void importarDeArchivoGrande() {
    Coleccion coleccion = new Coleccion("Choques vehiculos",
        "Choques en rutas", dataDesogranizada, criterios2);

    List<Hecho> hechos = coleccion.obtenerTodosLosHechos();


//    System.out.printf(" %s \n", hechos.get(0).getTitulo());
//    System.out.printf(" %s \n", hechos.get(1).getTitulo());
//    System.out.printf(" %s \n", hechos.get(2).getTitulo());

    Assertions.assertEquals(3, hechos.size());
  }



  //Colisión vehículo-vehículo

  //TODO testear que rompe con archivo vacio
  /*
  @Test
  public void importarDesdeDatasetVacio() {
    Coleccion coleccion = new Coleccion("incendios forestales",
        "incendios en la patagonia", datavacio, criterios);

    List<Hecho> hechos = coleccion.obtenerTodosLosHechos();
  }

   */

  //TODO testear y validar casos excepcionales del archivo csv

  @Test
  public void listaHechosDisponibles() {
    Coleccion coleccion = new Coleccion("incendios forestales",
        "incendios en la patagonia", dataset, criterios);

    List<Hecho> hechos = coleccion.listarHechosDisponibles();

    Assertions.assertEquals(2, hechos.size());
  }
}
