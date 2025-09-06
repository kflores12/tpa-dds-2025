package ar.edu.utn.frba.dds.dominio.criterios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CATEGORIA")
public class CriterioCategoria extends Criterio {
  @Column
  String categoria;

  public CriterioCategoria(String categoria) {

    this.categoria = categoria.toLowerCase();
  }

  public CriterioCategoria() {
  }

  @Override
  public boolean aplicarFiltro(Hecho hecho) {
    String categoriaHecho = hecho.getCategoria().toLowerCase();
    return categoria.contains(categoriaHecho);
  }



}
