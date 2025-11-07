package ar.edu.utn.frba.dds.model.entities.fuentes;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.model.dtos.HechoDto;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("FUENTE_DATASET")
public class FuenteDataSet extends Fuente {
  @Column
  private String ruta; //Agrego final para que no pueda ser modificada post inicializacion.
  @Column
  private String formatoFecha;
  @Column
  private char separador;

  public FuenteDataSet(String ruta, String formatoFecha, char separador) {
    this.ruta = requireNonNull(ruta);
    this.formatoFecha = requireNonNull(formatoFecha);
    this.separador = separador;
  }

  public FuenteDataSet() {
  }

  public String getRuta() {
    return ruta;
  }

  public void setRuta(String ruta) {
    this.ruta = ruta;
  }

  public String getFormatoFecha() {
    return formatoFecha;
  }

  public void setFormatoFecha(String formatoFecha) {
    this.formatoFecha = formatoFecha;
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
              LocalDateTime.parse(hechodto.getFechaAcontecimiento(), formatter),
              LocalDateTime.now(),
              TipoFuente.DATASET,
              hechodto.getMultimedia(),
              Boolean.TRUE,
              this
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

  @Override
  public void actualizarHechos() {

  }

}
