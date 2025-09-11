package ar.edu.utn.frba.dds.dominio;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.dominio.criterios.Criterio;
import ar.edu.utn.frba.dds.dominio.criterios.CriterioCategoria;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteApi;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FuenteAPITest {
    private MockWebServer mockWebServer;
    private FuenteApi fuenteApi;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        fuenteApi = new FuenteApi(mockWebServer.url("/").toString(), null);


        List<Fuente> lista = new ArrayList<>();
        lista.add(fuenteApi);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void testObtenerHechos() throws Exception {
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

        List<Hecho> hechos = fuenteApi.getHechos();

        assertEquals(2, hechos.size());
        assertEquals("Incendio en reserva natural", hechos.get(0).getTitulo());
        assertEquals("choque entre tres autos", hechos.get(1).getTitulo());
    }

    @Test
    void testObtenerHechos2() throws Exception {
        List<Criterio> criterios = new ArrayList<Criterio>();
        criterios.add(new CriterioCategoria("desastre natural"));
        Coleccion coleccion = new Coleccion("abc","abc",fuenteApi,
            criterios,"1" ,null);
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
            }
        ]""";

        mockWebServer.enqueue(new MockResponse()
            .setBody(jsonResponse)
            .addHeader("Content-Type", "application/json"));

        List<Hecho> hechos = coleccion.obtnerHechos();

        assertEquals(1, hechos.size());
        assertEquals("Incendio en reserva natural", hechos.get(0).getTitulo());
    }
}