package servicio;

import modelo.*;
import modelo.enu.EstadoUnidad;
import modelo.enu.TipoUbicacion;
import modelo.enu.TipoUnidad;

import java.util.List;

public final class DatosIniciales {
    private DatosIniciales() {}

    public record Escenario(Grafo grafo, List<UnidadEmergencia> unidades) {}

    public static Escenario crearEscenario() {
        Grafo grafo = new Grafo();

        // Coordenadas sintéticas en un plano 0–1000 para visualizar el grafo.
        grafo.agregarUbicacion(new Ubicacion("U01", "Plaza de Armas", TipoUbicacion.OTRO, 500, 480));
        grafo.agregarUbicacion(new Ubicacion("U02", "Mercado", TipoUbicacion.INTERSECCION, 330, 300));
        grafo.agregarUbicacion(new Ubicacion("U03", "Av Circunvalacion", TipoUbicacion.INTERSECCION, 160, 520));
        grafo.agregarUbicacion(new Ubicacion("U04", "Av. Principal", TipoUbicacion.INTERSECCION, 700, 300));
        grafo.agregarUbicacion(new Ubicacion("U05", "Zona Norte", TipoUbicacion.INTERSECCION, 650, 120));
        grafo.agregarUbicacion(new Ubicacion("U06", "Zona Sur", TipoUbicacion.INTERSECCION, 520, 820));
        grafo.agregarUbicacion(new Ubicacion("U07", "Hospital Essalud", TipoUbicacion.HOSPITAL, 820, 620));
        grafo.agregarUbicacion(new Ubicacion("U08", "Hospital Santa María", TipoUbicacion.HOSPITAL, 120, 820));
        grafo.agregarUbicacion(new Ubicacion("U09", "Bomberos", TipoUbicacion.ESTACION_BOMBEROS, 850, 120));
        grafo.agregarUbicacion(new Ubicacion("U10", "Comisaría", TipoUbicacion.ESTACION_POLICIAL, 300, 650));
        grafo.agregarUbicacion(new Ubicacion("U11", "Urb. Capilla", TipoUbicacion.INTERSECCION, 900, 400));
        grafo.agregarUbicacion(new Ubicacion("U12", "UNAJ", TipoUbicacion.OTRO, 100, 250));
        grafo.agregarUbicacion(new Ubicacion("U13", "Urb. Rinconada", TipoUbicacion.OTRO, 400, 850));

        grafo.agregarTramo("U01", "U02", 1.2);
        grafo.agregarTramo("U01", "U04", 1.5);
        grafo.agregarTramo("U01", "U06", 2.0);
        grafo.agregarTramo("U01", "U10", 1.7);
        grafo.agregarTramo("U02", "U03", 2.1);
        grafo.agregarTramo("U02", "U05", 2.4);
        grafo.agregarTramo("U02", "U12", 1.8);
        grafo.agregarTramo("U03", "U08", 2.0);
        grafo.agregarTramo("U03", "U10", 1.4);
        grafo.agregarTramo("U04", "U05", 1.1);
        grafo.agregarTramo("U04", "U07", 2.3);
        grafo.agregarTramo("U04", "U11", 1.6);
        grafo.agregarTramo("U05", "U09", 1.3);
        grafo.agregarTramo("U06", "U07", 2.2);
        grafo.agregarTramo("U06", "U08", 2.5);
        grafo.agregarTramo("U07", "U11", 1.4);
        grafo.agregarTramo("U10", "U08", 1.9);
        grafo.agregarTramo("U12", "U03", 2.2);
        grafo.agregarTramo("U13", "U01", 2.5);
        grafo.agregarTramo("U13", "U02", 3.2);
        grafo.agregarTramo("U13", "U03", 2.1);

        List<UnidadEmergencia> unidades = List.of(
                new UnidadEmergencia("AMB-01", TipoUnidad.AMBULANCIA, "U07", EstadoUnidad.DISPONIBLE),
                new UnidadEmergencia("AMB-02", TipoUnidad.AMBULANCIA, "U08", EstadoUnidad.DISPONIBLE),
                new UnidadEmergencia("AMB-03", TipoUnidad.AMBULANCIA, "U01", EstadoUnidad.DISPONIBLE),
                new UnidadEmergencia("AMB-04", TipoUnidad.AMBULANCIA, "U03", EstadoUnidad.DISPONIBLE),
                new UnidadEmergencia("BOM-01", TipoUnidad.BOMBEROS, "U09", EstadoUnidad.DISPONIBLE),
                new UnidadEmergencia("BOM-02", TipoUnidad.BOMBEROS, "U01", EstadoUnidad.DISPONIBLE)
        );

        return new Escenario(grafo, unidades);
    }
}
