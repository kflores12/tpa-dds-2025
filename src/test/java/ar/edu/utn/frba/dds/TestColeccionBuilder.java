package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.ColeccionBuilder;
import ar.edu.utn.frba.dds.dominio.Etiqueta;
import ar.edu.utn.frba.dds.dominio.Fuente;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class TestColeccionBuilder {
  @Test
  void testCrearColeccion() {
    ColeccionBuilder builder = new ColeccionBuilder("Eventos climaticos extraños",
        "Serie de eventos climaticos inexplicables",
        new Etiqueta("Clima"));
    builder.setFuente("datos.csv");
    builder.setFuenteTipo(Fuente.CONTRIBUYENTE);
    builder.setListaHechos(new ArrayList<>());

    Coleccion coleccion = builder.crearColeccion();
    assertEquals(Fuente.CONTRIBUYENTE, coleccion.getFuenteTipo());
    }

    @Test
  void testCrearColeccionInconsistente() {
      ColeccionBuilder builder = new ColeccionBuilder("Eventos climaticos extraños",
          "Serie de eventos climaticos inexplicables",
          new Etiqueta("Clima"));
      assertThrows(RuntimeException.class,builder::crearColeccion);
    }
}