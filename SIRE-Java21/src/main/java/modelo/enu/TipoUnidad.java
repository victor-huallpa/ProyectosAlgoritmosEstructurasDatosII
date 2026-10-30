package modelo.enu;

public enum TipoUnidad {
    AMBULANCIA("Ambulancia"),
    BOMBEROS("Bomberos");

    private final String etiqueta;
    TipoUnidad(String etiqueta) { this.etiqueta = etiqueta; }
    @Override public String toString() { return etiqueta; }
}
