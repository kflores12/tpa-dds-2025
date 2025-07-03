package ar.edu.utn.frba.dds.dominio;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TestFuenteDinamica {
  Fuente fuenteDinamica;
  RepositorioHechos repoHechos;
  RepositorioSolicitudes repoSolicitudes;
  List<Criterio> criterios;
  Criterio cBase;
  SolicitudDeCarga solicitudDeCargaPrimera;
  SolicitudDeCarga solicitudDeCargaSegunda;
  List<Hecho> hechosParaCargar;
  Hecho hechoPrimero;
  Hecho hechoSegundo;
  Hecho hechoModificador;
  Hecho hechoCargaVieja;
  private RepositorioFuentes fuentesRepo;
  private Agregador agregador;

  @BeforeEach
  public void prepImportacionDinamica() {
     hechoPrimero = new Hecho("Corte de luz","Corte de luz en zona sur","cortes",21.2,12.8, LocalDate.of(2025,1,1),LocalDate.now(),TipoFuente.DINAMICA,"",Boolean.TRUE);
     hechoSegundo = new Hecho("Corte de agua","Corte de agua en zona oeste","cortes",25.6,9.3, LocalDate.of(2025,1,20),LocalDate.now(),TipoFuente.DINAMICA,"",Boolean.TRUE);
     hechoModificador = new Hecho("Corte de luz modificado","Corte de luz en zona oeste","cortes",22.6,29.3, LocalDate.of(2025,1,18),LocalDate.now(),TipoFuente.DINAMICA,"http://multimediavalue",Boolean.TRUE);
     hechoCargaVieja = new Hecho("Corte de internet","Corte de internet en zona norte","cortes",22.6,29.3, LocalDate.of(2025,1,18),LocalDate.of(2025,2,18),TipoFuente.DINAMICA,"http://multimediavalue",Boolean.TRUE);
     cBase = new CriterioBase();
     criterios = new ArrayList<>(Arrays.asList(cBase));
     repoHechos = new RepositorioHechos();
     repoSolicitudes = new RepositorioSolicitudes();
     fuenteDinamica = new FuenteDinamica(repoHechos);
    fuentesRepo = new RepositorioFuentes();
    fuentesRepo.registrarFuente(fuenteDinamica);
    FiltroAgregador filtroPorTipo =
        new FiltroPorTipo(List.of(FuenteApi.class));
    agregador = new Agregador(fuentesRepo, filtroPorTipo);
  }

  @Test
  public void importarHechos() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    solicitudDeCargaPrimera = new SolicitudDeCarga(hechoPrimero,Boolean.TRUE,repoHechos);
    solicitudDeCargaSegunda = new SolicitudDeCarga(hechoSegundo,Boolean.FALSE,repoHechos);
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaSegunda);
    //Tomar solicitud.
    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    //Admin toma y aprueba solicitudes.
    solicitudes.get(0).aprobar();
    solicitudes.get(1).aprobar();
    //Cargo la Solicitud.
    Coleccion coleccion = new Coleccion("cortes",
        "cortes en Argentina", fuenteDinamica,
        criterios,generador.generar());
    List<Hecho> hechos = coleccion.getHechos();
    //Reviso que los hechos esten bien cargados (Con sus titulos).

    Assertions.assertEquals(hechos.get(0).getTitulo(),"Corte de agua");
    Assertions.assertEquals(hechos.get(1).getTitulo(),"Corte de luz");
    Assertions.assertEquals(2, hechos.size());
  }

  @Test
  public void importarHechosSoloAceptoUno() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    solicitudDeCargaPrimera = new SolicitudDeCarga(hechoPrimero,Boolean.TRUE,repoHechos);
    solicitudDeCargaSegunda = new SolicitudDeCarga(hechoSegundo,Boolean.FALSE,repoHechos);
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaSegunda);
    //Tomar solicitud.
    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    //Admin toma y aprueba solicitudes.
    solicitudes.get(1).aprobar();
    Coleccion coleccion = new Coleccion("cortes",
        "cortes en Argentina", fuenteDinamica,
        criterios,generador.generar());
    List<Hecho> hechos = coleccion.getHechos();

    Assertions.assertEquals(hechos.get(0).getTitulo(),"Corte de agua");
    Assertions.assertEquals(1, hechos.size());
  }

  @Test
  public void importarHechosRegistradoYRechazar() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    solicitudDeCargaPrimera = new SolicitudDeCarga(hechoPrimero,Boolean.TRUE,repoHechos);
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);
    //Tomar solicitud.
    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    //Admin toma y aprueba solicitudes.
    solicitudes.get(0).rechazar();
    solicitudes.get(0).sugerir("Cambia el titulo");
    Coleccion coleccion = new Coleccion("cortes",
        "cortes en Argentina", fuenteDinamica,
        criterios, generador.generar());
    List<Hecho> hechos = coleccion.getHechos();

    Assertions.assertEquals("Cambia el titulo", solicitudDeCargaPrimera.getSugerencia());
    Assertions.assertEquals(EstadoSolicitud.RECHAZADA, solicitudDeCargaPrimera.getEstado());
    Assertions.assertEquals(0, hechos.size());
  }

  @Test
  public void importarHechosRegistradoYAceptar() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    solicitudDeCargaPrimera = new SolicitudDeCarga(hechoPrimero,Boolean.TRUE,repoHechos);
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);
    //Tomar solicitud.
    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    //Admin toma y aprueba solicitudes.
    solicitudes.get(0).aprobar();
    Coleccion coleccion = new Coleccion("cortes",
        "cortes en Argentina", fuenteDinamica,
        criterios,generador.generar());
    List<Hecho> hechos = coleccion.getHechos();

    Assertions.assertEquals(hechos.get(0).getTitulo(),"Corte de luz");
    Assertions.assertEquals(1, hechos.size());
  }

  @Test
  public void importarHechosRegistradoYModificar() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    solicitudDeCargaPrimera = new SolicitudDeCarga(hechoPrimero,Boolean.TRUE,repoHechos);
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);
    //Tomar solicitud.
    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    //Admin toma y aprueba solicitudes.
    solicitudes.get(0).aprobar();
    Coleccion coleccion = new Coleccion("cortes",
        "cortes en Argentina", fuenteDinamica,
        criterios,generador.generar());
    List<Hecho> hechos = coleccion.getHechos();

    Assertions.assertEquals(hechos.get(0).getTitulo(),"Corte de luz");
    solicitudes.get(0).modificarHecho(hechoModificador);
    Assertions.assertEquals(hechos.get(0).getTitulo(),"Corte de luz modificado");
  }

  @Test
  public void importarHechosRegistradoYModificarFailNoRegistrado() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    solicitudDeCargaPrimera = new SolicitudDeCarga(hechoPrimero,Boolean.FALSE,repoHechos);
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);
    //Tomar solicitud.
    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    //Admin toma y aprueba solicitudes.
    solicitudes.get(0).aprobar();
    Coleccion coleccion = new Coleccion("cortes",
        "cortes en Argentina", fuenteDinamica,
        criterios,generador.generar());
    List<Hecho> hechos = coleccion.getHechos();

    Assertions.assertEquals(hechos.get(0).getTitulo(),"Corte de luz");

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      solicitudes.get(0).modificarHecho(hechoModificador);
    });
    Assertions.assertEquals("No se puede modificar este hecho", exception.getMessage());
  }


  @Test
  public void importarHechosRegistradoYModificarFailSolicitudNoAceptada() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    solicitudDeCargaPrimera = new SolicitudDeCarga(hechoPrimero,Boolean.TRUE,repoHechos);
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      solicitudDeCargaPrimera.modificarHecho(hechoModificador);
    });
    Assertions.assertEquals("No se puede modificar este hecho", exception.getMessage());
  }

  @Test
  public void importarHechosRegistradoYModificarFailFechaCargaMayorA7() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    solicitudDeCargaPrimera = new SolicitudDeCarga(hechoCargaVieja,Boolean.TRUE,repoHechos);
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);

    //Tomar solicitud.
    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    //Admin toma y aprueba solicitudes.
    solicitudes.get(0).aprobar();
    Coleccion coleccion = new Coleccion("cortes",
        "cortes en Argentina", fuenteDinamica,
        criterios,generador.generar());
    List<Hecho> hechos = coleccion.getHechos();

    Assertions.assertEquals("Corte de internet",hechos.get(0).getTitulo());

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      solicitudDeCargaPrimera.modificarHecho(hechoModificador);
    });
    Assertions.assertEquals("No se puede modificar este hecho", exception.getMessage());
  }

  @AfterEach
  void limpiarValores() {
    Hecho hechoPrimero = null;
    Hecho hechoSegundo = null;
    Hecho hechoModificador = null;
    Hecho hechoCargaVieja = null;
    repoHechos.limpiarBaseDeHechos();
    repoSolicitudes.limpiarListas();
  }
}

