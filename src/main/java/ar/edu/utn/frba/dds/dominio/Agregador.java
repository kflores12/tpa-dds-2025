package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agregador implements  Fuente {
  private final List<Fuente> fuentes;
  private final List<Hecho> copiaLocal;

  public Agregador(List<Fuente> fuentes) {
    this.fuentes =  requireNonNull(fuentes);
    this.copiaLocal = new ArrayList<>();
  }

  @Override
  public List<Hecho> getHechos() {
    return new ArrayList<>(copiaLocal);
  }

  public void AgregarHechos() {
    List<Hecho> hechosDeFuentes = fuentes.stream()
        .flatMap(f -> f.getHechos().stream())
        .toList();

    List<Hecho> hechosTotales = new ArrayList<>(copiaLocal);
    hechosTotales.addAll(hechosDeFuentes);
    copiaLocal.clear();
    copiaLocal.addAll(filtrarDuplicados(hechosTotales).values());
  }

  public Map<String, Hecho> filtrarDuplicados(List<Hecho> duplicados) {
    Map<String, Hecho> hechosUnicos = new HashMap<>();
    for (Hecho hecho : duplicados) {
      hechosUnicos.put(hecho.getTitulo(), hecho);
    }
    return hechosUnicos;
  }


}
