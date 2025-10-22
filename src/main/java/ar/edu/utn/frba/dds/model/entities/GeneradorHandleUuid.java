package ar.edu.utn.frba.dds.model.entities;

import java.util.UUID;

public class GeneradorHandleUuid {
  public String generar() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
