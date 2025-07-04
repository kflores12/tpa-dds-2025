package ar.edu.utn.frba.dds.dominio;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestMayoriaSimple{

  private final AmayoriaSimple algoritmo = new AmayoriaSimple();

  private Hecho hecho(String titulo, TipoFuente fuente) {
    return new Hecho(titulo, "desc", "cat", 0.0, 0.0,
        LocalDate.now(), LocalDate.now(), fuente , null, true);
  }

  @Test
  public void hechoEsConsensuadoConMayoria() {
    Hecho h1 = hecho("Corte", TipoFuente.FUENTEPROXYDEMO);
    Hecho h2 = hecho("Corte", TipoFuente.DATASET);
    Hecho h3 = hecho("Corte", TipoFuente.DINAMICA);

    // Total fuentes = 5 (agregamos 2 extras con otro hecho)
    Hecho otro1 = hecho("Otro", TipoFuente.FUENTEPROXYDEMO);
    Hecho otro2 = hecho("Otro", TipoFuente.DINAMICA);

    List<Hecho> hechosNodo = List.of(h1, h2, h3, otro1, otro2);

    assertTrue(algoritmo.estaConsensuado(h1, hechosNodo));
  }

  @Test
  public void hechoNoEsConsensuadoConMenosDeLaMitad() {
    Hecho h1 = hecho("Incendio", TipoFuente.DATASET);
    Hecho h2 = hecho("Incendio2", TipoFuente.FUENTEPROXYDEMO);

    Hecho otro1 = hecho("Otro", TipoFuente.DATASET);
    Hecho otro2 = hecho("Otro2", TipoFuente.FUENTEPROXYDEMO);
    Hecho otro3 = hecho("Otro3", TipoFuente.FUENTEAPI);

    List<Hecho> hechosNodo = List.of(h1, h2, otro1, otro2); // 2/5 < 50%

    assertTrue(algoritmo.estaConsensuado(h1, hechosNodo));
  }
}
