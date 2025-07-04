package ar.edu.utn.frba.dds.dominio;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestAbsoluto{

  private final Aabsoluta algoritmo = new Aabsoluta();

  private Hecho hecho(String titulo, TipoFuente fuente) {
    return new Hecho(titulo, "desc", "cat", 0.0, 0.0,
        LocalDate.now(), LocalDate.now(), fuente, null, true);
  }

  @Test
  public void hechoEsConsensuadoPorTodasLasFuentes() {
    Hecho h1 = hecho("Crisis", TipoFuente.DATASET );
    Hecho h2 = hecho("Crisis", TipoFuente.FUENTEPROXYDEMO);
    Hecho h3 = hecho("Crisis", TipoFuente.DINAMICA );

    List<Hecho> hechosNodo = List.of(h1, h2, h3); // 3 fuentes distintas, todas tienen el hecho

    assertTrue(algoritmo.estaConsensuado(h1, hechosNodo));
  }

  @Test
  public void hechoNoEsConsensuadoSiFaltaUnaFuente() {
    Hecho h1 = hecho("Crisis", TipoFuente.DATASET );
    Hecho h2 = hecho("Crisis", TipoFuente.FUENTEPROXYDEMO);

    Hecho otro = hecho("Otro", TipoFuente.DINAMICA );

    List<Hecho> hechosNodo = List.of(h1, h2, otro); // falta que la fuente C tenga el hecho

    assertFalse(algoritmo.estaConsensuado(h1, hechosNodo));
  }
}
