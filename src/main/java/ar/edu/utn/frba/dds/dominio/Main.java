package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.algoritmosconcenso.AmayoriaSimple;
import ar.edu.utn.frba.dds.dominio.fuentes.Agregador;
import ar.edu.utn.frba.dds.dominio.fuentes.ConexionMock;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDataSet;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteProxyDemo;
import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioHechos;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudDeCarga;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    System.out.println("INICIO: Ejecución iniciada a las " + LocalDateTime.now());
    try {

      List<Hecho> repositorioDeHechos = new ArrayList<>();
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
      FuenteDinamica fuenteDinamica = new FuenteDinamica(repositorioDeHechos2);
      ConexionMock conexion = new ConexionMock();
      URL url = new URL("http://mock.url");
      FuenteProxyDemo fuenteProxy = new FuenteProxyDemo(conexion, url, repositorioDeHechos);
      fuenteProxy.obtenerHechos();
      FuenteDataSet fuenteDataSet = new FuenteDataSet("/home/utnso/tpa-2025-05/EjHechos.csv",
              "d/M/yyyy",
              ';');
      List<Fuente> lista = new ArrayList<>();
      lista.add(fuenteDinamica);
      lista.add(fuenteProxy);
      lista.add(fuenteDataSet);
      if ("consensuarHechos".equals(args[0])) {
        consensuarHechos(lista);
      } else if ("ejecutarAgregador".equals(args[0])) {
        ejecutarAgregador(lista);
      }
    } catch (Exception e) {
      System.err.println("ERROR: " + e.getMessage());
    }
    System.out.println("FIN: Ejecución finalizada a las " + LocalDateTime.now());
  }

  private static void consensuarHechos(List<Fuente> lista) throws Exception {
    Agregador agregador = new Agregador(lista);
    agregador.actualizarHechos();
    List<Hecho> hechos = agregador.getHechos();
    FuenteDataSet fuenteDataSet = new FuenteDataSet("/home/utnso/tpa-2025-05/EjHechos.csv",
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

  private static void ejecutarAgregador(List<Fuente> lista) throws Exception {
    Agregador agregador = new Agregador(lista);
    agregador.actualizarHechos();
    List<Hecho> hechos = agregador.getHechos();
    System.out.println("Cantidad de hechos: " + hechos.size());
  }
}


