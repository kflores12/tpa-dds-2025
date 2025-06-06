package ar.edu.utn.frba.dds.dominio;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class CriterioConverter {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

  public static String toQuery(List<Criterio> criterios) {
    return criterios.stream()
        .map(CriterioConverter::criterioToParams)
        .filter(params -> !params.isEmpty())
        .collect(Collectors.joining("&"));
  }

  private static String criterioToParams(Criterio criterio) {
    if (criterio instanceof CriterioTitulo) {
      return "titulo=" + encodeValue(((CriterioTitulo) criterio).titulo);
    } else if (criterio instanceof CriterioFecha) {
      return "fecha=" + ((CriterioFecha) criterio).fecha.format(DATE_FORMATTER);
    } else if (criterio instanceof CriterioUbicacion) {
      CriterioUbicacion ubicacion = (CriterioUbicacion) criterio;
      return "latitud=" + ubicacion.latitud + "&longitud=" + ubicacion.longitud;
    } else if (criterio instanceof CriterioCategoria) {
      return "categoria=" + encodeValue(((CriterioCategoria) criterio).categoria);
    } else if (criterio instanceof CriterioFechaCarga) {
      return "fecha_carga=" + ((CriterioFechaCarga) criterio).fecha.format(DATE_FORMATTER);
    }
    return "";
  }

  private static String encodeValue(String value) {
    return java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8);
  }
}