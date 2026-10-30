package modelo.enu;
public enum NivelPrioridad {
 CRITICA(4,"Crítica"), ALTA(3,"Alta"), MEDIA(2,"Media"), BAJA(1,"Baja");
 private final int peso; private final String etiqueta;
 NivelPrioridad(int peso,String etiqueta){this.peso=peso;this.etiqueta=etiqueta;}
 public int getPeso(){return peso;}
 @Override public String toString(){return etiqueta;}
}