package modelo;

public class Tramo {
    private final String origenId;
    private final String destinoId;
    private double distanciaKm;
    private boolean bloqueado;

    public Tramo(String origenId, String destinoId, double distanciaKm) {
        if (origenId == null || destinoId == null || origenId.equals(destinoId)) {
            throw new IllegalArgumentException("Una vía debe conectar dos ubicaciones diferentes.");
        }
        this.origenId = origenId;
        this.destinoId = destinoId;
        setDistanciaKm(distanciaKm);
    }

    public void setDistanciaKm(double distanciaKm) {
        if (!Double.isFinite(distanciaKm) || distanciaKm <= 0) {
            throw new IllegalArgumentException("La distancia debe ser un número positivo.");
        }
        this.distanciaKm = distanciaKm;
    }

    public String getOrigenId() { return origenId; }
    public String getDestinoId() { return destinoId; }
    public double getDistanciaKm() { return distanciaKm; }
    public boolean isBloqueado() { return bloqueado; }
    public void setBloqueado(boolean bloqueado) { this.bloqueado = bloqueado; }

    @Override
    public String toString() {
        return origenId + " ↔ " + destinoId + " (" + distanciaKm + " km)"
                + (bloqueado ? " [BLOQUEADA]" : " [DISPONIBLE]");
    }
}
