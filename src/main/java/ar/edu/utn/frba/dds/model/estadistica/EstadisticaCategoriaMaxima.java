package ar.edu.utn.frba.dds.model.estadistica;

import com.opencsv.CSVWriter;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import javax.persistence.NoResultException;

public class EstadisticaCategoriaMaxima implements Estadistica, WithSimplePersistenceUnit {
  private String categoriaMax;


  @Override public void calcularEstadistica() {
    try {
      this.categoriaMax = entityManager()
          .createQuery(
              "SELECT h.categoria "
                  + "FROM Hecho h "
                  + "GROUP BY h.categoria "
                  + "ORDER BY COUNT(h) DESC",
              String.class
          )
          .setMaxResults(1)
          .getSingleResult();
    } catch (NoResultException e) {
      this.categoriaMax = null;
    }
  }

  @Override
  public void exportarEstadistica(String path) throws IOException {
    File file = new File(path);

    if (file.exists()) {
      boolean eliminado = file.delete();
    }

    try (CSVWriter writer = new CSVWriter(
        new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
      String[] header = {"Fecha", "CategoriaMasFrecuente"};
      String[] data = {LocalDateTime.now().toString(), categoriaMax != null ? categoriaMax : "N/A"};

      // Escribir encabezado solo si el archivo está vacío
      if (file.length() == 0) {
        writer.writeNext(header);
      }
      writer.writeNext(data);
    }
  }

  public String getCategoriaMax() {
    return categoriaMax;
  }

}


