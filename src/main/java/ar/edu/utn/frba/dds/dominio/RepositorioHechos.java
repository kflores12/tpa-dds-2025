package ar.edu.utn.frba.dds.dominio;

import static java.util.Objects.requireNonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioHechos {
  private static List<Hecho> BaseDeHechos = new ArrayList<Hecho>();


  public void CargarHecho(Hecho hecho){
    BaseDeHechos.add(requireNonNull(hecho));
  }

  public void BorrarHecho(Hecho hecho){
    BaseDeHechos.remove(hecho);
  }

  public static List<Hecho> obtenerTodos() {
    return new ArrayList<>(BaseDeHechos);
  }

  public static Hecho buscarHecho(Hecho hecho) {
    return BaseDeHechos.stream().anyMatch(h -> h.equals(hecho)) ? hecho : null;
  }

//  Ver si se modifica aca o en la solicitud de carga
//  public void modificarHecho(Hecho hecho){
//  }

}
