package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.utn.frba.dds.dominio.Administrador;
import ar.edu.utn.frba.dds.dominio.CargaDataset;
import ar.edu.utn.frba.dds.dominio.Hecho;
import org.junit.jupiter.api.Test;
import java.util.List;


public class TestCargaDataset {
  @Test
  void testCargaHechosDesdeCsv() {
    CargaDataset cargaDataset = new CargaDataset();
    List<Hecho> hechos = cargaDataset.cargarHechosDesdeCsv("datos.csv");
    assertFalse(hechos.isEmpty(), "La lista de hechos no debería esta vacia");
    assertEquals("Incendio en Bariloche", hechos.get(0).getTitulo(),
        "El titulo coincide");
  }

  @Test
  void testCsvVacioLanzaExcepcion(){
    CargaDataset cargaDataset = new CargaDataset();
    assertThrows(RuntimeException.class, ()
        -> cargaDataset.cargarHechosDesdeCsv("vacio.csv"));
  }

}
