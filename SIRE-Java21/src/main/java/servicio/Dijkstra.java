package servicio;

import modelo.Grafo;
import modelo.Tramo;
import modelo.Ubicacion;

import java.util.*;

/** Calcula la ruta de menor distancia usando una cola de prioridad. */
public class Dijkstra {
    private record Estado(String ubicacionId, double distancia) {}

    public Optional<ResultadoRuta> calcular(Grafo grafo, String origenId, String destinoId) {
        Objects.requireNonNull(grafo, "El grafo es obligatorio");
        if (grafo.obtenerUbicacion(origenId) == null || grafo.obtenerUbicacion(destinoId) == null) {
            throw new IllegalArgumentException("El origen y el destino deben existir en el grafo.");
        }
        if (origenId.equals(destinoId)) {
            return Optional.of(new ResultadoRuta(List.of(origenId), 0));
        }

        Map<String, Double> distancias = new HashMap<>();
        Map<String, String> predecesores = new HashMap<>();
        PriorityQueue<Estado> pendientes = new PriorityQueue<>(Comparator.comparingDouble(Estado::distancia));

        for (Ubicacion ubicacion : grafo.getUbicaciones()) {
            distancias.put(ubicacion.getId(), Double.POSITIVE_INFINITY);
        }
        distancias.put(origenId, 0.0);
        pendientes.offer(new Estado(origenId, 0.0));

        while (!pendientes.isEmpty()) {
            Estado actual = pendientes.poll();
            if (actual.distancia() > distancias.get(actual.ubicacionId())) continue;
            if (actual.ubicacionId().equals(destinoId)) break;

            for (Tramo tramo : grafo.obtenerAdyacentes(actual.ubicacionId())) {
                if (tramo.isBloqueado()) continue;
                Ubicacion vecino = grafo.obtenerDestino(tramo, actual.ubicacionId());
                double nuevaDistancia = actual.distancia() + tramo.getDistanciaKm();
                if (nuevaDistancia < distancias.get(vecino.getId())) {
                    distancias.put(vecino.getId(), nuevaDistancia);
                    predecesores.put(vecino.getId(), actual.ubicacionId());
                    pendientes.offer(new Estado(vecino.getId(), nuevaDistancia));
                }
            }
        }

        double total = distancias.get(destinoId);
        if (!Double.isFinite(total)) return Optional.empty();

        LinkedList<String> camino = new LinkedList<>();
        String cursor = destinoId;
        while (cursor != null) {
            camino.addFirst(cursor);
            if (cursor.equals(origenId)) break;
            cursor = predecesores.get(cursor);
        }
        if (camino.isEmpty() || !camino.getFirst().equals(origenId)) return Optional.empty();
        return Optional.of(new ResultadoRuta(camino, total));
    }
}
