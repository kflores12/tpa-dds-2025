package ar.edu.utn.frba.dds.model;
import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.model.entities.algoritmosconcenso.AlgoritmoDeConsenso;
import ar.edu.utn.frba.dds.model.entities.fuentes.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestAbsoluto{


  private Hecho hecho(String titulo) {
    FuenteDinamica fuenteDinamica;
    fuenteDinamica = new FuenteDinamica();
    return new Hecho(
        titulo, "desc", "cat",
        0.0, 0.0,
        LocalDateTime.now(), LocalDateTime.now(),
        TipoFuente.DATASET, // el algoritmo ya no se basa en TipoFuente
        null,
        true,
        fuenteDinamica
    );
  }

  @Test
  public void hechoEsConsensuadoPorTodasLasFuentes() {
    Hecho h1 = hecho("Crisis");
    Hecho h2 = hecho("Crisis");
    Hecho h3 = hecho("Crisis");

    // Fuente 1 mockeada
    Fuente fuente1 = mock(Fuente.class);
    when(fuente1.getHechos()).thenReturn(List.of(h1));

    // Fuente 2 mockeada
    Fuente fuente2 = mock(Fuente.class);
    when(fuente2.getHechos()).thenReturn(List.of(h2, h1));

    // Fuente 3 mockeada
    Fuente fuente3 = mock(Fuente.class);
    when(fuente3.getHechos()).thenReturn(List.of(h3, h1));

    Agregador agregador = new Agregador(List.of(fuente1, fuente2, fuente3));

    Assertions.assertTrue(AlgoritmoDeConsenso.Aabsoluta.estaConsensuado(h1, agregador));
  }


  @Test
  public void hechoNoEsConsensuadoSiFaltaUnaFuente() {
    Hecho h1 = hecho("Crisis");
    Hecho h2 = hecho("Crisis");
    Hecho otro = hecho("Otro");


    Conexion conexionMock = mock(Conexion.class);
    FuenteProxyDemo f1 = new FuenteProxyDemo(conexionMock, "", List.of(h1));
    FuenteProxyDemo f2 = new FuenteProxyDemo(conexionMock, "http://fake-url", List.of(h2));

    FuenteDataSet f3 = mock(FuenteDataSet.class);
    when(f3.getHechos()).thenReturn(List.of(otro)); // esta fuente no tiene el hecho consensuado

    Agregador agregador = new Agregador(List.of(f1, f2, f3));

    Assertions.assertFalse(AlgoritmoDeConsenso.Aabsoluta.estaConsensuado(h1, agregador));
  }
}
