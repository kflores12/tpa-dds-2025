package ar.edu.utn.frba.dds.dominio.estadistica;

import ar.edu.utn.frba.dds.dominio.Hecho;
import com.opencsv.CSVWriter;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

public class EstadisticaHoraHechosCategoria implements Estadistica, WithSimplePersistenceUnit {
  public String categoria;
  public LocalTime horaPicoCategoria;

  public EstadisticaHoraHechosCategoria(String categoria) {
    this.categoria = categoria;
  }

  @Override
  public void calcularEstadistica(){
    List<Hecho> hechos = entityManager()
        .createQuery("from Hecho h where h.categoria  = :categoria", Hecho.class )
        .setParameter("categoria" , this.categoria)
        .getResultList();

    this.horaPicoCategoria = hechos.stream()
        .map(( h -> h.getFechaAcontecimiento().toLocalTime()))
        .collect(Collectors.toMap(
            c -> c,
            c -> 1L,
            Long::sum
        ))
        .entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse(null);

  }


  @Override
  public void exportarEstadistica(String path) throws IOException {
    File file = new File(path);
    try (CSVWriter writer = new CSVWriter(new FileWriter(file, true))) {
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
