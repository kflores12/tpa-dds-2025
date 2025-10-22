package ar.edu.utn.frba.dds.model.dtos;

import com.opencsv.bean.CsvBindByName;

public class HechoDto {
  @CsvBindByName
  private String titulo;
  @CsvBindByName
  private String descripcion;
  @CsvBindByName
  private String categoria;
  @CsvBindByName
  private Double latitud;
  @CsvBindByName
  private Double longitud;
  @CsvBindByName
  private String fechaAcontecimiento;
  @CsvBindByName
  private String multimedia;

  public String getTitulo() {
    return this.titulo;
  }

  public String getDescripcion() {
    return this.descripcion;
  }

  public String getCategoria() {
    return this.categoria;
  }

  public Double getLatitud() {
    return this.latitud;
  }

  public Double getLongitud() {
    return this.longitud;
  }

  public String getFechaAcontecimiento() {
    return this.fechaAcontecimiento;
  }

  public String getMultimedia() {
    return this.multimedia;
  }

  public boolean contieneTodosLosCampos() {
    return
            (titulo != null && !titulo.isEmpty())
            &&
            (descripcion != null && !descripcion.isEmpty())
            &&
            (categoria != null && !categoria.isEmpty())
            &&
            latitud != null
            &&
            longitud != null
            &&
            fechaAcontecimiento != null;
  }
}
