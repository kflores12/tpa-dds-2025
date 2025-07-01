package ar.edu.utn.frba.dds.dominio;

import java.util.Timer;
import java.util.TimerTask;



public class Sistema {

  public static  void main(String[] args) {

    RepositorioFuentes repositorioFuentes = new RepositorioFuentes();
   // Agregador agregador = new Agregador(repositorioFuentes);

    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        //agregador.agregarHechos();
        //System.out.println("funciona");
      }
    };
    Timer timer = new Timer("Timer");
    long interval = 3600000L; //una hora
    timer.schedule(task,0,interval);

  }
}
