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
        Boolean.TRUE,
        fuenteDinamica
    );


    solicitudDeCargaPrimera = new SolicitudDeCarga(
        "Corte de luz", "Corte de luz en zona sur",
        "cortes", 21.2, 12.8,
        LocalDateTime.of(2025, 1, 1,12,00),
        "", true, fuenteDinamica
    );

    solicitudDeCargaPrimeraSinRegistro = new SolicitudDeCarga(
        "Corte de luz", "Corte de luz en zona sur",
        "cortes", 21.2, 12.8,
        LocalDateTime.of(2025, 1, 1,12,00),
        "", false, fuenteDinamica
    );

    solicitudDeCargaSegunda = new SolicitudDeCarga(
        "Corte de agua", "Corte de agua en zona oeste",
        "cortes", 25.6, 9.3,
        LocalDateTime.of(2025, 1, 20,12,00),
        "", true, fuenteDinamica
    );

  }

  @Test
  public void importarHechos() {
    repoSolicitudes.registrar(solicitudDeCargaPrimera);
    solicitudDeCargaPrimera.aprobar();


    Hecho hechoModificado = solicitudDeCargaPrimera.getHechoCreado();

    Assertions.assertEquals(EstadoSolicitud.ACEPTADA, solicitudDeCargaPrimera.getEstado());
    Assertions.assertEquals("Corte de luz", hechoModificado.getTitulo());
    Assertions.assertNotNull(hechoModificado);
  }

  @Test
  public void importarHechosSoloAceptoUno() {
    repoSolicitudes.registrar(solicitudDeCargaPrimera);
    repoSolicitudes.registrar(solicitudDeCargaSegunda);
    solicitudDeCargaSegunda.aprobar();

    Hecho hechoModificado = solicitudDeCargaSegunda.getHechoCreado();

    Assertions.assertEquals("Corte de agua", hechoModificado.getTitulo());
  }

  @Test
  public void importarHechosRegistradoYRechazar() {
    repoSolicitudes.registrar(solicitudDeCargaPrimera);

    solicitudDeCargaPrimera.rechazar();
    solicitudDeCargaPrimera.sugerir("Cambia el titulo");


    Assertions.assertEquals("Cambia el titulo", solicitudDeCargaPrimera.getSugerencia());
    Assertions.assertEquals(EstadoSolicitud.RECHAZADA, solicitudDeCargaPrimera.getEstado());
    Assertions.assertTrue(fuenteDinamica.getHechos().isEmpty());
  }


  @Test
  public void importarHechosRegistradoYModificar() {
    repoSolicitudes.registrar(solicitudDeCargaPrimera);

    solicitudDeCargaPrimera.aprobar();
    solicitudDeCargaPrimera.modificarHecho(hechoModificador);

    repoHechos.modificarHecho(hechoModificador);

    Hecho hechoModificado = solicitudDeCargaPrimera.getHechoCreado();

    Assertions.assertEquals("Corte de luz modificado", hechoModificado.getTitulo());
  }

  @Test
  public void importarHechosRegistradoYModificarFailNoRegistrado() {
    repoSolicitudes.registrar(solicitudDeCargaPrimeraSinRegistro);

    solicitudDeCargaPrimeraSinRegistro.aprobar();

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      solicitudDeCargaPrimeraSinRegistro.modificarHecho(hechoModificador);
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



}

