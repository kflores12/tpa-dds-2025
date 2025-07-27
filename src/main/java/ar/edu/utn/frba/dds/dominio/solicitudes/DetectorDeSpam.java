package ar.edu.utn.frba.dds.dominio.solicitudes;

public interface DetectorDeSpam {
  boolean esSpam(String texto);
}
