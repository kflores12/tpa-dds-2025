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
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticaProvMaxHechosColeccion implements Estadistica, WithSimplePersistenceUnit {
  private String  provincia;
  private final Coleccion coleccion;

  public EstadisticaProvMaxHechosColeccion(Coleccion coleccion) {
    this.coleccion = coleccion;
  }

  @Override public void calcularEstadistica() {

    List<Hecho> hechosDeLaColeccion = this.coleccion.obtnerHechos();

    Map<String, Long> conteoPorProvincia = new HashMap<>();

    for (Hecho hecho : hechosDeLaColeccion) {
      String provincia = getProvincia(hecho.getLatitud(), hecho.getLongitud());
      conteoPorProvincia.merge(provincia, 1L, Long::sum);
    }

    this.provincia = conteoPorProvincia.entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse(null);

  }

  @Override public void exportarEstadistica(String path) throws IOException {
    File file = new File(path);
    if (file.exists()) {
      boolean eliminado = file.delete();
    }
    try (CSVWriter writer = new CSVWriter(
        new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
      String[] header = {"Fecha", "Coleccion", "ProvinciaMaxima"};
      String[] data = {
          LocalDateTime.now().toString(),
          coleccion.getTitulo(),
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
