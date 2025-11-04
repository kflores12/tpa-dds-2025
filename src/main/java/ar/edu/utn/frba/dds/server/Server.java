package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.scripts.Bootstrap;
import ar.edu.utn.frba.dds.server.templates.JavalinHandlebars;
import ar.edu.utn.frba.dds.server.templates.JavalinRenderer;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import java.util.List;
import java.util.Map;

public class Server {

  private static final GestorTokens gestorTokens = new GestorTokens();

  private static final List<String> protectedPaths = List.of(
  );

  public void start() {
    Bootstrap.init();
    var app = Javalin.create(config -> {
      initializeStaticFiles(config);
      initializeTemplating(config);
    });
    initializeSecurity(app);

    new Router().configure(app);
    app.start(9001);
  }

  private void initializeSecurity(Javalin app) {

    app.before(ctx -> {
      String token = ctx.cookie("jwt_token");
      AppRole userRole = AppRole.ANYONE;

      if (token != null) {
        DecodedJWT jwt = gestorTokens.validateToken(token);
        if (jwt != null) {
          try {
            String roleString = jwt.getClaim("role").asString();
            userRole = AppRole.valueOf(roleString);
            ctx.attribute("username", jwt.getSubject());
          } catch (Exception e) {  }
        } else {
          ctx.removeCookie("jwt_token", "/");
        }
      }

      ctx.attribute("userRole", userRole);

      String requestPath = ctx.path();

      boolean isProtected = protectedPaths.stream().anyMatch(requestPath::startsWith);

      if (isProtected) {
        boolean hasPermission = (userRole == AppRole.USER || userRole == AppRole.ADMIN);

        if (!hasPermission) {
          ctx.redirect("/login");
        }

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

    config.staticFiles.add(staticFileConfig -> {
      staticFileConfig.hostedPath = "/uploads";
      staticFileConfig.directory = "uploads"; // carpeta fuera de src/main/resources
      staticFileConfig.location = io.javalin.http.staticfiles.Location.EXTERNAL;
      staticFileConfig.headers = Map.of("Cache-Control", "max-age=0");
    });
  }
}