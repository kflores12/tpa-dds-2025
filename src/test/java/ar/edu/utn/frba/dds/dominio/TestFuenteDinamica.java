package ar.edu.utn.frba.dds.dominio;

import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.dominio.criterios.Criterio;
import ar.edu.utn.frba.dds.dominio.criterios.CriterioBase;
import ar.edu.utn.frba.dds.dominio.fuentes.*;
import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioHechos;
import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioSolicitudes;
import ar.edu.utn.frba.dds.dominio.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudDeCarga;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TestFuenteDinamica implements SimplePersistenceTest {

  Hecho hechoModificador;
  CriterioBase cBase;
  List<Criterio> criterios;
  RepositorioHechos repoHechos;
  RepositorioSolicitudes repoSolicitudes;
  FuenteDinamica fuenteDinamica;
  Agregador agregador;
  SolicitudDeCarga solicitudDeCargaPrimera;
  SolicitudDeCarga solicitudDeCargaSegunda;
  SolicitudDeCarga solicitudDeCargaPrimeraSinRegistro;


  @BeforeEach
  public void prepImportacionDinamica() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();

    hechoModificador = new Hecho(
        "Corte de luz modificado",
        "Corte de luz en zona oeste",
        "cortes", 22.6, 29.3,
        LocalDate.of(2025, 1, 18),
        LocalDate.now(),
        TipoFuente.DINAMICA,
        "http://multimediavalue",
        Boolean.TRUE
    );

    cBase = new CriterioBase();
    criterios = List.of(cBase); // lista inmutable
    repoHechos = new RepositorioHechos();
    repoSolicitudes = new RepositorioSolicitudes();
    fuenteDinamica = new FuenteDinamica();
    agregador = new Agregador(List.of(fuenteDinamica));

    solicitudDeCargaPrimera = new SolicitudDeCarga(
        "Corte de luz", "Corte de luz en zona sur",
        "cortes", 21.2, 12.8,
        LocalDate.of(2025, 1, 1),
        "", true
    );

    solicitudDeCargaPrimeraSinRegistro = new SolicitudDeCarga(
        "Corte de luz", "Corte de luz en zona sur",
        "cortes", 21.2, 12.8,
        LocalDate.of(2025, 1, 1),
        "", false
    );

    solicitudDeCargaSegunda = new SolicitudDeCarga(
        "Corte de agua", "Corte de agua en zona oeste",
        "cortes", 25.6, 9.3,
        LocalDate.of(2025, 1, 20),
        "", true
    );
  }

  @Test
  public void importarHechos() {
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);

    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    Hecho hechoAprobado = solicitudes.get(0).aprobar();

    // persistir en repo
    repoHechos.cargarHecho(hechoAprobado);
    fuenteDinamica.actualiza(repoHechos);

    List<Hecho> hechos = fuenteDinamica.getHechos();

    Assertions.assertEquals(EstadoSolicitud.ACEPTADA, solicitudDeCargaPrimera.getEstado());
    Assertions.assertEquals("Corte de luz", hechos.get(0).getTitulo());
    Assertions.assertEquals(1, hechos.size());
  }

  @Test
  public void importarHechosSoloAceptoUno() {
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaSegunda);

    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    Hecho hechoAprobado = solicitudes.get(1).aprobar();

    repoHechos.cargarHecho(hechoAprobado);
    fuenteDinamica.actualiza(repoHechos);

    List<Hecho> hechos = fuenteDinamica.getHechos();

    Assertions.assertEquals("Corte de agua", hechos.get(0).getTitulo());
    Assertions.assertEquals(1, hechos.size());
  }

  @Test
  public void importarHechosRegistradoYRechazar() {
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);

    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    solicitudes.get(0).rechazar();
    solicitudes.get(0).sugerir("Cambia el titulo");

    fuenteDinamica.actualiza(repoHechos);

    Assertions.assertEquals("Cambia el titulo", solicitudDeCargaPrimera.getSugerencia());
    Assertions.assertEquals(EstadoSolicitud.RECHAZADA, solicitudDeCargaPrimera.getEstado());
    Assertions.assertTrue(fuenteDinamica.getHechos().isEmpty());
  }

  @Test
  public void importarHechosRegistradoYAceptar() {
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);

    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    Hecho hechoAprobado = solicitudes.get(0).aprobar();

    repoHechos.cargarHecho(hechoAprobado);
    fuenteDinamica.actualiza(repoHechos);

    List<Hecho> hechos = fuenteDinamica.getHechos();

    Assertions.assertEquals("Corte de luz", hechos.get(0).getTitulo());
    Assertions.assertEquals(1, hechos.size());
  }

  @Test
  public void importarHechosRegistradoYModificar() {
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);

    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    Hecho hechoAprobado = solicitudes.get(0).aprobar();
    repoHechos.cargarHecho(hechoAprobado);

    // asigno mismo id al modificador
    hechoModificador.setId(hechoAprobado.getId());
    solicitudes.get(0).modificarHecho(hechoModificador);

    repoHechos.modificarHecho(hechoModificador);
    fuenteDinamica.actualiza(repoHechos);

    List<Hecho> hechosBusco = fuenteDinamica.getHechos();

    Assertions.assertEquals("Corte de luz modificado", hechosBusco.get(0).getTitulo());
  }

  @Test
  public void importarHechosRegistradoYModificarFailNoRegistrado() {
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimeraSinRegistro);

    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    Hecho hechoAprobado = solicitudes.get(0).aprobar();
    repoHechos.cargarHecho(hechoAprobado);

    hechoModificador.setId(hechoAprobado.getId());

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      solicitudes.get(0).modificarHecho(hechoModificador);
    });

    Assertions.assertEquals("No se puede modificar este hecho", exception.getMessage());
  }

  @Test
  public void importarHechosRegistradoYModificarFailSolicitudNoAceptada() {
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      solicitudDeCargaPrimera.modificarHecho(hechoModificador);
    });

    Assertions.assertEquals("No se puede modificar este hecho", exception.getMessage());
  }


  /*
  TODO REVISAR Y CORREGIR ESTA TEST
  @Test
  public void importarHechosRegistradoYModificarFailFechaCargaMayorA7() {
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);

    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    Hecho hechoAprobado = solicitudes.get(0).aprobar();
    repoHechos.cargarHecho(hechoAprobado);

    solicitudDeCargaPrimera.setFechaCargaOriginal(LocalDate.of(2025, 2, 18));
    hechoModificador.setId(hechoAprobado.getId());

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      solicitudDeCargaPrimera.modificarHecho(hechoModificador);
    });

    Assertions.assertEquals("No se puede modificar este hecho", exception.getMessage());
  }

   */


}

