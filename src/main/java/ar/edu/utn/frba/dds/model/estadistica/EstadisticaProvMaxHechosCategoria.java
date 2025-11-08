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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EstadisticaProvMaxHechosCategoria implements Estadistica, WithSimplePersistenceUnit {

  List<EstadisticaProvMaxHechosCategoria.Estpmhcategoriadto> reporte =
      new ArrayList<EstadisticaProvMaxHechosCategoria.Estpmhcategoriadto>();

  public record Estpmhcategoriadto(String categoria, String provincia, BigInteger cantidad) {}

  public EstadisticaProvMaxHechosCategoria() {}

  @Override
  public void calcularEstadistica() {

    List<Object[]> listaDto = entityManager()
        .createNativeQuery(
            "SELECT a.categoria, a.provincia, a.cantidad\n"
                +
                "FROM (\n"
                +
                "  SELECT categoria, provincia, COUNT(*) AS cantidad\n"
                +
                "  FROM hechos\n"
                +
                "  GROUP BY categoria, provincia\n"
                +
                ") a\n"
                +
                "LEFT JOIN (\n"
                +
                "  SELECT categoria, provincia, COUNT(*) AS cantidad\n"
                +
                "  FROM hechos\n"
                +
                "  GROUP BY categoria, provincia\n"
                +
                ") b\n"
                +
                "  ON a.categoria = b.categoria\n"
                +
                " AND b.cantidad > a.cantidad\n"
                +
                "WHERE b.categoria IS NULL;")
        .getResultList();

    for (Object[] r : listaDto) {
      String categoria = (String) r[0];
      String provincia = (String) r[1];
      BigInteger cantidad = (BigInteger) r[2];
      reporte.add(new EstadisticaProvMaxHechosCategoria.Estpmhcategoriadto(
          categoria,
          provincia,
          cantidad));
    }

    reporte.forEach(dto -> System.out.printf(
        "Categoria: %s | Provincia: %s | Cantidad %d%n",
        dto.categoria(),
        dto.provincia(),
        dto.cantidad()));

  }

  @Override public void exportarEstadistica(String path) throws IOException {
    File file = new File(path);

    if (file.exists()) {
      boolean eliminado = file.delete();
    }
    try (CSVWriter writer = new CSVWriter(
        new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
      String[] header = {"Fecha", "Categoria", "ProvinciaMaxima", "Cantidad"};

      if (file.length() == 0) {
        writer.writeNext(header);
      }

      reporte.forEach(dto ->
          writer.writeNext(new String[]{LocalDateTime.now().toString(),
              dto.categoria() != null ? dto.categoria() : "N/A",
              dto.provincia != null ? dto.provincia() : "N/A"}));

    }
  }

  public List<EstadisticaProvMaxHechosCategoria.Estpmhcategoriadto> getReporte() {
    return new ArrayList<>(reporte);
  }

}
