package ar.edu.utn.frba.dds.dominio;


import ar.edu.utn.frba.dds.dominio.algoritmosconcenso.AlgoritmoDeConsenso;
import ar.edu.utn.frba.dds.dominio.fuentes.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestMayoriaSimple {


  private Hecho hecho(String titulo) {
    return new Hecho(
        titulo, "desc", "cat",
        0.0, 0.0,
        LocalDateTime.now(), LocalDateTime.now(),
        TipoFuente.DATASET, null, true
    );
  }

  @Test
  public void hechoEsConsensuadoConMayoria_mockeado() {
    Hecho h1 = hecho("Corte");
    Hecho h2 = hecho("Corte");
    Hecho h3 = hecho("Corte");

    Hecho otro1 = hecho("Otro");
    Hecho otro2 = hecho("Otro");

    // Fuente 1 con "Corte"
    Fuente fuente1 = mock(Fuente.class);
    when(fuente1.getHechos()).thenReturn(List.of(h1));

    // Fuente 2 con "Corte"
    Fuente fuente2 = mock(Fuente.class);
    when(fuente2.getHechos()).thenReturn(List.of(h2, h1));

    // Fuente 3 con "Corte"
    Fuente fuente3 = mock(Fuente.class);
    when(fuente3.getHechos()).thenReturn(List.of(h3, h1));

    // Fuente 4 con "Otro"
    Fuente fuente4 = mock(Fuente.class);
    when(fuente4.getHechos()).thenReturn(List.of(otro1));

    // Fuente 5 con "Otro"
    Fuente fuente5 = mock(Fuente.class);
    when(fuente5.getHechos()).thenReturn(List.of(otro2));

    Agregador agregador = new Agregador(List.of(fuente1, fuente2, fuente3, fuente4, fuente5));

    Assertions.assertTrue(AlgoritmoDeConsenso.AmayoriaSimple.estaConsensuado(h1, agregador));
  }

  @Test
  public void hechoNoEsConsensuadoConMenosDeLaMitad() {
    Hecho h1 = hecho("Incendio");
    Hecho h2 = hecho("Incendio");

    Hecho otro1 = hecho("Otro");
    Hecho otro2 = hecho("Otro2");
    Hecho otro3 = hecho("Otro3");

    FuenteDinamica f1 = new FuenteDinamica();
    f1.getHechos().add(h1);

    Conexion conexionMock = mock(Conexion.class);
    FuenteProxyDemo f2 = new FuenteProxyDemo(conexionMock, "http://fake", List.of(h2));

    FuenteDataSet f3 = mock(FuenteDataSet.class);
    when(f3.getHechos()).thenReturn(List.of(otro1));

    FuenteDataSet f4 = mock(FuenteDataSet.class);
    when(f4.getHechos()).thenReturn(List.of(otro2));

    FuenteDataSet f5 = mock(FuenteDataSet.class);
    when(f5.getHechos()).thenReturn(List.of(otro3));

    Agregador agregador = new Agregador(List.of(f1, f2, f3, f4, f5));

    // Solo 2 de 5 fuentes tienen el hecho → no hay mayoría
    Assertions.assertFalse(AlgoritmoDeConsenso.AmayoriaSimple.estaConsensuado(h1, agregador));
  }
}
