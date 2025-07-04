package ar.edu.utn.frba.dds.dominio;

import java.net.URL;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {

        System.out.println("INICIO: Ejecución iniciada a las " + LocalDateTime.now());

        try {
            ConexionMock conexion = new ConexionMock();
            URL url = new URL("http://mock.url");
            RepositorioHechos repo = new RepositorioHechos();

            FuenteProxyDemo fuente = new FuenteProxyDemo(conexion, url, repo);
            fuente.obtenerHechos();

            System.out.println("EXITO: Hechos procesados: " + repo.obtenerTodos().size());
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        } 

            System.out.println("FIN: Ejecución finalizada a las " + LocalDateTime.now());
    }
}

