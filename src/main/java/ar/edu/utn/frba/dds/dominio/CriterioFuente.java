package ar.edu.utn.frba.dds.dominio;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CriterioFuente implements Criterio {

  TipoFuente fuente;

  public CriterioFuente(TipoFuente fuente) {

    this.fuente = fuente;
  }

  @Override
  public boolean aplicarFiltro(Hecho hecho) {
    TipoFuente fuenteHecho = hecho.getFuente();
    return fuente == fuenteHecho;
  }

  public String toQuery() {
    return "fuente=" + URLEncoder.encode(fuente.toString(), StandardCharsets.UTF_8);
  }

}
