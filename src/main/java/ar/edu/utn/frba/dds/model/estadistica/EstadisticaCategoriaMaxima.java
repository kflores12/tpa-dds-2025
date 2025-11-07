package ar.edu.utn.frba.dds.model.estadistica;

import com.opencsv.CSVWriter;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EstadisticaCategoriaMaxima implements Estadistica, WithSimplePersistenceUnit {
  List<categoriaMaxDTO> reporte = new ArrayList<categoriaMaxDTO>();

  public record categoriaMaxDTO(String categoria, Integer cantidad_hechos) {}

  @Override public void calcularEstadistica() {
    List<Object[]> listaDTO = entityManager()
        .createQuery("SELECT " +
            "h.categoria," +
            "COUNT(h) as cantidad_hechos " +
            "FROM Hecho h " +
            "GROUP BY h.categoria " +
            "ORDER BY COUNT(h) DESC",
            Object[].class
        ).getResultList();

    List<categoriaMaxDTO> lista = new ArrayList<>();

    for (Object[] r : listaDTO) {
      String categoria = (String) r[0];
      int cantidad_hechos  = ((Number) r[1]).intValue();
      reporte.add(new categoriaMaxDTO(categoria, cantidad_hechos));
    }

    reporte.forEach(dto -> System.out.printf("Nombre: %s | Cantidad: %d%n", dto.categoria(), dto.cantidad_hechos()));
  }

  @Override
  public void exportarEstadistica(String path) throws IOException {
    File file = new File(path);

    if (file.exists()) {
      boolean eliminado = file.delete();
    }

    try (CSVWriter writer = new CSVWriter(
        new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
      String[] header = {"Fecha", "Categoria", "Cantidad Hechos"};

      if (file.length() == 0) {
        writer.writeNext(header);
      }

      reporte.forEach(dto ->
          writer.writeNext(new String[]{LocalDateTime.now().toString(),
              dto.categoria() != null ? dto.categoria() : "N/A",
              String.valueOf(dto.cantidad_hechos() != null ? dto.cantidad_hechos() : 0)}));

    }
  }

  public List<categoriaMaxDTO> getReporte() {
    return reporte;
  }

}


