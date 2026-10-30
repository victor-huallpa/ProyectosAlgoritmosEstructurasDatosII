package modelo.enu;

public enum TipoUbicacion {
    INTERSECCION("Intersección"),
    HOSPITAL("Hospital"),
    ESTACION_BOMBEROS("Estación de bomberos"),
    ESTACION_POLICIAL("Estación policial"),
    OTRO("Otro");

    private final String etiqueta;

    TipoUbicacion(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
