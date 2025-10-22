package ar.edu.utn.frba.dds.model.estadistica;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LocalizadorDeProvincias {

  public static String getProvincia(double lat, double lon) {
    try {
      String urlStr = "https://apis.datos.gob.ar/georef/api/ubicacion?lat="
          + lat + "&lon=" + lon;

      URL url = new URL(urlStr);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");

      BufferedReader in = new BufferedReader(
          new InputStreamReader(conn.getInputStream(), "UTF-8"));
      StringBuilder content = new StringBuilder();
      String line;
      while ((line = in.readLine()) != null) {
        content.append(line);
      }
      in.close();
      conn.disconnect();

      // Extraer el nombre de la provincia
      String respuesta = content.toString();
      int idx = respuesta.indexOf("\"provincia\"");
      if (idx != -1) {
        int nombreIdx = respuesta.indexOf("\"nombre\"", idx);
        int start = respuesta.indexOf(":", nombreIdx) + 2;
        int end = respuesta.indexOf("\"", start);
        return respuesta.substring(start, end);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
