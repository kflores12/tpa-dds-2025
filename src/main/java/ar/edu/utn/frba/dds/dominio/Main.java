package ar.edu.utn.frba.dds.dominio;

import java.net.URL;
import java.time.*;
import java.util.List;
import java.util.concurrent.*;
public class Main {

    public static void main(String[] args) {
        try {


public class Main {
  public static void main(String[] args) {
    System.out.println("INICIO: Ejecución iniciada a las " + LocalDateTime.now());
    try {
      Hecho hecho = new Hecho("Corte de luz Dinamica",
          "Corte de luz en zona sur", "cortes", 21.2,
          12.8, LocalDate.of(2025, 1, 1),
          LocalDate.now(), TipoFuente.DINAMICA, "", Boolean.TRUE);


      RepositorioHechos repositorioDeHechos = new RepositorioHechos();
      RepositorioHechos repositorioDeHechos2 = new RepositorioHechos();
      repositorioDeHechos2.cargarHecho(hecho);
      FuenteDinamica fuenteDinamica = new FuenteDinamica(repositorioDeHechos2);
      RepositorioFuentes repositorioDeFuentes = new RepositorioFuentes();
      ConexionMock conexion = new ConexionMock();
      URL url = new URL("http://mock.url");
      FuenteProxyDemo fuenteProxy = new FuenteProxyDemo(conexion, url, repositorioDeHechos);
      fuenteProxy.obtenerHechos();
      FuenteDataSet fuenteDataSet = new FuenteDataSet("EjHechos.csv", "d/M/yyyy",
          ';');
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

        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}



