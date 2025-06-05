package ar.edu.utn.frba.dds.dominio;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;

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
  Hecho hechoPrimero = new Hecho("Corte de luz","Corte de luz en zona sur","cortes",21.2,12.8, LocalDate.of(2025,1,1),LocalDate.now(),TipoFuente.DINAMICA,"",Boolean.TRUE);
  Hecho hechoSegundo = new Hecho("Corte de agua","Corte de agua en zona oeste","cortes",25.6,9.3, LocalDate.of(2025,1,20),LocalDate.now(),TipoFuente.DINAMICA,"",Boolean.TRUE);
  Hecho hechoModificador = new Hecho("Corte de luz modificado","Corte de agua en zona oeste","cortes",22.6,29.3, LocalDate.of(2025,1,18),LocalDate.now(),TipoFuente.DINAMICA,"http://multimediavalue",Boolean.TRUE);

  @BeforeEach
  public void prepImportacionDinamica() {
    cBase = new CriterioBase();
    hechosParaCargar = new ArrayList<>(Arrays.asList(hechoPrimero, hechoSegundo));
    criterios = new ArrayList<>(Arrays.asList(cBase));
    fuenteDinamica = new FuenteDinamica(repoHechos);
    repoHechos = new RepositorioHechos();
    repoSolicitudes = new RepositorioSolicitudes();
  }

  @Test
  public void importarHechosSinRegistrarme() {
    GeneradorHandleUUID generador = new GeneradorHandleUUID();
    solicitudDeCargaPrimera = new SolicitudDeCarga(hechoPrimero,Boolean.TRUE,repoHechos,repoSolicitudes);
    solicitudDeCargaSegunda = new SolicitudDeCarga(hechoSegundo,Boolean.FALSE,repoHechos,repoSolicitudes);
    //Admin aprueba solicitudes.
    solicitudDeCargaPrimera.evaluarSolicitud(EstadoSolicitud.ACEPTADA);
    solicitudDeCargaSegunda.evaluarSolicitud(EstadoSolicitud.ACEPTADA);
    //Traigo del repositorio.
    Coleccion coleccion = new Coleccion("Cortes",
        "Cortes en Argentina", fuenteDinamica, criterios,generador.generar());
    List<Hecho> hechos = coleccion.obtenerTodosLosHechos();
    //Reviso que los hechos esten bien cargados (Con sus titulos).
    System.out.printf(" %s \n", hechos.get(0).getTitulo());
    System.out.printf(" %s \n", hechos.get(1).getTitulo());

    Assertions.assertEquals(2, hechos.size());
  }

  @Test
  public void importarHechosRegistrado() {
    GeneradorHandleUUID generador = new GeneradorHandleUUID();
    solicitudDeCargaPrimera = new SolicitudDeCarga(hechoPrimero,Boolean.TRUE,repoHechos,repoSolicitudes);
    solicitudDeCargaSegunda = new SolicitudDeCarga(hechoSegundo,Boolean.FALSE,repoHechos,repoSolicitudes);
    solicitudDeCargaPrimera.evaluarSolicitud(EstadoSolicitud.ACEPTADA);
    solicitudDeCargaSegunda.evaluarSolicitud(EstadoSolicitud.ACEPTADA);

    Coleccion coleccion = new Coleccion("incendios forestales",
        "incendios en la patagonia", fuenteDinamica, criterios,generador.generar());

    List<Hecho> hechos = coleccion.obtenerTodosLosHechos();

    System.out.printf(" %s \n", hechos.get(0).getTitulo());
    System.out.printf(" %s \n", hechos.get(1).getTitulo());

    Assertions.assertEquals(2, hechos.size());
  }

  @Test
  public void importarHechosRegistradoYRechazar() {}

  @Test
  public void importarHechosRegistradoYAceptar() {}

  @Test
  public void importarHechosRegistradoYModificar() {}


}

