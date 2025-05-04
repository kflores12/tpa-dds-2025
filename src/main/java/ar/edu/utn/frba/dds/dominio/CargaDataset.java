package ar.edu.utn.frba.dds.dominio;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CargaDataset {

  public List<Hecho> cargarHechosDesdeCsv(String rutaArchivo) {
    List<Hecho> hechosExtraidos = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8))) {

      String linea;

      while ((linea = br.readLine()) != null) {
        String[] campos = linea.split(",", -1);

        if (campos.length < 6) {
          continue;
        }

        String titulo = campos[0];
        String descripcion = campos[1];
        String categoriaStr = campos[2];
        Double latitud = Double.parseDouble(campos[3]);
        Double longitud = Double.parseDouble(campos[4]);
        LocalDate fechaHecho = LocalDate.parse(campos[5], formatter);

        Fuente origen = Fuente.DATASET;
        Categoria categoria = Categoria.valueOf(categoriaStr);
        Contribuyente contribuyente = null;
        TipoDeHecho tipo = TipoDeHecho.TEXTO;

        Hecho nuevoHecho = new Hecho(titulo, descripcion, categoria, latitud,
            longitud, fechaHecho, LocalDate.now(), origen, contribuyente, tipo);

        hechosExtraidos.add(nuevoHecho);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return hechosExtraidos;
  }
}