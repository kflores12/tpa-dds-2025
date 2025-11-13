package ar.edu.utn.frba.dds.scripts;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioCategoria;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioDescripcion;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioFecha;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioFechaCarga;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioRangoFechas;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioTitulo;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioUbicacion;
import ar.edu.utn.frba.dds.model.entities.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.model.entities.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.model.entities.solicitudes.SolicitudDeCarga;
import ar.edu.utn.frba.dds.model.estadistica.ComponenteEstadistico;
import ar.edu.utn.frba.dds.model.estadistica.Estadistica;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaCantidadSpam;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaCategoriaMaxima;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaHoraHechosCategoria;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaProvMaxHechosCategoria;
import ar.edu.utn.frba.dds.model.estadistica.EstadisticaProvMaxHechosColeccion;
import ar.edu.utn.frba.dds.repositories.RepositorioCriterios;
import ar.edu.utn.frba.dds.repositories.RepositorioFuentes;
import ar.edu.utn.frba.dds.repositories.RepositorioHechos;
import ar.edu.utn.frba.dds.repositories.RepositorioSolicitudesDeCarga;
import ar.edu.utn.frba.dds.server.AppRole;
import ar.edu.utn.frba.dds.service.ServicioAutenticacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bootstrap implements WithSimplePersistenceUnit {
  public static void init() {
    FuenteDinamica.setRepositorioHechos(RepositorioHechos.getInstance());
    new Bootstrap().cargarDatosIniciales();
  }

  private void cargarDatosIniciales() {

    withTransaction(() -> {
      ServicioAutenticacion servicioAutenticacion = new ServicioAutenticacion();
      RepositorioHechos repositorioHechos = new RepositorioHechos();
      RepositorioFuentes repositorioFuentes = new RepositorioFuentes();

      List<FuenteDinamica> fuentesDinamicas = entityManager()
          .createQuery("FROM FuenteDinamica", FuenteDinamica.class)
          .getResultList();

      FuenteDinamica fuenteAsociada;

      if (fuentesDinamicas.isEmpty()) {
        // Si NO existe, la creamos y persistimos.
        fuenteAsociada = new FuenteDinamica();
        repositorioFuentes.registrarFuente(fuenteAsociada);
      } else {
        fuenteAsociada = fuentesDinamicas.get(0);
      }

      // --- 3. CREAR HECHOS DE PRUEBA ASOCIADOS ---
      Long conteoHechos = entityManager()
          .createQuery("SELECT COUNT(h) FROM Hecho h", Long.class)
          .getSingleResult();

      Hecho hecho1 = new Hecho(
          "Incendio en Barrio Sur - Edificio A",
          "Se reporta foco de incendio en un edificio abandonado, cerca de la plaza.",
          "Incendio",
          -34.6037,
          -58.3816,
          LocalDateTime.now().minusDays(2),
          LocalDateTime.now(),
          TipoFuente.DINAMICA,
          "url_imagen_incendio.jpg",
          Boolean.TRUE
      );

      Hecho hecho2 = new Hecho(
          "Accidente Vial - Av. 9 de Julio",
          "Colisión múltiple en el centro, visibilidad baja por neblina.",
          "Accidente Vial",
          -34.6090,
          -58.3820,
          LocalDateTime.now().minusHours(5),
          LocalDateTime.now(),
          TipoFuente.DINAMICA,
          "url_video_accidente.mp4",
          Boolean.TRUE
      );

      Hecho hecho3 = new Hecho(
          "Mancha tóxica en el río",
          "Se observa vertido ilegal de residuos industriales en el Riachuelo.",
          "Contaminación",
          -34.6400,
          -58.3500,
          LocalDateTime.now().minusDays(1),
          LocalDateTime.now(),
          TipoFuente.DINAMICA,
          null,
          Boolean.TRUE
      );
      if (conteoHechos == 0) {
        repositorioHechos.cargarHecho(hecho1);
        repositorioHechos.cargarHecho(hecho2);
        repositorioHechos.cargarHecho(hecho3);
      }
      SolicitudDeCarga solicitudDeCarga =
          new SolicitudDeCarga("abc", "abc", "abc",
              -34.6400,
              -58.3500, LocalDateTime.now(),
              LocalDateTime.now(),
              null, false);

      servicioAutenticacion.registerUser(
          "admin", "admin123", AppRole.ADMIN
      );
      RepositorioSolicitudesDeCarga repoCarga = new RepositorioSolicitudesDeCarga();
      repoCarga.registrar(solicitudDeCarga);

      RepositorioCriterios repositorioCriterios = new RepositorioCriterios();
      CriterioCategoria criterioCategoria = new CriterioCategoria();
      CriterioDescripcion criterioDescripcion = new CriterioDescripcion();
      CriterioFecha criterioFecha = new CriterioFecha();
      criterioFecha.setFechaAcontecimiento(LocalDate.now());
      repositorioCriterios.cargarCriterio(criterioCategoria);
      repositorioCriterios.cargarCriterio(criterioDescripcion);
      repositorioCriterios.cargarCriterio(criterioFecha);
      CriterioFechaCarga criterioFechaCarga = new CriterioFechaCarga();
      criterioFechaCarga.setFechaCarga(LocalDate.now());
      CriterioRangoFechas criterioRangoFechas = new CriterioRangoFechas();
      criterioRangoFechas.setDesde(LocalDate.now());
      criterioRangoFechas.setHasta(LocalDate.now());
      CriterioUbicacion criterioUbicacion = new CriterioUbicacion();
      CriterioTitulo criterioTitulo = new CriterioTitulo();
      repositorioCriterios.cargarCriterio(criterioRangoFechas);
      repositorioCriterios.cargarCriterio(criterioUbicacion);
      repositorioCriterios.cargarCriterio(criterioTitulo);
      repositorioCriterios.cargarCriterio(criterioFechaCarga);
      EstadisticaCategoriaMaxima estadisticaCm = new EstadisticaCategoriaMaxima();
      EstadisticaCantidadSpam estadisticaCs = new EstadisticaCantidadSpam();
      EstadisticaProvMaxHechosCategoria estadisticaPmhcat = new EstadisticaProvMaxHechosCategoria();
      EstadisticaProvMaxHechosColeccion estadisticaPmhcol = new EstadisticaProvMaxHechosColeccion();
      EstadisticaHoraHechosCategoria estadisticaHoraPico = new EstadisticaHoraHechosCategoria();

      // Las registrás todas
      List<Estadistica> estadisticas = new ArrayList<>();
      estadisticas.add(estadisticaCm);
      estadisticas.add(estadisticaCs);
      estadisticas.add(estadisticaPmhcat);
      estadisticas.add(estadisticaPmhcol);
      estadisticas.add(estadisticaHoraPico);

      // Inicializás el componente global
      ComponenteEstadistico.inicializar(estadisticas);

      System.out.println("--- Seeder: 3 Hechos de prueba creados para eliminación.");
    });
  }

}
