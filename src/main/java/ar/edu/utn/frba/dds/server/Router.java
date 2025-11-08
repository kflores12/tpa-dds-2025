package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.ColeccionController;
import ar.edu.utn.frba.dds.controllers.EstadisticasController;
import ar.edu.utn.frba.dds.controllers.GestionSolicitudesController;
import ar.edu.utn.frba.dds.controllers.HechoController;
import ar.edu.utn.frba.dds.controllers.HomeController;
import ar.edu.utn.frba.dds.controllers.LoginController;
import ar.edu.utn.frba.dds.controllers.RegistroController;
import ar.edu.utn.frba.dds.controllers.SolicitudController;
import io.javalin.Javalin;

public class Router {
  public void configure(Javalin app) {
    HomeController homeController = new HomeController();
    HechoController hechoController = new HechoController();
    SolicitudController solicitudController = new SolicitudController();
    LoginController loginController = new LoginController();
    RegistroController registroController = new RegistroController();
    GestionSolicitudesController gestionSolicitudesController = new GestionSolicitudesController();
    ColeccionController coleccionController = new ColeccionController();

    // HOME
    app.get("/", ctx -> ctx.redirect("/home"));
    app.get("/home",
        ctx -> ctx.render("home.hbs", homeController.index(ctx)));

    // LOGIN / REGISTRO
    app.get("/login", loginController::renderLogin);
    app.post("/login", loginController::handleLogin);
    app.get("/logout", loginController::handleLogout);
    //BUSQUEDA DE HECHOS
    app.get("/hechos/buscar", ctx -> ctx.render("busqueda-hechos.hbs",
        hechoController.showBusquedaForm(ctx)));


    app.get("/register", registroController::renderRegister);
    app.post("/register", registroController::handleRegister);

    // HECHOS
    app.get("/hechos/nuevo",
        ctx -> ctx.render(
            "creacion.hbs",
            hechoController.showCreationForm(ctx)));
    app.post("/hechos", hechoController::create);
    app.get("/hechos/confirmacion/{solicitudId}", hechoController::showConfirmation);

    // SOLICITUDES DE ELIMINACIÓN
    app.get("/hechos/{id}/solicitud",
        ctx -> ctx.render(
            "eliminacion.hbs",
            solicitudController.showSolicitudForm(ctx)));
    app.post("/solicitudes", solicitudController::createSolicitudEliminacion);
    app.get("/solicitudes/resultado/{solicitudId}",
        ctx -> ctx.render(
            "resultado_eliminacion.hbs",
            solicitudController.showResultado(ctx)));

    // DASHBOARD
    app.get("/dashboard", ctx -> ctx.render("/dashboard/dashboard.hbs"));
    app.get("/dashboard/solicitudes",
        gestionSolicitudesController::mostrarSolicitudes);
    app.post("solicitud/carga/{id}/aceptar",
        gestionSolicitudesController::aceptarSolicitudCarga);
    app.post("solicitud/carga/{id}/rechazar",
        gestionSolicitudesController::rechazarSolicitudCarga);
    app.post("solicitud/eliminacion/{id}/aceptar",
        gestionSolicitudesController::aceptarSolicitudEliminacion);
    app.post("solicitud/eliminacion/{id}/rechazar",
        gestionSolicitudesController::rechazarSolicitudEliminacion);
    app.get("/dashboard/colecciones/crear",
        coleccionController::mostrarFormularioCreacion);
    app.post("/dashboard/colecciones/crear",
        coleccionController::crearColeccion);
    app.get("/dashboard/colecciones/modificar",
        coleccionController::mostrarColecciones);
    app.post("/dashboard/colecciones/modificar/{id}",
        coleccionController::editarColeccion);
    app.get("/dashboard/colecciones/modificar/{id}",
        coleccionController::mostrarFormularioEdicion);



    //Estadisticas
    app.get("/dashboard/estadisticas/cantidadSpam", ctx -> ctx.render(
        "dashboard/estadisticaSpam.hbs",
        EstadisticasController.mostrarSpam(ctx)));
    app.get("/dashboard/estadisticas/horaPicoCategoria", ctx -> ctx.render(
        "dashboard/estadisticaHoraPico.hbs",
        EstadisticasController.mostrarHoraPico(ctx)));
    app.get("/dashboard/estadisticas/categoriaMaxima", ctx -> ctx.render(
        "dashboard/estadisticaCategoriaMaxima.hbs",
        EstadisticasController.mostrarCategoriaMaxima(ctx)));
    app.get("/dashboard/estadisticas/categoriaProvinciaMax", ctx -> ctx.render(
        "dashboard/estadisticaCategoriaProvinciaMax.hbs",
        EstadisticasController.mostrarCategoriaProvinciaMaxHechos(ctx)));
    app.get("/dashboard/estadisticas/coleccionProvinciaMax", ctx -> ctx.render(
        "dashboard/estadisticaColeccionProvinciaMax.hbs",
        EstadisticasController.mostrarColeccionProvinciaMaxHechos(ctx)));

    //Descargas
    app.get("/descargar/estadisticas_cantidad_spam.csv", ctx -> {
      ctx.result(java.nio.file.Files.newInputStream(java.nio.file.Paths.get(
          "./descargar/estadisticas_cantidad_spam.csv"))); });
    app.get("/descargar/estadisticas_categoria_horaspico.csv", ctx -> {
      ctx.result(java.nio.file.Files.newInputStream(java.nio.file.Paths.get(
          "./descargar/estadisticas_categoria_horaspico.csv"))); });
    app.get("/descargar/estadisticas_categoria_maxima.csv", ctx -> {
      ctx.result(java.nio.file.Files.newInputStream(java.nio.file.Paths.get(
          "./descargar/estadisticas_categoria_maxima.csv"))); });
    app.get("/descargar/estadisticas_categoria_hechosmaximos.csv", ctx -> {
      ctx.result(java.nio.file.Files.newInputStream(java.nio.file.Paths.get(
          "./descargar/estadisticas_categoria_hechosmaximos.csv"))); });
    app.get("/descargar/estadisticas_coleccion_hechosmaximos.csv", ctx -> {
      ctx.result(java.nio.file.Files.newInputStream(java.nio.file.Paths.get(
          "./descargar/estadisticas_coleccion_hechosmaximos.csv"))); });
  }
}

