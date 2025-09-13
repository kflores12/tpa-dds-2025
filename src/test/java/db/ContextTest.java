package db;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.GeneradorHandleUuid;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.criterios.Criterio;
import ar.edu.utn.frba.dds.dominio.criterios.CriterioBase;
import ar.edu.utn.frba.dds.dominio.estadistica.*;
import ar.edu.utn.frba.dds.dominio.fuentes.Agregador;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDataSet;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioHechos;
import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioSolicitudesDeCarga;
import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioSolicitudesEliminacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.solicitudes.FactorySolicitudDeEliminacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudDeEliminacion;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContextTest implements SimplePersistenceTest {

  Hecho hecho;
  Hecho hecho2;
  Hecho hecho3;

  @BeforeEach
  public void fixtureBeforeEach() {
  hecho = new Hecho(
      "Corte de luz modificado",
      "Corte de luz en zona oeste",
      "cortes", -27.782412, -63.252387,
      LocalDateTime.of(2025, 1, 18,12,00),
      LocalDateTime.now(),
      TipoFuente.DINAMICA,
      "http://multimediavalue",
      Boolean.TRUE
  );

  hecho2 = new Hecho(
      "Corte de luz modificado",
      "Corte de luz en zona oeste",
      "cortes", -43.6844,-69.2717,
      LocalDateTime.of(2025, 1, 18,12,00),
      LocalDateTime.now(),
      TipoFuente.DINAMICA,
      "http://multimediavalue",
      Boolean.TRUE
  );

  hecho3 = new Hecho(
      "incendio en new York",
      "Corte de luz en zona oeste",
      "incedio", -43.6834,-69.2713,
      LocalDateTime.of(2025, 1, 18,14,00),
      LocalDateTime.now(),
      TipoFuente.DINAMICA,
      "http://multimediavalue",
      Boolean.TRUE
  );
  }

  @Test
  void contextUp() {
    assertNotNull(entityManager());
  }

  @Test
  void contextUpWithTransaction() throws Exception {
    withTransaction(() -> {
    });
  }

  @Test
  public void testEstadisticaCategoriaMaxima() {
    RepositorioHechos repositorio = new RepositorioHechos();

    repositorio.cargarHecho(hecho);
    repositorio.cargarHecho(hecho2);
    repositorio.cargarHecho(hecho3);

    EstadisticaCategoriaMaxima estadisticaCM = new EstadisticaCategoriaMaxima ();
    estadisticaCM.calcularEstadistica();

    Assertions.assertEquals("cortes", estadisticaCM.getCategoriaMax());

  }

  @Test
  public void testEstadisticaCantidadSpam() {
    RepositorioHechos repositorioH = new RepositorioHechos();

    FactorySolicitudDeEliminacion factory;
    DetectorDeSpam inter = mock(DetectorDeSpam.class);
    when(inter.esSpam("Motivo válido")).thenReturn(false);
    when(inter.esSpam("Motivo invalido")).thenReturn(true);
    RepositorioSolicitudesEliminacion  repositorio = new RepositorioSolicitudesEliminacion();

    repositorioH.cargarHecho(hecho); //Se necesita cargar el hecho para poder cargar la solicitud

    factory = new FactorySolicitudDeEliminacion(inter);

    SolicitudDeEliminacion solicitud1 = factory.crear(hecho, "Motivo invalido");
    repositorio.cargarSolicitudEliminacion(solicitud1);


    EstadisticaCantidadSpam estadisticaCS = new EstadisticaCantidadSpam();
    estadisticaCS.calcularEstadistica();

    Assertions.assertEquals(1, estadisticaCS.getCantidadSpam());

  }

  @Test
  public void testEstadisticaProvMaxHechosCategoria() {
    RepositorioHechos repositorioH = new RepositorioHechos();

    Hecho hecho = new Hecho(
        "Corte de luz modificado",
        "Corte de luz en zona oeste",
        "cortes", -27.782412, -63.252387,
        LocalDateTime.of(2025, 1, 18,12,00),
        LocalDateTime.now(),
        TipoFuente.DINAMICA,
        "http://multimediavalue",
        Boolean.TRUE
    );

    repositorioH.cargarHecho(hecho);

    EstadisticaProvMaxHechosCategoria estadisticaPMHC = new EstadisticaProvMaxHechosCategoria("cortes");
    estadisticaPMHC.calcularEstadistica();

    Assertions.assertEquals("Ciudad Autónoma de Buenos Aires", estadisticaPMHC.getProvinciaMax());
  }

  @Test
  public void testEstadisticaProvMaxHechosColeccion() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    RepositorioHechos repositorioHechos = new RepositorioHechos();
    FuenteDinamica dinamica = new FuenteDinamica();
    CriterioBase criterio = new CriterioBase();
    List<Criterio> criterios = new ArrayList<>(Arrays.asList(criterio));

    repositorioHechos.cargarHecho(hecho);
    repositorioHechos.cargarHecho(hecho2);
    repositorioHechos.cargarHecho(hecho3);

    dinamica.actualiza(repositorioHechos);

    Coleccion coleccion = new Coleccion("incendios forestales",
        "incendios en la patagonia",
        dinamica, criterios, generador.generar(),null);

    EstadisticaProvMaxHechosColeccion estadisticaPMHC = new EstadisticaProvMaxHechosColeccion(coleccion);
    estadisticaPMHC.calcularEstadistica();

    Assertions.assertEquals("Chubut", estadisticaPMHC.getProvinciaMax());
  }

  //CSV
  @Test
  public void testExportarEstadisticaCategoriaMaxima() throws Exception {
    RepositorioHechos repositorio = new RepositorioHechos();
    repositorio.cargarHecho(hecho);
    repositorio.cargarHecho(hecho2);
    repositorio.cargarHecho(hecho3);

    EstadisticaCategoriaMaxima estadistica = new EstadisticaCategoriaMaxima();
    estadistica.calcularEstadistica();

    String path = "estadisticas_categoria_maxima.csv";
    estadistica.exportarEstadistica(path);

    List<String> lineas = Files.readAllLines(Paths.get(path));
    Assertions.assertTrue(lineas.get(0).contains("Fecha") && lineas.get(0).contains("CategoriaMasFrecuente"));
    Assertions.assertTrue(lineas.stream().anyMatch(l -> l.contains("cortes")));
  }

  @Test
  public void testExportarEstadisticaCantidadSpam() throws Exception {
    RepositorioHechos repositorioH = new RepositorioHechos();
    DetectorDeSpam inter = mock(DetectorDeSpam.class);
    when(inter.esSpam("spam")).thenReturn(true);
    when(inter.esSpam("ok")).thenReturn(false);

    RepositorioSolicitudesEliminacion repositorio = new RepositorioSolicitudesEliminacion();
    repositorioH.cargarHecho(hecho);

    FactorySolicitudDeEliminacion factory = new FactorySolicitudDeEliminacion(inter);
    SolicitudDeEliminacion solicitudSpam = factory.crear(hecho, "spam");
    repositorio.cargarSolicitudEliminacion(solicitudSpam);

    EstadisticaCantidadSpam estadistica = new EstadisticaCantidadSpam();
    estadistica.calcularEstadistica();

    String path = "estadisticas_cantidad_spam.csv";
    estadistica.exportarEstadistica(path);

    List<String> lineas = Files.readAllLines(Paths.get(path));
    Assertions.assertTrue(lineas.get(0).contains("Fecha") && lineas.get(0).contains("CantidadSpam"));
    Assertions.assertTrue(lineas.stream().anyMatch(l -> l.contains("1")));
  }

  @Test
  public void testExportarEstadisticaProvMaxHechosCategoria() throws Exception {
    RepositorioHechos repositorio = new RepositorioHechos();
    repositorio.cargarHecho(hecho); // está en Santiago del Estero

    EstadisticaProvMaxHechosCategoria estadistica = new EstadisticaProvMaxHechosCategoria("cortes");
    estadistica.calcularEstadistica();

    String path = "estadisticas_categoria_hechosmaximos.csv";
    estadistica.exportarEstadistica(path);

    List<String> lineas = Files.readAllLines(Paths.get(path));
    Assertions.assertTrue(lineas.get(0).contains("Fecha") && lineas.get(0).contains("Provincia") && lineas.get(0).contains("Categoria"));
    Assertions.assertTrue(lineas.stream().anyMatch(l -> l.contains("Ciudad Autónoma de Buenos Aires")));
  }

  @Test
  public void testExportarEstadisticaProvMaxHechosColeccion() throws Exception {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    RepositorioHechos repositorioHechos = new RepositorioHechos();
    FuenteDinamica dinamica = new FuenteDinamica();
    CriterioBase criterio = new CriterioBase();
    List<Criterio> criterios = new ArrayList<>(Arrays.asList(criterio));

    repositorioHechos.cargarHecho(hecho);
    repositorioHechos.cargarHecho(hecho2);
    repositorioHechos.cargarHecho(hecho3);

    dinamica.actualiza(repositorioHechos);

    Coleccion coleccion = new Coleccion("incendios forestales",
        "incendios en la patagonia",
        dinamica, criterios, generador.generar(), null);

    EstadisticaProvMaxHechosColeccion estadistica = new EstadisticaProvMaxHechosColeccion(coleccion);
    estadistica.calcularEstadistica();

    String path = "estadisticas_hechosmaximos_coleccion.csv";
    estadistica.exportarEstadistica(path);

    List<String> lineas = Files.readAllLines(Paths.get(path));
    Assertions.assertTrue(lineas.get(0).contains("Fecha") && lineas.get(0).contains("Provincia") && lineas.get(0).contains("Coleccion"));
    Assertions.assertTrue(lineas.stream().anyMatch(l -> l.contains("Chubut")));
  }

  @Test
  public void testExportarEstadisticaHoraPicoCategoria() throws Exception {
    RepositorioHechos repositorio = new RepositorioHechos();

    // Hecho con hora específica
    Hecho hecho = new Hecho(
        "Corte de luz",
        "Zona norte sin servicio",
        "cortes",
        -34.60,
        -58.38,
        LocalDateTime.of(2025, 9, 12, 14, 30), // ← hora 14:30
        LocalDateTime.now(),
        TipoFuente.DINAMICA,
        "archivo.png",
        true
    );

    withTransaction(() -> {
      repositorio.cargarHecho(hecho);
    });

    EstadisticaHoraHechosCategoria estadistica = new EstadisticaHoraHechosCategoria("cortes");
    estadistica.calcularEstadistica();

    String path = "estadisticas_categoria_horaspico.csv";
    estadistica.exportarEstadistica(path);

    List<String> lineas = Files.readAllLines(Paths.get(path));

    // Verifica encabezado
    Assertions.assertTrue(lineas.get(0).contains("Fecha") && lineas.get(0).contains("Categoria") && lineas.get(0).contains("HoraPico"));

    // Verifica que la hora pico esté presente
    Assertions.assertTrue(lineas.stream().anyMatch(l -> l.contains("14:30")));
  }
}

