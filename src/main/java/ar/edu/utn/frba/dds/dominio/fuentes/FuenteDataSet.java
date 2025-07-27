package ar.edu.utn.frba.dds.dominio.fuentes;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.HechoDto;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FuenteDataSet implements Fuente {

  private final String ruta; //Agrego final para que no pueda ser modificada post inicializacion.
  private final String formatoFecha;
  private final char separador;

  public FuenteDataSet(String ruta, String formatoFecha, char separador) {
    this.ruta = requireNonNull(ruta);
    this.formatoFecha = requireNonNull(formatoFecha);
    this.separador = separador;
  }

  @Override
  public List<Hecho> getHechos() {

    List<Hecho> hechos = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(this.formatoFecha);

    CSVParser parser = new CSVParserBuilder()
        .withSeparator(this.separador)
        .withIgnoreLeadingWhiteSpace(true)
        .build();

    try (
         Reader inputReader = new InputStreamReader(new FileInputStream(ruta),
             StandardCharsets.UTF_8);
         CSVReader csvReader = new CSVReaderBuilder(inputReader)
             .withCSVParser(parser)
             .build()
    ) {
      HeaderColumnNameMappingStrategy<HechoDto> strategy =
          new HeaderColumnNameMappingStrategy<>();
      strategy.setType(HechoDto.class);
      CsvToBean<HechoDto> csvToBean = new CsvToBeanBuilder<HechoDto>(csvReader)
          .withMappingStrategy(strategy)
          .withIgnoreLeadingWhiteSpace(true)
          .build();
      try {
        for (HechoDto hechodto : csvToBean) {
          if (!hechodto.contieneTodosLosCampos()) {
            return new ArrayList<>();
          }

          Hecho hecho = new Hecho(
              hechodto.getTitulo(),
              hechodto.getDescripcion(),
              hechodto.getCategoria(),
              hechodto.getLatitud(),
              hechodto.getLongitud(),
              LocalDate.parse(hechodto.getFechaAcontecimiento(), formatter),
              LocalDate.now(),
              TipoFuente.DATASET,
              hechodto.getMultimedia(),
              Boolean.TRUE
          );

          hechos.add(hecho);
        }
      } catch (Exception e) {
        return new ArrayList<>();
      }
    } catch (IOException e) {
      return new ArrayList<>();
    }
    
    return new ArrayList<>(hechos);
  }
  
}
