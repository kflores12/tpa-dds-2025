package ar.edu.utn.frba.dds.dominio;

import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.dominio.criterios.Criterio;
import ar.edu.utn.frba.dds.dominio.criterios.CriterioBase;
import ar.edu.utn.frba.dds.dominio.fuentes.*;
import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioFuentes;
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
  Fuente fuenteDinamica;
  RepositorioHechos repoHechos;
  RepositorioSolicitudes repoSolicitudes;
  List<Criterio> criterios;
  Criterio cBase;
  SolicitudDeCarga solicitudDeCargaPrimera;
  SolicitudDeCarga solicitudDeCargaPrimeraSinRegistro;
  SolicitudDeCarga solicitudDeCargaSegunda;
  List<Hecho> hechosParaCargar;
  Hecho hechoPrimero;
  Hecho hechoSegundo;
  Hecho hechoModificador;
  Hecho hechoCargaVieja;
  private RepositorioFuentes fuentesRepo;
  private Agregador agregador;
  List<Fuente> fuentesAgregador;

  @BeforeEach
  public void prepImportacionDinamica() {
    hechoModificador = new Hecho("Corte de luz modificado","Corte de luz en zona oeste","cortes",22.6,29.3, LocalDate.of(2025,1,18),LocalDate.now(), TipoFuente.DINAMICA,"http://multimediavalue",Boolean.TRUE);
    cBase = new CriterioBase();
    criterios = new ArrayList<>(Arrays.asList(cBase));
    repoHechos = new RepositorioHechos();
    repoSolicitudes = new RepositorioSolicitudes();
    fuenteDinamica = new FuenteDinamica(repoHechos);
    fuentesRepo = new RepositorioFuentes();
    fuentesAgregador = new ArrayList<>();
    fuentesAgregador.add(fuenteDinamica);
    agregador = new Agregador(fuentesAgregador);

    solicitudDeCargaPrimera = new SolicitudDeCarga("Corte de luz","Corte de luz en zona sur"
        ,"cortes",21.2,12.8, LocalDate.of(2025,1,1),"",Boolean.TRUE,repoHechos);

    solicitudDeCargaPrimeraSinRegistro = new SolicitudDeCarga("Corte de luz","Corte de luz en zona sur"
        ,"cortes",21.2,12.8, LocalDate.of(2025,1,1),"",Boolean.FALSE,repoHechos);

    solicitudDeCargaSegunda= new SolicitudDeCarga("Corte de agua","Corte de agua en zona oeste","cortes",25.6,9.3,  LocalDate.of(2025,1,20),"",Boolean.TRUE,repoHechos);

  }

  @Test
  public void importarHechos() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();

    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);
    //Tomar solicitud.
    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    //Admin toma y aprueba solicitudes.
    solicitudes.get(0).aprobar();
    //Cargo la Solicitud.
    Coleccion coleccion = new Coleccion("cortes",
        "cortes en Argentina", fuenteDinamica,
        criterios,generador.generar(), null);

    List<Hecho> hechos = coleccion.obtnerHechos();

    //Reviso que los hechos esten bien cargados (Con sus titulos).

    Assertions.assertEquals("Corte de luz",hechos.get(0).getTitulo());

    Assertions.assertEquals(1, hechos.size());
  }

  @Test
  public void importarHechosSoloAceptoUno() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaSegunda);
    //Tomar solicitud.
    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    //Admin toma y aprueba solicitudes.
    solicitudes.get(1).aprobar();
    Coleccion coleccion = new Coleccion("cortes",
        "cortes en Argentina", fuenteDinamica,
        criterios,generador.generar(),null);
    List<Hecho> hechos = coleccion.obtnerHechos();

    Assertions.assertEquals(hechos.get(0).getTitulo(),"Corte de agua");
    Assertions.assertEquals(1, hechos.size());
  }

  @Test
  public void importarHechosRegistradoYRechazar() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);
    //Tomar solicitud.
    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    //Admin toma y aprueba solicitudes.
    solicitudes.get(0).rechazar();
    solicitudes.get(0).sugerir("Cambia el titulo");
    Coleccion coleccion = new Coleccion("cortes",
        "cortes en Argentina", fuenteDinamica,
        criterios, generador.generar(),null);
    List<Hecho> hechos = coleccion.obtnerHechos();

    Assertions.assertEquals("Cambia el titulo", solicitudDeCargaPrimera.getSugerencia());
    Assertions.assertEquals(EstadoSolicitud.RECHAZADA, solicitudDeCargaPrimera.getEstado());
    Assertions.assertEquals(0, hechos.size());
  }

  @Test
  public void importarHechosRegistradoYAceptar() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);
    //Tomar solicitud.
    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    //Admin toma y aprueba solicitudes.
    solicitudes.get(0).aprobar();
    Coleccion coleccion = new Coleccion("cortes",
        "cortes en Argentina", fuenteDinamica,
        criterios,generador.generar(),null);
    List<Hecho> hechos = coleccion.obtnerHechos();

    Assertions.assertEquals(hechos.get(0).getTitulo(),"Corte de luz");
    Assertions.assertEquals(1, hechos.size());
  }

  @Test
  public void importarHechosRegistradoYModificar() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);
    //Tomar solicitud.
    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    //Admin toma y aprueba solicitudes.
    solicitudes.get(0).aprobar();
    Coleccion coleccion = new Coleccion("cortes",
        "cortes en Argentina", fuenteDinamica,
        criterios,generador.generar(),null);
    List<Hecho> hechos = coleccion.obtnerHechos();

    Assertions.assertEquals(hechos.get(0).getTitulo(),"Corte de luz");
    solicitudes.get(0).modificarHecho(hechoModificador);
    List<Hecho> hechosBusco = coleccion.obtnerHechos();

    Assertions.assertEquals(hechosBusco.get(0).getTitulo(),"Corte de luz modificado");
  }

  @Test
  public void importarHechosRegistradoYModificarFailNoRegistrado() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimeraSinRegistro);
    //Tomar solicitud.
    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    //Admin toma y aprueba solicitudes.
    solicitudes.get(0).aprobar();
    Coleccion coleccion = new Coleccion("cortes",
        "cortes en Argentina", fuenteDinamica,
        criterios,generador.generar(),null);
    List<Hecho> hechos = coleccion.obtnerHechos();

    Assertions.assertEquals(hechos.get(0).getTitulo(),"Corte de luz");

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      solicitudes.get(0).modificarHecho(hechoModificador);
    });
    Assertions.assertEquals("No se puede modificar este hecho", exception.getMessage());
  }


  @Test
  public void importarHechosRegistradoYModificarFailSolicitudNoAceptada() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      solicitudDeCargaPrimera.modificarHecho(hechoModificador);
    });
    Assertions.assertEquals("No se puede modificar este hecho", exception.getMessage());
  }

  @Test
  public void importarHechosRegistradoYModificarFailFechaCargaMayorA7() {
    GeneradorHandleUuid generador = new GeneradorHandleUuid();
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);

    //Tomar solicitud.
    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    //Admin toma y aprueba solicitudes.
    solicitudes.get(0).aprobar();
    Coleccion coleccion = new Coleccion("cortes",
        "cortes en Argentina", fuenteDinamica,
        criterios,generador.generar(),null);
    List<Hecho> hechos = coleccion.obtnerHechos();

    //Simulo la fecha de carga del hecho en la solicitud
    solicitudDeCargaPrimera.setFechaCargaOriginal(LocalDate.of(2025,2,18));

    Assertions.assertEquals("Corte de luz",hechos.get(0).getTitulo());

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
    //repoHechos.limpiarBaseDeHechos();
    repoSolicitudes.limpiarListas();
  }
}

