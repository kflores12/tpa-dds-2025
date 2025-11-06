package ar.edu.utn.frba.dds.server;


import ar.edu.utn.frba.dds.controllers.HechoController;
import ar.edu.utn.frba.dds.controllers.HomeController;
import ar.edu.utn.frba.dds.controllers.SolicitudController;
import ar.edu.utn.frba.dds.controllers.*;
import io.javalin.Javalin;

public class Router {
  public void configure(Javalin app) {
    HomeController homeController = new HomeController();
    HechoController hechoController = new HechoController();
    SolicitudController solicitudController = new SolicitudController();
    LoginController loginController = new LoginController();
    RegistroController registroController = new RegistroController();

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
        ctx -> ctx.render("creacion.hbs", hechoController.showCreationForm(ctx)));
    app.post("/hechos", hechoController::create);
    app.get("/hechos/confirmacion/{solicitudId}", hechoController::showConfirmation);

    // SOLICITUDES DE ELIMINACIÓN
    app.get("/hechos/{id}/solicitud",
        ctx -> ctx.render("eliminacion.hbs", solicitudController.showSolicitudForm(ctx)));
    app.post("/solicitudes", solicitudController::createSolicitudEliminacion);
    app.get("/solicitudes/resultado/{solicitudId}",
        ctx -> ctx.render("resultado_eliminacion.hbs", solicitudController.showResultado(ctx)));
    app.get("/dashboard",ctx -> ctx.render("dashboard.hbs"));
  }
}

