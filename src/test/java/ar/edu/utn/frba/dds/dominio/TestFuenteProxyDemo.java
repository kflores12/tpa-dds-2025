package ar.edu.utn.frba.dds.dominio;
import static org.mockito.Mockito.*;

import ar.edu.utn.frba.dds.dominio.fuentes.Conexion;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteProxyDemo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestFuenteProxyDemo {

  FuenteProxyDemo fuente;

  @Mock
  Conexion conexion;

  private List<Hecho> listadoDeHechos;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    listadoDeHechos = new ArrayList<>();
    URL url = new URL("http://demo.url");

    fuente = new FuenteProxyDemo(conexion, url, listadoDeHechos);

  }

  @Test
  public void solicitarHechos() throws Exception {
    System.out.println("Un hecho obtenido se almacena en el repositorio de hechos ");
    Map<String, Object> hecho1 = new HashMap<>();
    hecho1.put("titulo", "Hecho 1");
    hecho1.put("descripcion", "Un hecho interesante");
    hecho1.put("categoria", "Categoría X");
    hecho1.put("latitud", 1.1);
    hecho1.put("longitud", 2.2);
    hecho1.put("fecha acontecimiento", LocalDate.of(2024, 1, 1));
    hecho1.put("fecha carga", LocalDate.of(2024, 1, 2));
    hecho1.put("multimedia", "http://imagen.jpg");

    when(conexion.siguienteHecho(any(URL.class), any(LocalDateTime.class)))
        .thenReturn(hecho1)
        .thenReturn(null);

    fuente.actualizarHechos();
    List<Hecho> baseDeHechosActualizada = fuente.getHechos();

    Assertions.assertEquals(1, baseDeHechosActualizada.size());
  }

 }


