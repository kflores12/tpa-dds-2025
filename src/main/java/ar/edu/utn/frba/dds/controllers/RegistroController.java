package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.service.ServicioAutenticacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.Map;

public class RegistroController implements WithSimplePersistenceUnit {

  private final ServicioAutenticacion servicioAutenticacion;

  public RegistroController() {
    this.servicioAutenticacion = new ServicioAutenticacion();
  }

  public void renderRegister(Context ctx) {
    ctx.render("registro.hbs");
  }

  public void handleRegister(Context ctx) {
    String username = ctx.formParam("username");
    String password = ctx.formParam("password");

    Map<String, Object> model = new HashMap<>();

    try {
      final boolean[] success = {false};

      withTransaction(() -> {
        success[0] = servicioAutenticacion.registerUser(username, password, "USER");
      });

      if (success[0]) {
        ctx.redirect("/login");
      } else {
        model.put("error", "El nombre de usuario ya existe.");
        ctx.status(400).render("registro.hbs", model);
      }

    } catch (Exception e) {
      e.printStackTrace();
      model.put("error", "Ocurrió un error inesperado al registrar.");
      ctx.status(500).render("registro.hbs", model);
    }
  }
}