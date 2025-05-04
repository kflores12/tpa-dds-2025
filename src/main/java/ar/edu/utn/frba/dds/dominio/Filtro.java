package ar.edu.utn.frba.dds.dominio;

import java.time.LocalDate;

public interface Filtro {

  boolean aplicarFiltro(Hecho hecho);


//  public double gananciasDia(LocalDate fecha) {
//    return listaVentas.stream().filter(vta -> (vta.getFecha()).isEqual(fecha))
//        .mapToDouble(vta ->  vta.precioVentaTotal()).sum();
//  }
//
//  public double precioVentaTotal() {
//    return listaPrendas.stream().mapToDouble(elem -> elem.precio()).sum();
//  }
//
//  public double precioVentaTotal() {
//    return listaPrendas.stream().mapToDouble(elem -> elem.precio()).sum();
//  }



}
