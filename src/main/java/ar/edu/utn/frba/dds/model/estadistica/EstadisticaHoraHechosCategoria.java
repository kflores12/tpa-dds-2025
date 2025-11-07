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

  List<EstadisticaHoraHechosCategoria.categoriaHoraPicoDTO> reporte = new ArrayList<EstadisticaHoraHechosCategoria.categoriaHoraPicoDTO>();

  public record categoriaHoraPicoDTO(String categoria, String hora_pico, BigInteger cantidad) {}

  @Override
  public void calcularEstadistica() {

    List<Object[]> listaDTO = entityManager()
        .createNativeQuery("SELECT subq.categoria, subq.fechaAcontecimiento as hora_pico, subq.cantidad\n" +
            "                        FROM (\n" +
            "                          SELECT\n" +
            "                            h.categoria,\n" +
            "                            'hora pico' as fechaAcontecimiento,\n" +
            "                            COUNT(*) AS cantidad \n" +
            "                          FROM hechos h\n" +
            "                          GROUP BY h.categoria, h.fechaAcontecimiento\n" +
            "                        ) subq\n" +
            "                        LEFT JOIN (\n" +
            "                          SELECT\n" +
            "                            h.categoria,\n" +
            "                            'hora pico' as fechaAcontecimiento,\n" +
            "                            COUNT(*) AS cantidad \n" +
            "                          FROM hechos h\n" +
            "                          GROUP BY h.categoria, h.fechaAcontecimiento\n" +
            "                        ) subq2\n" +
            "                        ON subq.categoria = subq2.categoria\n" +
            "                AND subq2.cantidad > subq.cantidad\n" +
            "                WHERE subq2.categoria IS NULL;"
        )
        .getResultList();

    List<EstadisticaHoraHechosCategoria.categoriaHoraPicoDTO> lista = new ArrayList<>();

    for (Object[] r : listaDTO) {
      String categoria = (String) r[0];
      String hora_pico  = (String) r[1];
      BigInteger cantidad  = (BigInteger) r[2];
      reporte.add(new EstadisticaHoraHechosCategoria.categoriaHoraPicoDTO(categoria, hora_pico, cantidad));
    }

    reporte.forEach(dto -> System.out.printf("Categoria: %s | Hora: %s | Cantidad: %d%n", dto.categoria(), dto.hora_pico(), dto.cantidad()));
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
              dto.hora_pico != null ? dto.hora_pico() : "N/A"}));


    }

  }

  public List<categoriaHoraPicoDTO> getReporte() {
    return reporte;
  }
}
