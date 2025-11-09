package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.repositories.RepositorioHechos;
import ar.edu.utn.frba.dds.server.AppRole;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class HomeController {

  private final RepositorioHechos repoHechos = RepositorioHechos.getInstance();

  public Map<String, Object> index(@NotNull Context ctx) {
    AppRole rol = ctx.attribute("userRole");
    boolean esRegistrado = rol == AppRole.USER || rol == AppRole.ADMIN;
    boolean esAdmin = rol == AppRole.ADMIN;
    String username = ctx.attribute("username");

    // Obtener solo los hechos disponibles (no eliminados)
    List<Hecho> hechosDisponibles = repoHechos.obtenerDisponibles();

    Map<String, Object> model = new HashMap<>();
    model.put("titulo", "MetaMapa: Gestión de Mapeos Colaborativos");
    model.put("mensaje", esRegistrado
        ? "Bienvenido, " + (username != null ? username : "Usuario") +
        ". Podés registrar y gestionar tus hechos."
        : "Estás navegando como visitante. Podés ver hechos en el mapa o cargar nuevos de forma anónima.");
    model.put("hechos", hechosDisponibles);
    model.put("esRegistrado", esRegistrado);
    model.put("esAdmin", esAdmin);
    model.put("username", username != null ? username : "Invitado");

    return model;
  }
}
