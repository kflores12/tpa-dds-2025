package ar.edu.utn.frba.dds.dominio;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestMultiplesMenciones {

  private final AmultiplesMeciones algoritmo = new AmultiplesMeciones();

  private Hecho crearHecho(String titulo, String descripcion, TipoFuente fuente) {
    return new Hecho(titulo, descripcion, "Categoria", 1.0, 1.0,
        LocalDate.now(), LocalDate.now(), fuente, null, true);
  }
  LocalDate hace3dias = LocalDate.now().minusDays(3);

  @Test
  public void hechoEsConsensuadoConMultiplesFuentesYSinConflictos() {
    Hecho h1 = crearHecho("Inundación", "Barrio A", TipoFuente.FUENTEAPI);
    Hecho h2 = crearHecho("Inundación", "Barrio A", TipoFuente.DINAMICA);
    List<Hecho> hechosNodo = List.of(h1, h2);
    boolean resultado = algoritmo.estaConsensuado(h1, hechosNodo);
    assertTrue(resultado);
  }

  @Test
  public void hechoNoEsConsensuadoPorConflictoDeTitulos() {
    Hecho h1 = crearHecho("Inundación", "Barrio A", TipoFuente.FUENTEAPI);
    Hecho h2 = crearHecho("Inundación", "Barrio Z", TipoFuente.DINAMICA);

    List<Hecho> hechosNodo = List.of(h1, h2);

    assertFalse(algoritmo.estaConsensuado(h1, hechosNodo));
  }

  @Test
  public void hechoNoEsConsensuadoPorUnaSolaFuente() {
    Hecho h1 = crearHecho("Corte de luz", "Zona sur", TipoFuente.DATASET);

    List<Hecho> hechosNodo = List.of(h1);

    assertFalse(algoritmo.estaConsensuado(h1, hechosNodo));
  }

}

