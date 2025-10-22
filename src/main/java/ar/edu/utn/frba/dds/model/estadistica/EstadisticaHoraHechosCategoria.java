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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class EstadisticaHoraHechosCategoria implements Estadistica, WithSimplePersistenceUnit {
  public String categoria;
  public LocalTime horaPicoCategoria;

  public EstadisticaHoraHechosCategoria(String categoria) {
    this.categoria = categoria;
  }

  @Override
  public void calcularEstadistica() {

    List<Hecho> hechos = entityManager()
        .createQuery("from Hecho h where h.categoria  = :categoria", Hecho.class)
        .setParameter("categoria", this.categoria)
        .getResultList();

    List<LocalTime> horas = hechos.stream()
        .map(h -> LocalTime.from(h.getFechaAcontecimiento()))
        .toList();

    this.horaPicoCategoria = horas.stream()
        .collect(groupingBy(identity(), counting()))
        .entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey).orElse(null);

  }


  @Override
  public void exportarEstadistica(String path) throws IOException {
    File file = new File(path);

    if (file.exists()) {
      boolean eliminado = file.delete();
    }
    try (CSVWriter writer = new CSVWriter(
        new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
      String[] header = {"Fecha", "Categoria", "HoraPico"};

      String horaFormateada = horaPicoCategoria != null
          ? horaPicoCategoria.format(DateTimeFormatter.ofPattern("HH:mm"))
          : "N/A";

      String[] data = {
          LocalDateTime.now().toString(),
          categoria,
          horaFormateada
      };

      if (file.length() == 0) {
        writer.writeNext(header);
      }
      writer.writeNext(data);
    }
  }

  public LocalTime gethoraPicoCategoria() {
    return horaPicoCategoria;
  }


}
