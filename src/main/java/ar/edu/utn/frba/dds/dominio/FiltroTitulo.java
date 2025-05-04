package ar.edu.utn.frba.dds.dominio;

import java.time.LocalDate;
import java.util.ArrayList;

public class FiltroTitulo implements Filtro {

  String titulo;

  public FiltroTitulo(String titulo) {
    this.titulo = titulo;
  }

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getTitulo().equals(titulo);
  }
}
