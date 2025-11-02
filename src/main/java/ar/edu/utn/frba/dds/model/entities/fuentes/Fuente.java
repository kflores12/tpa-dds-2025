package ar.edu.utn.frba.dds.model.entities.fuentes;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import java.util.List;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "fuentes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_fuente")
public abstract class Fuente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  public Fuente() {
  }

  public Long getId() {
    return id;
  }



  public abstract List<Hecho> getHechos();

  public abstract void actualizarHechos();
}
