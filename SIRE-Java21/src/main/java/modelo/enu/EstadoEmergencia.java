package modelo.enu;
public enum EstadoEmergencia {
 PENDIENTE("Pendiente"), ASIGNADA("Unidad asignada"), ATENDIDA("Atendida");
 private final String etiqueta; EstadoEmergencia(String etiqueta){this.etiqueta=etiqueta;}
 @Override public String toString(){return etiqueta;}
}