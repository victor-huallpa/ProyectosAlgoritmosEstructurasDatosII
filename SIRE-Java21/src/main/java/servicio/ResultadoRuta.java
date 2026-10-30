package servicio;

import java.util.List;

/** Resultado inmutable de una búsqueda de ruta. */
public record ResultadoRuta(List<String> ubicaciones, double distanciaTotalKm) {
    public ResultadoRuta {
        ubicaciones = List.copyOf(ubicaciones);
    }
}
