package modelo.enu;
public enum TipoEmergencia {
 MEDICA("Emergencia médica"), INCENDIO("Incendio"), ACCIDENTE("Accidente de tránsito"), RESCATE("Rescate");
 private final String etiqueta;
 TipoEmergencia(String etiqueta){this.etiqueta=etiqueta;}
 @Override public String toString(){return etiqueta;}
}