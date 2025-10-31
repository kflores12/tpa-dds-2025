package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.model.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.entities.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.model.entities.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.repositories.RepositorioFuentes;
import ar.edu.utn.frba.dds.server.templates.JavalinHandlebars;
import ar.edu.utn.frba.dds.server.templates.JavalinRenderer;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;

import java.time.LocalDateTime;

public class Server implements WithSimplePersistenceUnit {
  public void start() {
    inicializarDatos();
    var app = Javalin.create(config -> {
      initializeStaticFiles(config);
      initializeTemplating(config);
    });

    new Router().configure(app);
    app.start(9001);
  }

  private void inicializarDatos() {
    withTransaction(() -> {
      RepositorioFuentes repoFuentes = RepositorioFuentes.getInstance();

      // --- 1. CREAR/OBTENER LA FUENTE DINÁMICA (ID 1) ---
      Fuente fuenteAsociada = repoFuentes.getFuente(1L); // Intentamos obtener la fuente existente

      if (fuenteAsociada == null) {
        // Si NO existe, la creamos y usamos esta nueva instancia como la 'fuenteAsociada'
        fuenteAsociada = new FuenteDinamica();
        entityManager().persist(fuenteAsociada);
        System.out.println("--- Seeder: Fuente Dinámica inicial creada con ID: " + fuenteAsociada.getId());
      } else {
        System.out.println("--- Seeder: Fuente Dinámica inicial ya existía con ID: " + fuenteAsociada.getId());
      }

      // --- 2. CREAR HECHOS DE PRUEBA ASOCIADOS ---
      Long conteoHechos = entityManager().createQuery("SELECT COUNT(h) FROM Hecho h", Long.class).getSingleResult();

      if (conteoHechos == 0) {

        // Hecho 1: Se usa fuenteAsociada, que garantiza ser una instancia no nula
        Hecho hecho1 = new Hecho(
            "Incendio en Barrio Sur", // 1
            "Se reporta foco de incendio en un edificio abandonado.", // 2
            "Incendio", // 3
            -34.6037, // 4
            -58.3816, // 5
            LocalDateTime.now().minusDays(2), // 6
            LocalDateTime.now(), // 7
            TipoFuente.DINAMICA, // 8 (Enum TipoFuente)
            "", // 9 (Multimedia)
            Boolean.TRUE, // 10 (Disponibilidad)
            fuenteAsociada // 11 (Fuente fuenteOrigen) <-- USAMOS LA INSTANCIA NO NULA
        );

        // Hecho 2: Se usa fuenteAsociada
        Hecho hecho2 = new Hecho(
            "Accidente Vial Grave", // 1
            "Colisión en Av. 9 de Julio, causado por imprudencia.", // 2
            "Accidente Vial", // 3
            -34.6090, // 4
            -58.3820, // 5
            LocalDateTime.now().minusHours(5), // 6
            LocalDateTime.now(), // 7
            TipoFuente.DINAMICA, // 8 (Enum TipoFuente)
            "", // 9 (Multimedia)
            Boolean.TRUE, // 10 (Disponibilidad)
            fuenteAsociada // 11 (Fuente fuenteOrigen)
        );

        // Persistir los hechos
        entityManager().persist(hecho1);
        entityManager().persist(hecho2);

        System.out.println("--- Seeder: 2 Hechos de prueba creados para eliminación.");
      }
    });
  }

  private void initializeTemplating(JavalinConfig config) {
    config.fileRenderer(
        new JavalinRenderer().register("hbs", new JavalinHandlebars())
    );
  }

  private static void initializeStaticFiles(JavalinConfig config) {
    config.staticFiles.add(staticFileConfig -> {
      staticFileConfig.hostedPath = "/assets";
      staticFileConfig.directory = "/assets";
    });
  }
}
