package ar.edu.utn.frba.dds.dominio;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//No esta terminado todavia
public class Main {
  public static void main(String[] args) {
    System.out.println("INICIO: Ejecución iniciada a las " + LocalDateTime.now());
    try {

      RepositorioHechos repositorioDeHechos = new RepositorioHechos();
      RepositorioHechos repositorioDeHechos2 = new RepositorioHechos();
      SolicitudDeCarga solicitudDeCargaPrimera = new SolicitudDeCarga("Corte de luz",
          "Corte de luz en zona sur",
          "cortes",
          21.2,
          12.8,
          LocalDate.of(2025, 1, 1),
          "",
          Boolean.TRUE,
          repositorioDeHechos2);
      solicitudDeCargaPrimera.aprobar("unEvaluador");
      FuenteDataSet fuenteDataSet = new FuenteDataSet("/home/spinozista/tpa-2025-05/EjHechos.csv",
          "d/M/yyyy",
          ';');
      FuenteDinamica fuenteDinamica = new FuenteDinamica(repositorioDeHechos2);
      RepositorioFuentes repositorioDeFuentes = new RepositorioFuentes();
      ConexionMock conexion = new ConexionMock();
      URL url = new URL("http://mock.url");
      FuenteProxyDemo fuenteProxy = new FuenteProxyDemo(conexion, url, repositorioDeHechos);
      fuenteProxy.obtenerHechos();
      repositorioDeFuentes.registrarFuente(fuenteDinamica);
      repositorioDeFuentes.registrarFuente(fuenteDataSet);
      repositorioDeFuentes.registrarFuente(fuenteProxy);

      if ("consensuarHechos".equals(args[0])) {
        consensuarHechos(repositorioDeFuentes);
      } else if ("ejecutarAgregador".equals(args[0])) {
        ejecutarAgregador(repositorioDeFuentes);
      }
    } catch (Exception e) {
      System.err.println("ERROR: " + e.getMessage());
    }
    System.out.println("FIN: Ejecución finalizada a las " + LocalDateTime.now());
  }

  private static void consensuarHechos(RepositorioFuentes repositorioDeFuentes) throws Exception {
    var filtroBase = new FiltroBaseAgregador();
    Agregador agregador = new Agregador(repositorioDeFuentes, filtroBase);
    agregador.agregarHechos();
    List<Hecho> hechos = agregador.getHechos();
    FuenteDataSet fuenteDataSet = new FuenteDataSet("/home/spinozista/tpa-2025-05/EjHechos.csv",
          "d/M/yyyy",
          ';');
    Coleccion coleccion = new Coleccion("coleccionPrueba",
            "coleccionPrueba",
            fuenteDataSet,
            List.of(),
            "1",
            new AmayoriaSimple());
    coleccion.actualizarHechosConsensuados(hechos);
  }

  private static void ejecutarAgregador(RepositorioFuentes repositorioDeFuentes) throws Exception {
    FiltroBaseAgregador filtroBase = new FiltroBaseAgregador();
    Agregador agregador = new Agregador(repositorioDeFuentes, filtroBase);
    agregador.agregarHechos();
    List<Hecho> hechos = agregador.getHechos();
    System.out.println("Cantidad de hechos: " + hechos.size());
  }
}


