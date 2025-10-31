package ar.edu.utn.frba.dds.controllers;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class HomeController {
  public Map<String, Object> index(@NotNull Context ctx) {
    return Map.of(
        "titulo", "MetaMapa: Gestión de Mapeos Colaborativos",
        "mensaje", "Bienvenido a la interfaz Web."
    );
  }
}
