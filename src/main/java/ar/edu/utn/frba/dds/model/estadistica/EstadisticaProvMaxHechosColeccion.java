package ar.edu.utn.frba.dds.model.estadistica;

import static ar.edu.utn.frba.dds.model.estadistica.LocalizadorDeProvincias.getProvincia;

import ar.edu.utn.frba.dds.model.entities.Coleccion;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticaProvMaxHechosColeccion implements Estadistica, WithSimplePersistenceUnit {

  List<EstadisticaProvMaxHechosColeccion.Estpmhcolecciondto> reporte =
      new ArrayList<EstadisticaProvMaxHechosColeccion.Estpmhcolecciondto>();

  public record Estpmhcolecciondto(String coleccion, String provincia, BigInteger cantidad) {
  }

  public EstadisticaProvMaxHechosColeccion() {}

  @Override public void calcularEstadistica() {

    List<Object[]> listaDto = entityManager()
        .createNativeQuery("SELECT subq1.coleccion, subq1.provincia, subq1.cantidad \n"
            +
            "FROM ( \n"
            +
            "SELECT\n"
            +
            "c.titulo as coleccion,\n"
            +
            "h.provincia,\n"
            +
            "COUNT(*) AS cantidad\n"
            +
            "FROM coleccion_hechos ch \n"
            +
            "JOIN colecciones c \n"
            +
            "ON ch.coleccion_id = c.id\n"
            +
            "JOIN hechos h \n"
            +
            "ON ch.hecho_id = h.id \n"
            +
            "GROUP BY c.titulo, h.provincia \n"
            +
            ") subq1 \n"
            +
            "LEFT JOIN (\n"
            +
            "SELECT\n"
            +
            "c.titulo as coleccion,\n"
            +
            "h.provincia,\n"
            +
            "COUNT(*) AS cantidad\n"
            +
            "FROM coleccion_hechos ch \n"
            +
            "JOIN colecciones c \n"
            +
            "ON ch.coleccion_id = c.id\n"
            +
            "JOIN hechos h \n"
            +
            "ON ch.hecho_id = h.id\n"
            +
            "GROUP BY c.titulo, h.provincia\n"
            +
            ") subq2\n"
            +
            "ON subq1.coleccion = subq2.coleccion\n"
            +
            "AND subq2.cantidad > subq1.cantidad\n"
            +
            "WHERE subq2.coleccion IS NULL;")
        .getResultList();

    for (Object[] r : listaDto) {

      String coleccion = (String) r[0];

      String provincia = (String) r[1];

      BigInteger cantidad = (BigInteger) r[2];
      reporte.add(new EstadisticaProvMaxHechosColeccion.Estpmhcolecciondto(
          coleccion,
          provincia,
          cantidad));
    }

    reporte.forEach(dto -> System.out.printf(
        "Coleccion: %s | Provincia: %s | Cantidad: %d%n",
        dto.coleccion(),
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
      String[] header = {"Fecha", "Coleccion", "ProvinciaMaxima"};

      if (file.length() == 0) {
        writer.writeNext(header);
      }

      reporte.forEach(dto ->
          writer.writeNext(new String[]{LocalDateTime.now().toString(),
              dto.coleccion != null ? dto.coleccion() : "N/A",
              dto.provincia != null ? dto.provincia() : "N/A"}));

    }
  }

  public List<EstadisticaProvMaxHechosColeccion.Estpmhcolecciondto> getReporte() {
    return new ArrayList<>(reporte);
  }

}
