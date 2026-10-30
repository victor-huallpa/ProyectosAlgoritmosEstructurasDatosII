package modelo;

import java.util.*;

public class Grafo {
    private final Map<String, Ubicacion> ubicaciones = new LinkedHashMap<>();
    private final List<Tramo> tramos = new ArrayList<>();
    private final Map<String, List<Tramo>> adyacencias = new LinkedHashMap<>();

    public void agregarUbicacion(Ubicacion ubicacion) {
        Objects.requireNonNull(ubicacion, "La ubicación es obligatoria");
        if (ubicaciones.containsKey(ubicacion.getId())) {
            throw new IllegalArgumentException("Ya existe una ubicación con ese identificador.");
        }
        boolean nombreExiste = ubicaciones.values().stream()
                .anyMatch(actual -> actual.getNombre().equalsIgnoreCase(ubicacion.getNombre()));
        if (nombreExiste) {
            throw new IllegalArgumentException("Ya existe una ubicación con ese nombre.");
        }
        ubicaciones.put(ubicacion.getId(), ubicacion);
        adyacencias.put(ubicacion.getId(), new ArrayList<>());
    }

    public void agregarTramo(String origenId, String destinoId, double distanciaKm) {
        if (!ubicaciones.containsKey(origenId) || !ubicaciones.containsKey(destinoId)) {
            throw new IllegalArgumentException("Selecciona dos ubicaciones existentes.");
        }
        if (origenId.equals(destinoId)) {
            throw new IllegalArgumentException("No se puede conectar una ubicación consigo misma.");
        }
        boolean duplicado = tramos.stream().anyMatch(t ->
                (t.getOrigenId().equals(origenId) && t.getDestinoId().equals(destinoId))
                || (t.getOrigenId().equals(destinoId) && t.getDestinoId().equals(origenId)));
        if (duplicado) {
            throw new IllegalArgumentException("Esa vía ya existe.");
        }

        Tramo tramo = new Tramo(origenId, destinoId, distanciaKm);
        tramos.add(tramo);
        adyacencias.get(origenId).add(tramo);
        adyacencias.get(destinoId).add(tramo);
    }

    public Collection<Ubicacion> getUbicaciones() {
        return Collections.unmodifiableCollection(ubicaciones.values());
    }

    public List<Tramo> getTramos() {
        return Collections.unmodifiableList(tramos);
    }

    public List<Tramo> obtenerAdyacentes(String ubicacionId) {
        List<Tramo> lista = adyacencias.get(ubicacionId);
        if (lista == null) throw new IllegalArgumentException("Ubicación no encontrada.");
        return Collections.unmodifiableList(lista);
    }

    public Ubicacion obtenerUbicacion(String id) {
        return ubicaciones.get(id);
    }

    public Ubicacion obtenerDestino(Tramo tramo, String ubicacionActualId) {
        String otroId = tramo.getOrigenId().equals(ubicacionActualId)
                ? tramo.getDestinoId() : tramo.getOrigenId();
        return ubicaciones.get(otroId);
    }

    public int cantidadUbicaciones() { return ubicaciones.size(); }
    public int cantidadTramos() { return tramos.size(); }
}
