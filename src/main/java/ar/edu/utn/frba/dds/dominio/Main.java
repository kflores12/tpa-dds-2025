package ar.edu.utn.frba.dds.dominio;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import com.fatboyindustrial.gsonjavatime.Converters;


public class Main {
    public static void main(String[] args){
       List<Criterio> criterios = new ArrayList<>();
        FuenteAPI fuente = new FuenteAPI("https://f515b336-498e-4216-a0a4-4c477369ada8.mock.pstmn.io", null);
        
        Coleccion coleccion = new Coleccion("Incendios","",fuente, criterios, "12");

        List<Hecho> hechos = coleccion.obtenerTodosLosHechos();
        System.out.println("Hechos en la colección: " + hechos.size());
        hechos.forEach(h -> System.out.println(h.getTitulo())); 
   

   

// ...

 /* PostSolicitud post = new PostSolicitud("https://f515b336-498e-4216-a0a4-4c477369ada8.mock.pstmn.io");

Hecho hecho = new Hecho("Incendio", "Beee", "Desastres naturales", -32.198, -15.354,
        LocalDate.now(), LocalDate.now(), TipoFuente.DATASET, "", true);

SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(hecho, "beeee", EstadoSolicitud.PENDIENTE);

 System.out.println("Enviando solicitud al mock...");
 post.crearSolicitud(solicitud); */

}
}
