package ar.edu.utn.frba.dds.dominio;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestsFuenteDataSet {
  Fuente dataset;
  Fuente datavacio;
  Fuente dataDesogranizada;
  Fuente dataFaltanColumnas;
  Fuente dataColumnasRotas;
  Fuente dataNoExiste;
  Fuente dataColumnaVacia;
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
    dataFaltanColumnas = new FuenteDataSet("EjHechosColumnaMandatoriaFaltante.csv","d/M/yyyy",';');
    dataColumnasRotas = new FuenteDataSet("EjHechosConColumnaRota.csv","d/M/yyyy",';');
    dataColumnaVacia = new FuenteDataSet("EjHechosConColumnaVaciaTitulo.csv","d/M/yyyy",';');
    dataNoExiste = new FuenteDataSet("archivoFalso123.csv","yyyy-MM-dd",',');
    CCategoria = new CriterioCategoria("Ruta Provincial");
    criterios = new ArrayList<>(Arrays.asList(titulo,rango));
    criterios2 = new ArrayList<>(Arrays.asList(CCategoria));

  }

  @Test
  public void elHandleDebeSerValidoYUnico() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();

    Coleccion coleccion1 = new Coleccion("Colección A", "desc A", dataset, criterios, generador.generar());
    Coleccion coleccion2 = new Coleccion("Colección A", "desc A", dataset, criterios, generador.generar());

    String handle1 = coleccion1.getHandler();
    String handle2 = coleccion2.getHandler();

    Assertions.assertNotNull(handle1);
    Assertions.assertTrue(handle1.matches("[a-z0-9]+"), "El handle no tiene formato válido");
    Assertions.assertNotEquals(handle1, handle2, "Los handles deberían ser distintos incluso con el mismo título");
  }

  @Test
  public void importarDesdeDatasetConUnFormato() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    Coleccion coleccion = new Coleccion("incendios forestales",
        "incendios en la patagonia",
        dataset, criterios, generador.generar());

    List<Hecho> hechos = coleccion.obtenerTodosLosHechos();

    Assertions.assertEquals("Incendio en pehuen", hechos.get(0).getTitulo());
    Assertions.assertEquals("Incendio en Bariloche", hechos.get(1).getTitulo());
    Assertions.assertEquals(2, hechos.size());
  }

  @Test
  public void importarDeArchivoConOtroFormatoDesordenado() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    Coleccion coleccion = new Coleccion("Choques vehiculos",
        "Choques en rutas", dataDesogranizada, criterios2,generador.generar());

    List<Hecho> hechos = coleccion.obtenerTodosLosHechos();

    Assertions.assertEquals("RUTA PROVINCIAL 7", hechos.get(0).getTitulo());
    Assertions.assertEquals("RUTA PROVINCIAL 42", hechos.get(1).getTitulo());
    Assertions.assertEquals("RUTA PROVINCIAL 17", hechos.get(2).getTitulo());
    Assertions.assertEquals(3, hechos.size());
    //En este test se valida tambien cuando una columna opcional falta.
    Assertions.assertEquals(null, hechos.get(0).getMultimedia());
  }

  @Test
  public void importarDeArchivoConColumnaMandatoriaFaltante() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    Coleccion coleccion = new Coleccion("Choques vehiculos",
        "Choques en rutas", dataFaltanColumnas, criterios2,generador.generar());

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      List<Hecho> hechos = coleccion.obtenerTodosLosHechos();
    });
    Assertions.assertEquals("Error al procesar los datos del CSV: EjHechosColumnaMandatoriaFaltante.csv" , exception.getMessage());
    Assertions.assertEquals("Faltan valores en alguna linea" , exception.getCause().getMessage()); // El valor es null
  }

  @Test
  public void importarDeArchivoConColumnaRota() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    Coleccion coleccion = new Coleccion("Choques vehiculos",
        "Choques en rutas", dataColumnasRotas, criterios2,generador.generar());


    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      List<Hecho> hechos = coleccion.obtenerTodosLosHechos();
    });
    Assertions.assertEquals("Error al procesar los datos del CSV: EjHechosConColumnaRota.csv" , exception.getMessage());
  }

  @Test
  public void importarDeArchivoConColumnaVacia() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    Coleccion coleccion = new Coleccion("Choques vehiculos",
        "Choques en rutas", dataColumnaVacia, criterios2,generador.generar());

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      List<Hecho> hechos = coleccion.obtenerTodosLosHechos();
    });
    Assertions.assertEquals("Error al procesar los datos del CSV: EjHechosConColumnaVaciaTitulo.csv" , exception.getMessage());
    Assertions.assertEquals("Faltan valores en alguna linea" , exception.getCause().getMessage()); // El valor es ""
  }

  @Test
  public void importarDeArchivoInexistente() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    Coleccion coleccion = new Coleccion("Choques vehiculos",
        "Choques en rutas", dataNoExiste, criterios2,generador.generar());

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      List<Hecho> hechos = coleccion.obtenerTodosLosHechos();
    });
    Assertions.assertEquals("Error al leer el archivo CSV: archivoFalso123.csv" , exception.getMessage());
    Assertions.assertEquals("archivoFalso123.csv (El sistema no puede encontrar el archivo especificado)" , exception.getCause().getMessage());
  }

  @Test
  public void importarDesdeDatasetVacio() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    Coleccion coleccion = new Coleccion("incendios forestales",
        "incendios en la patagonia", datavacio, criterios,generador.generar());

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      List<Hecho> hechos = coleccion.obtenerTodosLosHechos();
    });
    Assertions.assertEquals("Error al procesar los datos del CSV: vacio.csv" , exception.getMessage());
    Assertions.assertEquals("El archivo está vacío o no contiene hechos válidos" , exception.getCause().getMessage());
  }

  @Test
  public void listaHechosDisponibles() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    Coleccion coleccion = new Coleccion("incendios forestales",
        "incendios en la patagonia",
        dataset, criterios, generador.generar());
    List<Hecho> hechos = coleccion.listarHechosDisponibles();
    Assertions.assertEquals(2, hechos.size());
  }

}
