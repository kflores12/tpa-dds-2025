package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.repositories.RepositorioHechos;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomeController {

  private final RepositorioHechos repoHechos;

  public HomeController() {
    this.repoHechos = RepositorioHechos.getInstance();
  }

  public Map<String, Object> index(@NotNull Context ctx) {
    // 🔒 Preparado para autenticación futura
    boolean esRegistrado = ctx.sessionAttribute("usuarioRegistrado") != null;

    // ✅ Filtramos solo los hechos disponibles (visibles públicamente)
    List<Hecho> hechosDisponibles = repoHechos
        .obtenerTodos()
        .stream()
        .filter(Hecho::getDisponibilidad)
        .toList();

    return Map.of(
        "titulo", "MetaMapa: Gestión de Mapeos Colaborativos",
        "mensaje", "Explorá los hechos disponibles o contribuí agregando nuevos.",
        "hechos", hechosDisponibles,
        "esRegistrado", esRegistrado
    );
  }
}
