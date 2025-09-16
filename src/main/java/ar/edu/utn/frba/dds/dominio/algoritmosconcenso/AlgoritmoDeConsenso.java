package ar.edu.utn.frba.dds.dominio.algoritmosconcenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.fuentes.Agregador;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
@Table(name = "algoritmos_de_consenso")
public abstract class AlgoritmoDeConsenso {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  public AlgoritmoDeConsenso() {
  }

  public abstract boolean estaConsensuado(Hecho hecho, Fuente fuente);

  protected List<Fuente> descomponerFuente(Fuente fuente) {
    if (fuente instanceof Agregador agregador) {
      return agregador.getFuentes();
    }
    return List.of(fuente);
  }

}