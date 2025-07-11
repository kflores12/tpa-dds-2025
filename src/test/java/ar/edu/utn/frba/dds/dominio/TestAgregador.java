package ar.edu.utn.frba.dds.dominio;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class TestAgregador {
  private static final Logger log = LoggerFactory.getLogger(TestAgregador.class);
  Fuente fuenteDinamica;
  Fuente fuenteDataSet;
  Fuente fuenteApi;

  //DINAMICA
  RepositorioHechos repoHechos;
  RepositorioSolicitudes repoSolicitudes;
  List<Criterio> criterios;
  Criterio cBase;
  SolicitudDeCarga solicitudDeCargaPrimera;
  Hecho hechoPrimero;
  private RepositorioFuentes fuentesRepo;
  private Agregador agregador;
  GeneradorHandleUuid generador = new GeneradorHandleUuid();

  //API
  private MockWebServer mockWebServer;

  @InjectMocks
  FuenteProxyDemo fuenteProxyDemo;

  @Mock
  Conexion conexion;

  //PROXY
  private RepositorioHechos repositorioDeProxy;

  @BeforeEach
  public void setUp() throws Exception {
    //CRITERIOS
    cBase = new CriterioBase();
    criterios = new ArrayList<>(Arrays.asList(cBase));

    hechoPrimero = new Hecho("Corte de luz Dinamica",
        "Corte de luz en zona sur","cortes",21.2,
        12.8, LocalDate.of(2025,1,1),
        LocalDate.now(),TipoFuente.DINAMICA,"",Boolean.TRUE);
    //REPO PARA FUENTE DINAMICA
    repoHechos = new RepositorioHechos();
    repoSolicitudes = new RepositorioSolicitudes();
    //PARA FUENTE API
    mockWebServer = new MockWebServer();
    mockWebServer.start();
    fuenteApi = new FuenteApi(mockWebServer.url("/").toString(), null);
    //PARA FUENTE PROXY
    MockitoAnnotations.openMocks(this);
    repositorioDeProxy = new RepositorioHechos();
    URL url = new URL("http://demo.url");
    //FUENTES
    fuenteDataSet = new FuenteDataSet("datos.csv","yyyy-MM-dd",',');
    fuenteDinamica = new FuenteDinamica(repoHechos);
    fuenteApi = new FuenteApi(mockWebServer.url("/").toString(), null);
    fuenteProxyDemo = new FuenteProxyDemo(conexion, url, repositorioDeProxy);

    //REPOSITORIO DE FUENTES
    fuentesRepo = new RepositorioFuentes();
    //AGREGADOR
    FiltroAgregador FiltroBaseAgregador =
        new FiltroBaseAgregador();
    agregador = new Agregador(fuentesRepo, FiltroBaseAgregador);

    //CARGAR FUENTES
    //DINAMICA
    solicitudDeCargaPrimera = new SolicitudDeCarga("Corte de luz Dinamica","Corte de luz en zona sur"
        ,"cortes",21.2,12.8, LocalDate.of(2025,1,1),"",Boolean.TRUE,repoHechos);
    repoSolicitudes.agregarSolicitudDeCarga(solicitudDeCargaPrimera);
    List<SolicitudDeCarga> solicitudes = repoSolicitudes.obtenerPendientesDeCarga();
    solicitudes.get(0).aprobar("unEvaluador");
    //PROXYDEMO
    Map<String, Object> hecho1 = new HashMap<>();
    hecho1.put("titulo", "Hecho 1 Proxy");
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

    fuenteProxyDemo.obtenerHechos();
    //FUENTEAPI
    String jsonResponse = """
        [
            {
                "titulo": "Incendio en reserva natural",
                "descripcion": "Fuego activo en la reserva de Calamuchita",
                "categoria": "desastre natural",
                "latitud": -32.192,
                "longitud": -64.3936,
                "fechaAcontecimiento": "2023-11-05",
                "fechaDeCarga": "2023-11-06",
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
                "fechaAcontecimiento": "2023-11-05",
                "fechaDeCarga": "2023-11-06",
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
    fuentesRepo.registrarFuente(fuenteDinamica);
    //agregador
    agregador.agregarHechos();
    List<Hecho> hechos = agregador.getHechos();
    //Reviso que los hechos esten bien cargados (Con sus titulos).

    Assertions.assertEquals(hechos.get(0).getTitulo(),"Corte de luz Dinamica");
    Assertions.assertEquals(1,hechos.size());
  }

  @Test
  public void cargarDeCuatroFuentesDiferentes() throws Exception {
    //DINAMICA
    fuentesRepo.registrarFuente(fuenteDinamica);
    //DATASET
    fuentesRepo.registrarFuente(fuenteDataSet);
    //PROXYDEMO
    fuentesRepo.registrarFuente(fuenteProxyDemo);
    //FUENTEAPI
    fuentesRepo.registrarFuente(fuenteApi);

    agregador.agregarHechos();
    List<Hecho> hechos = agregador.getHechos();

    Assertions.assertEquals("Corte de luz Dinamica",hechos.get(0).getTitulo());
    Assertions.assertEquals("Incendio en Bariloche",hechos.get(1).getTitulo());
    Assertions.assertEquals("Tiroteo",hechos.get(2).getTitulo());
    Assertions.assertEquals("Incendio en pehuen",hechos.get(3).getTitulo());
    Assertions.assertEquals("Hecho 1 Proxy",hechos.get(4).getTitulo());
    Assertions.assertEquals("Incendio en reserva natural",hechos.get(5).getTitulo());
    Assertions.assertEquals("choque entre tres autos",hechos.get(6).getTitulo());

    Assertions.assertEquals(7,hechos.size());
  }


  @Test
  public void cargarDe2FuentesDspsAgregoUna() throws Exception {
    //DINAMICA
    fuentesRepo.registrarFuente(fuenteDinamica);
    //PROXYDEMO
    fuentesRepo.registrarFuente(fuenteProxyDemo);

    agregador.agregarHechos();
    List<Hecho> hechos_2_fuentes = agregador.getHechos();

    //Parte 2
    //DATASET
    fuentesRepo.registrarFuente(fuenteDataSet);

    agregador.agregarHechos();
    List<Hecho> hechos_3_fuentes = agregador.getHechos();

    Assertions.assertEquals("Corte de luz Dinamica",hechos_2_fuentes.get(0).getTitulo());
    Assertions.assertEquals("Hecho 1 Proxy",hechos_2_fuentes.get(1).getTitulo());

    Assertions.assertEquals(2,hechos_2_fuentes.size());

    Assertions.assertEquals("Corte de luz Dinamica",hechos_3_fuentes.get(0).getTitulo());
    Assertions.assertEquals("Hecho 1 Proxy",hechos_3_fuentes.get(1).getTitulo());
    Assertions.assertEquals("Incendio en Bariloche",hechos_3_fuentes.get(2).getTitulo());
    Assertions.assertEquals("Tiroteo",hechos_3_fuentes.get(3).getTitulo());
    Assertions.assertEquals("Incendio en pehuen",hechos_3_fuentes.get(4).getTitulo());

    Assertions.assertEquals(5,hechos_3_fuentes.size());
  }

  @Test
  public void cargarDe3FuentesDspsSacoUna() throws Exception {
    //DINAMICA
    fuentesRepo.registrarFuente(fuenteDinamica);
    //PROXYDEMO
    fuentesRepo.registrarFuente(fuenteProxyDemo);

    //DATASET
    fuentesRepo.registrarFuente(fuenteDataSet);

    agregador.agregarHechos();
    List<Hecho> hechos_3_fuentes = agregador.getHechos();

    //Parte 2
    fuentesRepo.eliminarFuente(fuenteDinamica);

    agregador.agregarHechos();
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

  @Test
  public void filtrarDosFuentes() throws Exception {
    //DINAMICA
    fuentesRepo.registrarFuente(fuenteDinamica);
    //DATASET
    fuentesRepo.registrarFuente(fuenteDataSet);
    //PROXYDEMO
    fuentesRepo.registrarFuente(fuenteProxyDemo);
    //FUENTEAPI
    fuentesRepo.registrarFuente(fuenteApi);

    FiltroAgregador filtroPorTipo =
        new FiltroPorTipo(List.of(fuenteDinamica.getClass(), fuenteDataSet.getClass()));
    Agregador agregador_solo_Dinamico_DataSet = new Agregador(fuentesRepo, filtroPorTipo);

    agregador_solo_Dinamico_DataSet.agregarHechos();
    List<Hecho> hechos = agregador_solo_Dinamico_DataSet.getHechos();

    Assertions.assertEquals("Corte de luz Dinamica",hechos.get(0).getTitulo());
    Assertions.assertEquals("Incendio en Bariloche",hechos.get(1).getTitulo());
    Assertions.assertEquals("Tiroteo",hechos.get(2).getTitulo());
    Assertions.assertEquals("Incendio en pehuen",hechos.get(3).getTitulo());

    Assertions.assertEquals(4,hechos.size());
  }

  @AfterEach
  void limpiarValores() throws IOException {
    Hecho hechoPrimero = null;
    repoHechos.limpiarBaseDeHechos();
    repositorioDeProxy.limpiarBaseDeHechos();
    repoSolicitudes.limpiarListas();

    mockWebServer.shutdown();
  }

}