package ar.edu.utn.frba.dds.dominio;

import java.util.UUID;

public class GeneradorHandleUUID {
  public String generar() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
