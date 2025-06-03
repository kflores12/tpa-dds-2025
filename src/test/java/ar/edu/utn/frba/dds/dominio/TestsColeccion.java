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
  List<Criterio> criterios;
  Criterio rango;
  Criterio titulo;
  @BeforeEach
  public void fixtureColeccion() {
    rango = new CriterioRangoFechas(LocalDate.of(2023, 1, 1),
        LocalDate.of(2025,12,30));
    titulo = new CriterioTitulo("incendio");
    dataset = new FuenteDataSet("datos.csv");
    datavacio = new FuenteDataSet("vacio.csv");
    criterios = new ArrayList<>(Arrays.asList(titulo,rango));
  }

  @Test
  public void importarDesdeDataset() {
    GeneradorHandleUUID generador = new GeneradorHandleUUID();
    Coleccion coleccion = new Coleccion("incendios forestales",
        "incendios en la patagonia",
        dataset, criterios, generador.generar());

    List<Hecho> hechos = coleccion.obtenerTodosLosHechos();

    Assertions.assertEquals(2, hechos.size());
  }


  @Test
  public void listaHechosDisponibles() {
    GeneradorHandleUUID generador = new GeneradorHandleUUID();
    Coleccion coleccion = new Coleccion("incendios forestales",
        "incendios en la patagonia",
        dataset, criterios, generador.generar());

    List<Hecho> hechos = coleccion.listarHechosDisponibles();

    Assertions.assertEquals(2, hechos.size());
  }

  @Test
  public void elHandleDebeSerValidoYUnico() {
    GeneradorHandleUUID generador = new GeneradorHandleUUID();

    Coleccion coleccion1 = new Coleccion("Colección A", "desc A", dataset, criterios, generador.generar());
    Coleccion coleccion2 = new Coleccion("Colección A", "desc A", dataset, criterios, generador.generar());

    String handle1 = coleccion1.getHandler();
    String handle2 = coleccion2.getHandler();

    Assertions.assertNotNull(handle1);
    Assertions.assertTrue(handle1.matches("[a-z0-9]+"), "El handle no tiene formato válido");
    Assertions.assertNotEquals(handle1, handle2, "Los handles deberían ser distintos incluso con el mismo título");
  }
}
