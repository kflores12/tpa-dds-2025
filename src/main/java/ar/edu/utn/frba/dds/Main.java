package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dominio.Administrador;
import ar.edu.utn.frba.dds.dominio.Categoria;
import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.ContribuyenteAnonimo;
import ar.edu.utn.frba.dds.dominio.Fuente;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.RegistroDeColecciones;
import ar.edu.utn.frba.dds.dominio.TipoDeHecho;
import java.time.LocalDate;

public class Main {
  public static void main(String[] args) {

    //ADMINISTRADOR CREA UNA NUEVA COLECCION
    /*
    DECISIONES TOMADAS:
      - el administrador establece al momento de crear la coleccion desde donde obtendra sus datos
      - esto puede ser modificado en funcion de los requerimientos
    */

    Administrador administrador1 = new Administrador();

    administrador1.traerColeccionDesdeDataSet(Categoria.INCENDIO_FORESTAL,
        "Incendios Forestales en Argentina",
        "Compendio de noticias sobre incendios en la Republica Argentina",
        null, "datos.CSV");

    //PRUEBA DE QUE SER CARGO EXITOSAMENTE
    for (Coleccion c : RegistroDeColecciones.getColeccionesDisponibles()) {
      System.out.println(c.getListaHechos());
    }

  }


}
