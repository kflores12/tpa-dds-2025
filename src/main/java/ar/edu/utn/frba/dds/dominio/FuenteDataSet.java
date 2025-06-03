package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuenteDataSet implements Fuente {

  private String ruta;

  public FuenteDataSet(String ruta) {
    this.ruta = requireNonNull(ruta);
  }

  @Override
  public List<Hecho> importarHechos(List<Criterio> criterios) {

    List<Hecho> hechos = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    try (CSVReader lector = new CSVReader(
        new InputStreamReader(new FileInputStream(ruta), StandardCharsets.UTF_8))) {
      String[] linea;

      while ((linea = lector.readNext()) != null) {
        if (linea.length != 6) {
          throw new RuntimeException("Error en línea "
              + Arrays.toString(linea) + ": se esperaban 6 campos, se encontraron "
              + linea.length);
        }

        String titulo = linea[0];
        String desc = linea[1];
        String categoria = linea[2];
        Double latitud = Double.parseDouble(linea[3]);
        Double longitud = Double.parseDouble(linea[4]);
        LocalDate fechaAcontecimiento = LocalDate.parse(linea[5], formatter);


        Hecho hecho = new Hecho(titulo, desc, categoria, latitud, longitud,
            fechaAcontecimiento, LocalDate.now(), TipoFuente.DATASET, null,
            Boolean.TRUE);
        hechos.add(hecho);

      }
      if (hechos.isEmpty()) {
        throw new RuntimeException("El archivo esta vacio");
      }

    } catch (CsvValidationException | IOException e) {
      throw new RuntimeException("Error al leer el archivo CSV: " + ruta, e);
    }

    if (criterios == null || criterios.isEmpty()) {
      return new ArrayList<>(this.filtrarDuplicados(hechos).values());
    }
    //en caso de que no haya criterio devuelve todos los hechos sin filtrar
    //el filtro base seria redundante
    List<Hecho> filtrados = hechos.stream().filter(h -> criterios.stream()
        .allMatch(c -> c.aplicarFiltro(h))).toList();


    return new ArrayList<>(this.filtrarDuplicados(filtrados).values());
  }

  public Map<String, Hecho> filtrarDuplicados(List<Hecho> duplicados) {
    Map<String, Hecho> hechosUnicos = new HashMap<>();
    for (Hecho hecho : duplicados) {
      hechosUnicos.put(hecho.getTitulo(), hecho);
    }
    return hechosUnicos;
  }


}
