package ar.edu.utn.frba.dds.model.entities.algoritmosconcenso;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.model.entities.fuentes.Agregador;
import ar.edu.utn.frba.dds.model.entities.fuentes.Fuente;
import java.util.List;
import java.util.Objects;


public enum AlgoritmoDeConsenso {

  Aabsoluta {
    @Override
    public boolean estaConsensuado(Hecho hecho, Fuente fuente) {
      List<Fuente> fuentes = descomponerFuente(fuente);

      return fuentes.stream()
          .allMatch(f -> f.getHechos().stream().anyMatch(h -> h.esIgual(hecho)));
    }
  },
  AmayoriaSimple {
    @Override
    public boolean estaConsensuado(Hecho hecho, Fuente fuente) {
      List<Fuente> fuentes = descomponerFuente(fuente);
      int total = fuentes.size();
      if (total == 0) {
        return false;
      }
      long conteo = fuentes.stream()
          .filter(f -> f.getHechos().stream().anyMatch(h -> h.esIgual(hecho)))
          .count();

      return conteo >= Math.ceil(total / 2.0);
    }
    },
  AmultiplesMenciones {
    @Override
    public boolean estaConsensuado(Hecho hecho, Fuente fuente) {
      List<Fuente> fuentes = descomponerFuente(fuente);

      // Fuentes que tienen un hecho exactamente igual
      long fuentesQueTienenHecho = fuentes.stream()
          .filter(f -> f.getHechos().stream().anyMatch(h -> h.esIgual(hecho)))
          .count();

      boolean hayConflicto = fuentes.stream()
          .anyMatch(f -> f.getHechos().stream()
              .anyMatch(h -> mismoTitulo(h, hecho) && !h.esIgual(hecho)));

      return fuentesQueTienenHecho >= 2 && !hayConflicto;
    }

    private boolean mismoTitulo(Hecho a, Hecho b) {
      if (a == b) {
        return true;
      }
      if (a == null || b == null) {
        return false;
      }
      return Objects.equals(a.getTitulo(), b.getTitulo());
    }
  };

  public abstract boolean estaConsensuado(Hecho hecho, Fuente fuente);

  protected List<Fuente> descomponerFuente(Fuente fuente) {
    if (fuente instanceof Agregador agregador) {
      return agregador.getFuentes();
    }
    return List.of(fuente);
  }

}