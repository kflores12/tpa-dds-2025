package ar.edu.utn.frba.dds.dominio;
import ar.edu.utn.frba.dds.dominio.algoritmosconcenso.AlgoritmoDeConsenso;
import ar.edu.utn.frba.dds.dominio.fuentes.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestMultiplesMenciones {

  private Hecho hecho(String titulo, String descripcion) {
    return new Hecho(
        titulo, descripcion, "Categoria",
        1.0, 1.0,
        LocalDateTime.now(), LocalDateTime.now(),
        TipoFuente.DATASET, null, true
    );
  }

  @Test
  public void hechoEsConsensuadoPorMúltiplesFuentesSinConflictos_mockeado() {
    Hecho h1 = hecho("Inundación", "Barrio A");
    Hecho h2 = hecho("Inundación", "Barrio A");

    // Fuente 1 con h1
    Fuente fuente1 = mock(Fuente.class);
    when(fuente1.getHechos()).thenReturn(List.of(h1));

    // Fuente 2 con h2
    Fuente fuente2 = mock(Fuente.class);
    when(fuente2.getHechos()).thenReturn(List.of(h1));

    // Fuente 3 sin hechos del mismo título
    Fuente fuente3 = mock(Fuente.class);
    when(fuente3.getHechos()).thenReturn(List.of()); // no aporta conflicto

    Agregador agregador = new Agregador(List.of(fuente1, fuente2, fuente3));

    assertTrue(AlgoritmoDeConsenso.AmultiplesMenciones.estaConsensuado(h1, agregador)); // cumple el criterio
  }

  @Test
  public void hechoNoEsConsensuadoPorConflictoDeTitulos() {
    Hecho h1 = hecho("Inundación", "Barrio A");
    Hecho h2 = hecho("Inundación", "Barrio Z"); // mismo título, distinto contenido

    FuenteDinamica f1 = new FuenteDinamica();
    f1.getHechos().add(h1);

    FuenteDataSet f2 = mock(FuenteDataSet.class);
    when(f2.getHechos()).thenReturn(List.of(h2));

    Agregador agregador = new Agregador(List.of(f1, f2));

    assertFalse(AlgoritmoDeConsenso.AmultiplesMenciones.estaConsensuado(h1, agregador));
  }

  @Test
  public void hechoNoEsConsensuadoPorUnaSolaFuente() {
    Hecho h1 = hecho("Corte de luz", "Zona sur");

    FuenteDinamica f1 = new FuenteDinamica();
    f1.getHechos().add(h1);

    Agregador agregador = new Agregador(List.of(f1));

    assertFalse(AlgoritmoDeConsenso.AmultiplesMenciones.estaConsensuado(h1, agregador));
  }
}

