package ar.edu.utn.frba.dds;

import static ar.edu.utn.frba.dds.dominio.Fuente.CONTRIBUYENTE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.utn.frba.dds.dominio.Administrador;
import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.ColeccionBuilder;
import ar.edu.utn.frba.dds.dominio.Contribuyente;
import ar.edu.utn.frba.dds.dominio.Etiqueta;
import ar.edu.utn.frba.dds.dominio.Filtro;
import ar.edu.utn.frba.dds.dominio.FiltroDescripcion;
import ar.edu.utn.frba.dds.dominio.FiltroTitulo;
import ar.edu.utn.frba.dds.dominio.FiltroUbicacion;
import ar.edu.utn.frba.dds.dominio.Fuente;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.RepositorioSolicitudes;
import ar.edu.utn.frba.dds.dominio.TipoDeHecho;
import ar.edu.utn.frba.dds.dominio.Visualizador;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TestVisualizador {

  @Test
  public void TestVisualizadorUnFiltro() {
    FiltroTitulo filtroD = new FiltroTitulo("Incendio uno");
    Coleccion coleIncendios = coleccionConHechosCargados();
    Visualizador v = new Visualizador();

    assertEquals("Incendio uno",v.visualizarHechos(filtroD, coleIncendios).get(0).getTitulo());
  }

  @Test
  public void TestVisualizadorMultiplesFiltro() {
    FiltroTitulo filtroT = new FiltroTitulo("Incendio dos");
    FiltroUbicacion filtroUbi = new FiltroUbicacion(22.4,23.3);
    List<Filtro> MultiFiltros = new ArrayList<>();
    MultiFiltros.add(filtroT);
    MultiFiltros.add(filtroUbi);

    Coleccion coleIncendios = coleccionConHechosCargados();
    Visualizador v = new Visualizador();

    List<String> resultadoEsperado = List.of("Incendio tres","Incendio dos");
    List<String> hechos = v.visualizarMultiplesfiltros(MultiFiltros, coleIncendios).stream().map(Hecho::getTitulo).toList();

    assertTrue(hechos.containsAll(resultadoEsperado));
  }

  @Test
  public void TestVisualizadorSolicitarEliminacionHecho() {
    Visualizador v = new Visualizador();

    Etiqueta eti = new Etiqueta("INCENDIO_FORESTAL");
    LocalDate fechaHecho = LocalDate.of(2024, 5, 9);
    LocalDate fechasCarga = LocalDate.of(2025, 5, 1);
    RepositorioSolicitudes repoSolicitudes = new RepositorioSolicitudes();
    Hecho h1 = new Hecho("Incendio uno","Descripcion Incendio uno",eti,20.2,21.1,fechaHecho,fechasCarga,CONTRIBUYENTE,new Contribuyente(), TipoDeHecho.TEXTO);

    v.solicitarEliminacionhecho(h1, "Deseo borrarla", repoSolicitudes );
    assertEquals("Descripcion Incendio uno",repoSolicitudes.obtenerSolicitudesPendientes().get(0).getHecho().getDescripcion());
  }


  private Coleccion coleccionConHechosCargados() {
    Administrador administrador1 = new Administrador();
    ColeccionBuilder ColIncendios = new ColeccionBuilder("Inciendios forestales","estado de incendios anual", new Etiqueta("INCENDIO_forestal"));
    administrador1.cargarColeccionDesdeDataSet("datos.csv", ColIncendios);

    Etiqueta eti = new Etiqueta("INCENDIO_FORESTAL");
    LocalDate fechaHecho = LocalDate.of(2024, 5, 9);
    LocalDate fechasCarga = LocalDate.of(2025, 5, 1);

    List<Hecho> listaHechos = new ArrayList<>();

    Hecho h1 = new Hecho("Incendio uno","Descripcion Incendio uno",eti,20.2,21.1,fechaHecho,fechasCarga,CONTRIBUYENTE,new Contribuyente(), TipoDeHecho.TEXTO);
    Hecho h2 = new Hecho("Incendio dos","Descripcion Incendio dos",eti,21.3,22.2,fechaHecho,fechasCarga,CONTRIBUYENTE,new Contribuyente(), TipoDeHecho.TEXTO);
    Hecho h3 = new Hecho("Incendio tres","Descripcion Incendio tres",eti,22.4,23.3,fechaHecho,fechasCarga,CONTRIBUYENTE,new Contribuyente(), TipoDeHecho.TEXTO);
    Hecho h4 = new Hecho("Incendio cuatro","Descripcion Incendio cuatro",eti,23.5,24.4,fechaHecho,fechasCarga,CONTRIBUYENTE,new Contribuyente(), TipoDeHecho.TEXTO);

    Coleccion CIncendios = new Coleccion("Incendios","Test Incendios", CONTRIBUYENTE, eti,listaHechos , "CONTRIBUYENTE");

    CIncendios.agregarHecho(h1);
    CIncendios.agregarHecho(h2);
    CIncendios.agregarHecho(h3);
    CIncendios.agregarHecho(h4);

    return CIncendios;
  }

}
