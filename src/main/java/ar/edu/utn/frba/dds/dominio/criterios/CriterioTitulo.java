package ar.edu.utn.frba.dds.dominio.criterios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("TITULO")
public class CriterioTitulo extends Criterio {
  @Column
  String titulo;

  public CriterioTitulo() {
  }

  public CriterioTitulo(String titulo) {
    this.titulo = titulo.toLowerCase();
  }

  public boolean aplicarFiltro(Hecho hecho) {
    return hecho.getTitulo().toLowerCase().contains(titulo);
  }

}
