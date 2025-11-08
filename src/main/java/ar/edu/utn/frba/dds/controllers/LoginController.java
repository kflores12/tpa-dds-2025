package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.entities.User;
import ar.edu.utn.frba.dds.server.GestorTokens;
import ar.edu.utn.frba.dds.service.ServicioAutenticacion;
import io.javalin.http.Context;
import io.javalin.http.Cookie;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LoginController {

  private final ServicioAutenticacion servicioAutenticacion;
  private final GestorTokens gestorTokens;

  public LoginController() {
    this.servicioAutenticacion = new ServicioAutenticacion();
    this.gestorTokens = new GestorTokens();
  }

  public void renderLogin(Context ctx) {
    ctx.render("login.hbs");
  }

  public void handleLogin(Context ctx) {
    String username = ctx.formParam("username");
    String password = ctx.formParam("password");

    Optional<User> userOpt = servicioAutenticacion.validateLogin(username, password);

    if (userOpt.isPresent()) {
      User actualUser = userOpt.get();
      String token = gestorTokens.createToken(actualUser);

      Cookie jwtCookie = new Cookie(
          "jwt_token",
          token,
          "/",
          -1,
          false,
          -1,
          true
      );

      ctx.cookie(jwtCookie);
      ctx.redirect("/home");
    } else {
      Map<String, Object> model = new HashMap<>();
      model.put("error", "Usuario o contraseña incorrectos");
      ctx.status(401).render("login.hbs", model);
    }
  }

  public void handleLogout(Context ctx) {
    ctx.removeCookie("jwt_token", "/");
    ctx.redirect("/login");
  }
}