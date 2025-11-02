package ar.edu.utn.frba.dds.model.entities.fuentes;

import ar.edu.utn.frba.dds.model.entities.Hecho;

import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("FUENTE_DINAMICA")
public class FuenteDinamica extends Fuente {

  public FuenteDinamica() {
  }

  @Override
  public List<Hecho> getHechos() {

    return List.of();
  }

  @Override
  public void actualizarHechos() {
  }
}