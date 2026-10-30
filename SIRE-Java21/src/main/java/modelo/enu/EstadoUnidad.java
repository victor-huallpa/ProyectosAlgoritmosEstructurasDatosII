package modelo.enu;

public enum EstadoUnidad {
    DISPONIBLE("Disponible"),
    OCUPADA("Ocupada"),
    MANTENIMIENTO("Mantenimiento");

    private final String etiqueta;
    EstadoUnidad(String etiqueta) { this.etiqueta = etiqueta; }
    @Override public String toString() { return etiqueta; }
}
