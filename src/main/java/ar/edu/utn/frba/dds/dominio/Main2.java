package ar.edu.utn.frba.dds.dominio;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//No esta terminado todavia
public class Main2 {
  public static void main(String[] args) {
    System.out.println("INICIO: Ejecución iniciada a las " + LocalDateTime.now());
    try {

      RepositorioHechos repositorioDeHechos = new RepositorioHechos();
      RepositorioHechos repositorioDeHechos2 = new RepositorioHechos();
      SolicitudDeCarga solicitudDeCargaPrimera = new SolicitudDeCarga(
          "Corte de luz",
          "Corte de luz en zona sur",
          "cortes",
          21.2,
          12.8,
          LocalDate.of(2025, 1, 1),
          "",
          Boolean.TRUE,
          repositorioDeHechos2);
      solicitudDeCargaPrimera.aprobar("unEvaluador");
      FuenteDinamica fuenteDinamica = new FuenteDinamica(repositorioDeHechos2);
      RepositorioFuentes repositorioDeFuentes = new RepositorioFuentes();
      ConexionMock conexion = new ConexionMock();
      URL url = new URL("http://mock.url");
      FuenteProxyDemo fuenteProxy = new FuenteProxyDemo(conexion, url, repositorioDeHechos);
      fuenteProxy.obtenerHechos();
      FuenteDataSet fuenteDataSet = new FuenteDataSet(
          "/home/spinozista/tpa-2025-05/EjHechos.csv", "d/M/yyyy", ';');
      repositorioDeFuentes.registrarFuente(fuenteDinamica);
      repositorioDeFuentes.registrarFuente(fuenteDataSet);
      repositorioDeFuentes.registrarFuente(fuenteProxy);
      FiltroBaseAgregador filtroBase = new FiltroBaseAgregador();
      Agregador agregador = new Agregador(repositorioDeFuentes, filtroBase);
      agregador.agregarHechos();
      List<Hecho> hechos = agregador.getHechos();
      System.out.println("Cantidad de hechos: " + hechos.size());

    } catch (Exception e) {
      System.err.println("ERROR: " + e.getMessage());
    }
    System.out.println("FIN: Ejecución finalizada a las " + LocalDateTime.now());
  }
}


 