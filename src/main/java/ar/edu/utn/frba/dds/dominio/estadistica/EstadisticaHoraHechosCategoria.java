package ar.edu.utn.frba.dds.dominio.estadistica;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import ar.edu.utn.frba.dds.dominio.Hecho;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class EstadisticaHoraHechosCategoria implements Estadistica, WithSimplePersistenceUnit {

  List<EstadisticaHoraHechosCategoria.categoriaHoraPicoDTO> reporte = new ArrayList<EstadisticaHoraHechosCategoria.categoriaHoraPicoDTO>();

  public record categoriaHoraPicoDTO(String categoria, String hora_pico) {}

  @Override
  public void calcularEstadistica() {

    List<Object[]> listaDTO = entityManager()
        .createNativeQuery("SELECT subq.categoria, DATE_FORMAT(subq.fechaAcontecimiento, '%H:%i:%s') as hora_pico\n" +
            "FROM (\n" +
            "  SELECT\n" +
            "    h.categoria,\n" +
            "    h.fechaAcontecimiento,\n" +
            "    COUNT(*) AS cantidad,\n" +
            "    ROW_NUMBER() OVER (\n" +
            "      PARTITION BY categoria\n" +
            "      ORDER BY COUNT(*) DESC, h.fechaAcontecimiento\n" +
            "    ) AS rn\n" +
            "  FROM hechos h\n" +
            "  GROUP BY h.categoria, h.fechaAcontecimiento\n" +
            ") subq\n" +
            "WHERE subq.rn = 1")
        .getResultList();

    List<EstadisticaHoraHechosCategoria.categoriaHoraPicoDTO> lista = new ArrayList<>();

    for (Object[] r : listaDTO) {
      String categoria = (String) r[0];
      String hora_pico  = (String) r[1];
      reporte.add(new EstadisticaHoraHechosCategoria.categoriaHoraPicoDTO(categoria, hora_pico));
    }

    reporte.forEach(dto -> System.out.printf("Categoria: %s | Hora: %s%n", dto.categoria(), dto.hora_pico()));
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

      if (file.length() == 0) {
        writer.writeNext(header);
      }

      reporte.forEach(dto ->
          writer.writeNext(new String[]{LocalDateTime.now().toString(),
              dto.categoria() != null ? dto.categoria() : "N/A",
              dto.hora_pico != null ? dto.hora_pico() : "N/A"}));


    }

  }

  public List<categoriaHoraPicoDTO> getReporte() {
    return reporte;
  }
}
