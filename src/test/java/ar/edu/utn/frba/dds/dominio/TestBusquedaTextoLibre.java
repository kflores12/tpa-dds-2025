package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.repositorios.RepositorioHechos;
import ar.edu.utn.frba.dds.dominio.fuentes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestBusquedaTextoLibre {

    @Mock
    private RepositorioHechos repositorioHechosMock;

    private BusquedaTextoLibre busquedaTextoLibre;
    private Hecho hechoPrimero;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        busquedaTextoLibre = new BusquedaTextoLibre(repositorioHechosMock);

        hechoPrimero = new Hecho(
            "Accidente de transito",
            "Choque en Av. Corrientes",
            "Transito",
            21.2,
            12.8,
            LocalDate.of(2025, 1, 1),
            LocalDate.now(),
            TipoFuente.DINAMICA,
            "",
            Boolean.TRUE
        );
    }

    @Test
    void realizarBusquedaConTextoValido() {
        String textoBusqueda = "accidente";
        List<Hecho> resultadosEsperados = Arrays.asList(hechoPrimero);

        when(repositorioHechosMock.buscarPorTextoLibre(textoBusqueda))
            .thenReturn(resultadosEsperados);

        List<Hecho> resultados = busquedaTextoLibre.realizarBusqueda(textoBusqueda);

        assertEquals(1, resultados.size());
        assertEquals(hechoPrimero, resultados.get(0));
        verify(repositorioHechosMock).buscarPorTextoLibre(textoBusqueda);
    }

    @Test
    void realizarBusquedaConTextoConEspacios() {
        String textoBusquedaConEspacios = "  accidente  ";
        String textoBusquedaLimpio = "accidente";
        List<Hecho> resultadosEsperados = Arrays.asList(hechoPrimero);

        when(repositorioHechosMock.buscarPorTextoLibre(textoBusquedaLimpio))
            .thenReturn(resultadosEsperados);

        List<Hecho> resultados = busquedaTextoLibre.realizarBusqueda(textoBusquedaConEspacios);

        assertEquals(1, resultados.size());
        verify(repositorioHechosMock).buscarPorTextoLibre(textoBusquedaLimpio);
    }

    @Test
    void realizarBusquedaConTextoNull() {
        List<Hecho> resultados = busquedaTextoLibre.realizarBusqueda(null);

        assertTrue(resultados.isEmpty());
        verifyNoInteractions(repositorioHechosMock);
    }

    @Test
    void realizarBusquedaConTextoVacio() {
        List<Hecho> resultados = busquedaTextoLibre.realizarBusqueda("");

        assertTrue(resultados.isEmpty());
        verifyNoInteractions(repositorioHechosMock);
    }
}
