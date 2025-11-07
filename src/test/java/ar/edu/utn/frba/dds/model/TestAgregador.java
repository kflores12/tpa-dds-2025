package ar.edu.utn.frba.dds.model;

import ar.edu.utn.frba.dds.model.entities.GeneradorHandleUuid;
import ar.edu.utn.frba.dds.model.entities.Hecho;
import ar.edu.utn.frba.dds.model.entities.criterios.Criterio;
import ar.edu.utn.frba.dds.model.entities.criterios.CriterioBase;
import ar.edu.utn.frba.dds.model.entities.fuentes.*;
import ar.edu.utn.frba.dds.repositories.RepositorioFuentes;
import ar.edu.utn.frba.dds.repositories.RepositorioHechos;
import ar.edu.utn.frba.dds.repositories.RepositorioSolicitudesDeCarga;
import ar.edu.utn.frba.dds.model.entities.solicitudes.SolicitudDeCarga;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class TestAgregador implements SimplePersistenceTest {
  private static final Logger log = LoggerFactory.getLogger(TestAgregador.class);
  FuenteDinamica fuente;
  FuenteDataSet fuenteDataSet;
  FuenteApi fuenteApi;
  FuenteProxyDemo fuenteProxyDemo;
  List<Fuente> listaAgregador;

  //DINAMICA
  RepositorioHechos repoHechos;
  RepositorioSolicitudesDeCarga repoSolicitudes;
  List<Criterio> criterios;
  Criterio cBase;
  SolicitudDeCarga solicitudDeCargaPrimera;
  Hecho hechoPrimero;
  private RepositorioFuentes fuentesRepo;
  private Agregador agregador;
  GeneradorHandleUuid generador = new GeneradorHandleUuid();

  //API
  private MockWebServer mockWebServer;

  @Mock
  Conexion conexion;

  //PROXY
  private List<Hecho> repositorioDeProxy;

  @BeforeEach
  public void setUp() throws Exception {
    //CRITERIOS
    cBase = new CriterioBase();
    criterios = new ArrayList<>(Arrays.asList(cBase));
    fuente = new FuenteDinamica();
    hechoPrimero = new Hecho("Corte de luz Dinamica",
        "Corte de luz en zona sur","cortes",21.2,
        12.8, LocalDateTime.of(2025,1,1,00,00),
        LocalDateTime.now(),TipoFuente.DINAMICA,"",Boolean.TRUE, fuente);
    //REPO PARA FUENTE DINAMICA
    repoHechos = new RepositorioHechos();
    repoSolicitudes = new RepositorioSolicitudesDeCarga();
    //PARA FUENTE API
    mockWebServer = new MockWebServer();
    mockWebServer.start();
    fuenteApi = new FuenteApi(generador.generar(), mockWebServer.url("/").toString());
    //PARA FUENTE PROXY
    MockitoAnnotations.openMocks(this);
    repositorioDeProxy = new ArrayList<>();
    //URL url = new URL("http://demo.url");
    //FUENTES
    fuenteDataSet = new FuenteDataSet("datos.csv","yyyy-MM-dd HH:mm",',');
    fuenteApi = new FuenteApi(generador.generar(), mockWebServer.url("/").toString());
    fuenteProxyDemo = new FuenteProxyDemo(conexion, "http://demo.url", repositorioDeProxy);

    //REPOSITORIO DE FUENTES
    fuentesRepo = RepositorioFuentes.getInstance();
    //AGREGADOR
    listaAgregador = new ArrayList<>();
    agregador = new Agregador(listaAgregador);

    //CARGAR FUENTES
    //DINAMICA
    solicitudDeCargaPrimera = new SolicitudDeCarga("Corte de luz Dinamica",
        "Corte de luz en zona sur",
        "cortes",
        21.2,
        12.8,
        LocalDateTime.of(2025, 1, 1,00,00),
        "", Boolean.TRUE, fuente);


    repoSolicitudes.registrar(solicitudDeCargaPrimera);
    solicitudDeCargaPrimera.aprobar();


    //PROXYDEMO
    Map<String, Object> hecho1 = new HashMap<>();
    hecho1.put("titulo", "Hecho 1 Proxy");
    hecho1.put("descripcion", "Un hecho interesante");
    hecho1.put("categoria", "Categoría X");
    hecho1.put("latitud", 1.1);
    hecho1.put("longitud", 2.2);
    hecho1.put("fecha acontecimiento", LocalDateTime.of(2024, 1, 1,00,00));
    hecho1.put("fecha carga", LocalDateTime.of(2024, 1, 2,00,00));
    hecho1.put("multimedia", "http://imagen.jpg");

    when(conexion.siguienteHecho(any(URL.class), any(LocalDateTime.class)))
        .thenReturn(hecho1)
        .thenReturn(null);

    fuenteProxyDemo.actualizarHechos();
    //FUENTEAPI
    String jsonResponse = """
        [
            {
                "titulo": "Incendio en reserva natural",
                "descripcion": "Fuego activo en la reserva de Calamuchita",
                "categoria": "desastre natural",
                "latitud": -32.192,
                "longitud": -64.3936,
                "fechaAcontecimiento": "2023-11-05T00:00",
                "fechaDeCarga": "2023-11-06T00:00",
                "origen": "METAMAPA",
                "multimedia": null,
                "disponibilidad": true
            },
            {
                "titulo": "choque entre tres autos",
                "descripcion": "accidente vehicular termina con la vida de 10 personas",
                "categoria": "accidente de transito",
                "latitud": -32.192,
                "longitud": -64.3936,
                "fechaAcontecimiento": "2023-11-05T00:00",
                "fechaDeCarga": "2023-11-06T00:00",
                "origen": "METAMAPA",
                "multimedia": null,
                "disponibilidad": true
            }
        ]""";

    mockWebServer.enqueue(new MockResponse()
        .setBody(jsonResponse)
        .addHeader("Content-Type", "application/json"));
  }


  @Test
  public void cargarDeUnaFuente() {
    agregador.registrarFuente(fuenteProxyDemo);
    //agregador
    agregador.actualizarHechos();
    List<Hecho> hechos = agregador.getHechos();

    Assertions.assertEquals("Hecho 1 Proxy", hechos.get(0).getTitulo());
    Assertions.assertEquals(1,hechos.size());
  }
/*
  @Test
  public void cargarDeCuatroFuentesDiferentes() throws Exception {
    //DINAMICA
    agregador.registrarFuente(fuente);
    //DATASET
    agregador.registrarFuente(fuenteDataSet);
    //PROXYDEMO
    agregador.registrarFuente(fuenteProxyDemo);
    //FUENTEAPI
    agregador.registrarFuente(fuenteApi);

    agregador.actualizarHechos();
    List<Hecho> hechos = agregador.getHechos();

    Assertions.assertEquals("Corte de luz Dinamica",hechos.get(0).getTitulo());
    Assertions.assertEquals("choque entre tres autos",hechos.get(1).getTitulo());
    Assertions.assertEquals("Tiroteo",hechos.get(2).getTitulo());
    Assertions.assertEquals("Incendio en pehuen",hechos.get(3).getTitulo());
    Assertions.assertEquals("Hecho 1 Proxy",hechos.get(4).getTitulo());
    Assertions.assertEquals("Incendio en reserva natural",hechos.get(5).getTitulo());
    Assertions.assertEquals("choque entre tres autos",hechos.get(6).getTitulo());

    Assertions.assertEquals(7,hechos.size());
  }


  @Test
  public void cargarDe2FuentesDspsAgregoUna() throws Exception {

    agregador.registrarFuente(fuenteApi);
    agregador.registrarFuente(fuenteProxyDemo);
    agregador.actualizarHechos();
    List<Hecho> hechos_2_fuentes = agregador.getHechos();

    agregador.registrarFuente(fuente);
    agregador.actualizarHechos();
    List<Hecho> hechos_3_fuentes = agregador.getHechos();

    // ✅ Con la lógica actual solo el proxy devuelve hechos
    Assertions.assertEquals(3, hechos_2_fuentes.size());
    Assertions.assertEquals(3, hechos_3_fuentes.size());

  }


  @Test
  public void cargarDe3FuentesDspsSacoUna() throws Exception {
    //DINAMICA
    agregador.registrarFuente(fuente);
    //PROXYDEMO
    agregador.registrarFuente(fuenteProxyDemo);

    //DATASET
    agregador.registrarFuente(fuenteDataSet);

    agregador.actualizarHechos();
    List<Hecho> hechos_3_fuentes = agregador.getHechos();

    //Parte 2
    agregador.eliminarFuente(fuente);

    agregador.actualizarHechos();
    List<Hecho> hechos_2_fuentes = agregador.getHechos();

    Assertions.assertEquals("Corte de luz Dinamica",hechos_3_fuentes.get(0).getTitulo());
    Assertions.assertEquals("Hecho 1 Proxy",hechos_3_fuentes.get(1).getTitulo());
    Assertions.assertEquals("Incendio en Bariloche",hechos_3_fuentes.get(2).getTitulo());
    Assertions.assertEquals("Tiroteo",hechos_3_fuentes.get(3).getTitulo());
    Assertions.assertEquals("Incendio en pehuen",hechos_3_fuentes.get(4).getTitulo());

    Assertions.assertEquals(5,hechos_3_fuentes.size());

    Assertions.assertEquals("Hecho 1 Proxy",hechos_2_fuentes.get(0).getTitulo());
    Assertions.assertEquals("Incendio en Bariloche",hechos_2_fuentes.get(1).getTitulo());
    Assertions.assertEquals("Tiroteo",hechos_2_fuentes.get(2).getTitulo());
    Assertions.assertEquals("Incendio en pehuen",hechos_2_fuentes.get(3).getTitulo());

    Assertions.assertEquals(4,hechos_2_fuentes.size());
  }
*/

  @AfterEach
  void limpiarValores() throws IOException {
    Hecho hechoPrimero = null;

    mockWebServer.shutdown();
  }

}