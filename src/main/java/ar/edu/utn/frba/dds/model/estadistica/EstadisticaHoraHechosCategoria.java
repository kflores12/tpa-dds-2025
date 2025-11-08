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
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EstadisticaHoraHechosCategoria implements Estadistica, WithSimplePersistenceUnit {

  List<EstadisticaHoraHechosCategoria.Categoriahorapicodto> reporte =
      new ArrayList<EstadisticaHoraHechosCategoria.Categoriahorapicodto>();

  public record Categoriahorapicodto(String categoria, String horapico, BigInteger cantidad) {}

  @Override
  public void calcularEstadistica() {

    List<Object[]> listaDto = entityManager()
        .createNativeQuery("SELECT subq.categoria, subq.fechaAcontecimiento as hora_pico, "
            +
            "subq.cantidad\n"
            +
            "FROM (\n"
            +
            "SELECT\n"
            +
            "h.categoria,\n"
            +
            "'hora pico' as fechaAcontecimiento,\n"
            +
            "COUNT(*) AS cantidad \n"
            +
            "FROM hechos h\n"
            +
            "GROUP BY h.categoria, h.fechaAcontecimiento\n"
            +
            ") subq\n"
            +
            "LEFT JOIN (\n"
            +
            "SELECT\n"
            +
            "h.categoria,\n"
            +
            "'hora pico' as fechaAcontecimiento,\n"
            +
            "COUNT(*) AS cantidad \n"
            +
            "FROM hechos h\n"
            +
            "GROUP BY h.categoria, h.fechaAcontecimiento\n"
            +
            ") subq2\n"
            +
            "ON subq.categoria = subq2.categoria\n"
            +
            "AND subq2.cantidad > subq.cantidad\n"
            +
            "WHERE subq2.categoria IS NULL;"
        )
        .getResultList();

    for (Object[] r : listaDto) {
      String categoria = (String) r[0];
      String horapico  = (String) r[1];
      BigInteger cantidad  = (BigInteger) r[2];
      reporte.add(new EstadisticaHoraHechosCategoria.Categoriahorapicodto(
          categoria,
          horapico,
          cantidad));
    }

    reporte.forEach(dto -> System.out.printf(
        "Categoria: %s | Hora: %s | Cantidad: %d%n",
        dto.categoria(),
        dto.horapico(),
        dto.cantidad()));
  }


  @Override
  public void exportarEstadistica(String path) throws IOException {
    File file = new File(path);

    if (file.exists()) {
      boolean eliminado = file.delete();
    }
    try (CSVWriter writer = new CSVWriter(
        new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
      String[] header = {"Fecha", "Categoria", "HoraPico", "Cantidad"};

      if (file.length() == 0) {
        writer.writeNext(header);
      }

      reporte.forEach(dto ->
          writer.writeNext(new String[]{LocalDateTime.now().toString(),
              dto.categoria() != null ? dto.categoria() : "N/A",
              dto.horapico != null ? dto.horapico() : "N/A"}));


    }

  }

  public List<Categoriahorapicodto> getReporte() {
    return new ArrayList<>(reporte);
  }
}
