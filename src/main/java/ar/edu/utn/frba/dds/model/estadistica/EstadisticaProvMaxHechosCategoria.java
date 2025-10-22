package ar.edu.utn.frba.dds.model.estadistica;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import com.opencsv.CSVWriter;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class EstadisticaProvMaxHechosCategoria implements Estadistica, WithSimplePersistenceUnit {
  private String provincia;
  public String categoria;

  public EstadisticaProvMaxHechosCategoria(String categoria) {
    this.categoria = categoria;
  }

  @Override
  public void calcularEstadistica() {

    List<Hecho> hechos = entityManager()
        .createQuery("from Hecho h where h.categoria  = :categoria", Hecho.class)
        .setParameter("categoria", this.categoria)
        .getResultList();

    List<String> provincias = hechos.stream().map(Hecho::obtenerProvincia).toList();

    this.provincia = provincias.stream()
        .collect(groupingBy(identity(), counting()))
        .entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey).orElse(null);

  }

  @Override public void exportarEstadistica(String path) throws IOException {
    File file = new File(path);

    if (file.exists()) {
      boolean eliminado = file.delete();
    }
    try (CSVWriter writer = new CSVWriter(
        new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
      String[] header = {"Fecha", "Categoria", "ProvinciaMaxima"};
      String[] data = {
          LocalDateTime.now().toString(),
          categoria,
          provincia != null ? provincia : "N/A"
      };

      if (file.length() == 0) {
        writer.writeNext(header);
      }
      writer.writeNext(data);
    }
  }

  public String getProvinciaMax() {
    return provincia;
  }
}
