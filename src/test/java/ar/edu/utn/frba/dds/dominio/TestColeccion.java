package ar.edu.utn.frba.dds.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestColeccion {
  @Test
  public void importarDesdeCsv() {
    FuenteDataSet dataset = new FuenteDataSet("datos.csv");
    CriterioCategoria categoria = new CriterioCategoria("incendio forestales");
    CriterioRangoFechas rango = new CriterioRangoFechas(
        LocalDate.of(2023, 10, 2), LocalDate.of
        (2025,5,7));
    List<Criterio> criterios = new ArrayList<>();
    criterios.add(categoria);
    criterios.add(rango);
    Coleccion nuevaColeccion = new Coleccion("Incendios forestales en argentina",
        "incendios forestales", dataset, criterios, new ArrayList<>());
    List<Hecho> totales = nuevaColeccion.obtenerHechos();
    for (Hecho h : totales) {
      System.out.println("el titulo es: " + h.getTitulo());
    }
    assertEquals(2, totales.size());
  }

}
