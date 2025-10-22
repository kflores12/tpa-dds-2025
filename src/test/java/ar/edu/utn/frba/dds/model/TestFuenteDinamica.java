package ar.edu.utn.frba.dds.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.model.entities.GeneradorHandleUuid;
import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.model.entities.criterios.Criterio;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioBase;
import ar.edu.utn.frba.dds.model.entities.fuentes.*;
import ar.edu.utn.frba.dds.repositories.RepositorioHechos;
import ar.edu.utn.frba.dds.repositories.RepositorioSolicitudesDeCarga;
import ar.edu.utn.frba.dds.model.entities.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.model.entities.solicitudes.SolicitudDeCarga;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


public class TestFuenteDinamica implements SimplePersistenceTest {

  Hecho hechoModificador;
  CriterioBase cBase;
  List<Criterio> criterios;
  RepositorioHechos repoHechos;
  RepositorioSolicitudesDeCarga repoSolicitudes;
  FuenteDinamica fuenteDinamica;
  Agregador agregador;
  SolicitudDeCarga solicitudDeCargaPrimera;
  SolicitudDeCarga solicitudDeCargaSegunda;
  SolicitudDeCarga solicitudDeCargaPrimeraSinRegistro;


  @BeforeEach
  public void prepImportacionDinamica() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();

    cBase = new CriterioBase();
    criterios = List.of(cBase);
    repoHechos = new RepositorioHechos();
    repoSolicitudes = new RepositorioSolicitudesDeCarga();
    fuenteDinamica = new FuenteDinamica();
    agregador = new Agregador(List.of(fuenteDinamica));


    hechoModificador = new Hecho(
        "Corte de luz modificado",
        "Corte de luz en zona oeste",
        "cortes", 22.6, 29.3,
        LocalDateTime.of(2025, 1, 18,00,00),
        LocalDateTime.now(),
        TipoFuente.DINAMICA,
        "http://multimediavalue",
        Boolean.TRUE
    );


    solicitudDeCargaPrimera = new SolicitudDeCarga(
        "Corte de luz", "Corte de luz en zona sur",
        "cortes", 21.2, 12.8,
        LocalDateTime.of(2025, 1, 1,12,00),
        "", true
    );

    solicitudDeCargaPrimeraSinRegistro = new SolicitudDeCarga(
        "Corte de luz", "Corte de luz en zona sur",
        "cortes", 21.2, 12.8,
        LocalDateTime.of(2025, 1, 1,12,00),
        "", false
    );

    solicitudDeCargaSegunda = new SolicitudDeCarga(
        "Corte de agua", "Corte de agua en zona oeste",
        "cortes", 25.6, 9.3,
        LocalDateTime.of(2025, 1, 20,12,00),
        "", true
    );

  }

  @Test
  public void importarHechos() {
    repoSolicitudes.registrar(solicitudDeCargaPrimera);
    solicitudDeCargaPrimera.aprobar();

    fuenteDinamica.actualiza(repoHechos);

    repoSolicitudes.registrar(solicitudDeCargaPrimera);

    List<Hecho> hechos = fuenteDinamica.getHechos();

    Assertions.assertEquals(EstadoSolicitud.ACEPTADA, solicitudDeCargaPrimera.getEstado());
    Assertions.assertEquals("Corte de luz", hechos.get(0).getTitulo());
    Assertions.assertEquals(1, hechos.size());

  }

  @Test
  public void importarHechosSoloAceptoUno() {
    repoSolicitudes.registrar(solicitudDeCargaPrimera);
    repoSolicitudes.registrar(solicitudDeCargaSegunda);

    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();

    solicitudes.get(1).aprobar();
    fuenteDinamica.actualiza(repoHechos);

    List<Hecho> hechos = fuenteDinamica.getHechos();

    System.out.printf("%s %n", hechos.get(0).getTitulo());

    Assertions.assertEquals("Corte de agua", hechos.get(0).getTitulo());
    Assertions.assertEquals(1, hechos.size());
  }

  @Test
  public void importarHechosRegistradoYRechazar() {
    repoSolicitudes.registrar(solicitudDeCargaPrimera);

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
    repoSolicitudes.registrar(solicitudDeCargaPrimera);

    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();

    solicitudes.get(0).aprobar();

    fuenteDinamica.actualiza(repoHechos);

    List<Hecho> hechos = fuenteDinamica.getHechos();

    Assertions.assertEquals("Corte de luz", hechos.get(0).getTitulo());
    Assertions.assertEquals(1, hechos.size());
  }

  @Test
  public void importarHechosRegistradoYModificar() {
    repoSolicitudes.registrar(solicitudDeCargaPrimera);

    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();

    solicitudes.get(0).aprobar();
    solicitudes.get(0).modificarHecho(hechoModificador);

    repoHechos.modificarHecho(hechoModificador);
    fuenteDinamica.actualiza(repoHechos);

    List<Hecho> hechosBusco = fuenteDinamica.getHechos();

    Assertions.assertEquals("Corte de luz modificado", hechosBusco.get(0).getTitulo());
  }

  @Test
  public void importarHechosRegistradoYModificarFailNoRegistrado() {
    repoSolicitudes.registrar(solicitudDeCargaPrimeraSinRegistro);

    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    solicitudes.get(0).aprobar();

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      solicitudes.get(0).modificarHecho(hechoModificador);
    });

    Assertions.assertEquals("No se puede modificar este hecho", exception.getMessage());
  }


  @Test
  public void importarHechosRegistradoYModificarFailSolicitudNoAceptada() {
    repoSolicitudes.registrar(solicitudDeCargaPrimera);

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

