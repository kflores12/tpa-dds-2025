package ar.edu.utn.frba.dds.dominio.estadistica;

import static ar.edu.utn.frba.dds.dominio.estadistica.LocalizadorDeProvincias.getProvincia;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import com.opencsv.CSVWriter;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticaProvMaxHechosColeccion implements Estadistica, WithSimplePersistenceUnit {

  List<EstadisticaProvMaxHechosColeccion.EstPMHColeccionDTO> reporte = new ArrayList<EstadisticaProvMaxHechosColeccion.EstPMHColeccionDTO>();

  public record EstPMHColeccionDTO(String coleccion, String provincia) {}

  public EstadisticaProvMaxHechosColeccion() {}

  @Override public void calcularEstadistica() {

    List<Object[]> listaDTO = entityManager()
        .createNativeQuery("SELECT subq.coleccion, subq.provincia \n" +
            "FROM (\n" +
            "  SELECT\n" +
            "    c.titulo as coleccion,\n" +
            "    h.provincia,\n" +
            "    COUNT(*) AS cantidad,\n" +
            "    ROW_NUMBER() OVER (\n" +
            "      PARTITION BY c.titulo\n" +
            "      ORDER BY COUNT(*) DESC, h.provincia\n" +
            "    ) AS rn\n" +
            "  FROM coleccion_hechos ch \n" +
            "  JOIN colecciones c \n" +
            "  ON ch.coleccion_id = c.id\n" +
            "  JOIN hechos h \n" +
            "  ON ch.hecho_id = h.id\n" +
            "  GROUP BY c.titulo, h.provincia\n" +
            ") subq\n" +
            "WHERE subq.rn = 1")
        .getResultList();

    List<EstadisticaProvMaxHechosColeccion.EstPMHColeccionDTO> lista = new ArrayList<>();

    for (Object[] r : listaDTO) {
      String coleccion = (String) r[0];
      String provincia = (String) r[1];
      reporte.add(new EstadisticaProvMaxHechosColeccion.EstPMHColeccionDTO(coleccion,provincia));
    }

    reporte.forEach(dto -> System.out.printf("Coleccion: %s | Provincia: %s%n", dto.coleccion(), dto.provincia()));

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

  public List<EstadisticaProvMaxHechosColeccion.EstPMHColeccionDTO> getReporte() {
    return reporte;
  }

}
