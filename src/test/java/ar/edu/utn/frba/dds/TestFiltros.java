package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dominio.Administrador;
import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.ColeccionBuilder;
import ar.edu.utn.frba.dds.dominio.Etiqueta;
import ar.edu.utn.frba.dds.dominio.Filtro;
import ar.edu.utn.frba.dds.dominio.FiltroBase;
import ar.edu.utn.frba.dds.dominio.FiltroCategoria;
import ar.edu.utn.frba.dds.dominio.FiltroDescripcion;
import ar.edu.utn.frba.dds.dominio.FiltroFecha;
import ar.edu.utn.frba.dds.dominio.FiltroTitulo;
import ar.edu.utn.frba.dds.dominio.FiltroUbicacion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.RepositorioDeColecciones;
import ar.edu.utn.frba.dds.dominio.Visualizador;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


  public class TestFiltros {

    @Test
    public void TestFiltroBase() {
      FiltroBase filtroB = new FiltroBase();
      List<String> resultadoEsperado = List.of("Incendio en Bariloche","Incendio en pehuen");
      List<String> hechos = hechosCargados(filtroB).stream().map(hecho -> hecho.getTitulo()).toList();
      assertTrue(hechos.containsAll(resultadoEsperado));
    }

//    @Test
//    public void TestFiltroCategoria() {
//      Etiqueta eti = new Etiqueta("INCENDIO_FORESTAL");
//      FiltroCategoria filtroC = new FiltroCategoria(eti);
//      hechosCargados(filtroC);
//      //assertEquals("Incendio en pehuen",hechosCargados(filtroC).get(0).getTitulo());
//    }

    @Test
    public void TestFiltroDescricion() {
      FiltroDescripcion filtroD = new FiltroDescripcion("En bahía blanca");
      assertEquals("Tiroteo",hechosCargados(filtroD).get(0).getTitulo());
    }

    @Test
    public void TestFiltroFecha() {
      LocalDate fechas = LocalDate.of(2023, 10, 9);
      FiltroFecha filtroF = new FiltroFecha(fechas);
      assertEquals("Incendio en pehuen",hechosCargados(filtroF).get(0).getTitulo());
    }

    @Test
    public void TestFiltroTitulo() {
      FiltroTitulo filtroT = new FiltroTitulo("Incendio en Bariloche");
      assertEquals("Incendios desastrosos",hechosCargados(filtroT).get(0).getDescripcion());
    }
//
    @Test
    public void TestFiltroUbicacion() {
      FiltroUbicacion filtroUbi = new FiltroUbicacion(10.34,67.32);
      assertEquals("Incendio en Bariloche",hechosCargados(filtroUbi).get(0).getTitulo());
    }

    private Administrador adminPostCarga() {

      Administrador administrador1 = new Administrador();
      ColeccionBuilder ColIncendios = new ColeccionBuilder("Inciendios forestales","estado de incendios anual", new Etiqueta("INCENDIO_forestal"));
      ColeccionBuilder ColRobos = new ColeccionBuilder("Robos","Robos anuales", new Etiqueta("ROBO"));

      administrador1.cargarColeccionDesdeDataSet("datos.csv", ColIncendios);
      administrador1.cargarColeccionDesdeDataSet("datos.csv", ColRobos);

      return administrador1;
    }

    private List<Hecho> hechosCargados(Filtro filtro) {

      Administrador admin1 = this.adminPostCarga();

      for (Coleccion c : RepositorioDeColecciones.getColeccionesDisponibles()) {
        //System.out.println(admin1.visualizarHechos(filtro,c));
        if (!admin1.visualizarHechos(filtro, c).isEmpty()) {
          //System.out.println(admin1.visualizarHechos(filtro,c));
          return admin1.visualizarHechos(filtro,c);
        }
      }

      return Collections.emptyList();
    }

  }
