package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dominio.Administrador;
import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.ColeccionBuilder;
import ar.edu.utn.frba.dds.dominio.Etiqueta;
import ar.edu.utn.frba.dds.dominio.Filtro;
import ar.edu.utn.frba.dds.dominio.FiltroTitulo;
import ar.edu.utn.frba.dds.dominio.Fuente;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.RepositorioDeColecciones;
import ar.edu.utn.frba.dds.dominio.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.dominio.TipoDeHecho;
import ar.edu.utn.frba.dds.dominio.Visualizador;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

import static ar.edu.utn.frba.dds.dominio.EstadoSolicitud.ACEPTADA;
import static ar.edu.utn.frba.dds.dominio.EstadoSolicitud.RECHAZADA;
import static org.junit.jupiter.api.Assertions.*;



public class TestAdministrador {

  @Test
  public void TestCargaHechosIncendiosPehuen() {
    FiltroTitulo filtroTP = new FiltroTitulo("Incendio en pehuen");
    assertEquals("Incendio en pehuen",hechosCargados(filtroTP).get(0).getTitulo());
  }

  @Test
  public void TestCargaHechosIncendiosBariloche() {
    FiltroTitulo filtroTB = new FiltroTitulo("Incendio en Bariloche");
    assertEquals("Incendio en Bariloche",hechosCargados(filtroTB).get(0).getTitulo());
  }

  @Test
  public void TestCargaHechosRobosBB() {
    FiltroTitulo filtroTT = new FiltroTitulo("Tiroteo");
    assertEquals("En bahía blanca",hechosCargados(filtroTT).get(0).getDescripcion());
  }

  @Test
  public void TestAceptarSolicitudE() {
    Hecho hecho = new Hecho("Prueba", "Descripción", new Etiqueta("Categoria"),
        0.0, 0.0, LocalDate.of(2024,10,02),
        LocalDate.of(2024,9,5), Fuente.DATASET,
        null, TipoDeHecho.TEXTO);
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(hecho, "Motivo válido");
    Administrador admin = new Administrador();
    admin.aceptarSolicitud(solicitud);
    assertEquals(ACEPTADA,solicitud.getEstado());
  }

  @Test
  public void TestRechazarSolicitudE() {
    Hecho hecho = new Hecho("Prueba", "Descripción", new Etiqueta("Categoria"),
        0.0, 0.0, LocalDate.of(2024,10,02),
        LocalDate.of(2024,9,5), Fuente.DATASET,
        null, TipoDeHecho.TEXTO);
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(hecho, "Motivo válido");
    Administrador admin = new Administrador();
    admin.rechazarSolicitud(solicitud);
    assertEquals(RECHAZADA,solicitud.getEstado());
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
      if (!admin1.visualizarHechos(filtro, c).isEmpty()) {
        System.out.println(admin1.visualizarHechos(filtro,c));
        return admin1.visualizarHechos(filtro,c);
      }
    }

    return Collections.emptyList();
  }

}
