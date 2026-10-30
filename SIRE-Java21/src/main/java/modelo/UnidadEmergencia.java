package modelo;

import modelo.enu.EstadoUnidad;
import modelo.enu.TipoUnidad;

import java.util.Objects;

public class UnidadEmergencia {
    private final String codigo;
    private final TipoUnidad tipo;
    private String ubicacionId;
    private EstadoUnidad estado;

    public UnidadEmergencia(String codigo, TipoUnidad tipo, String ubicacionId, EstadoUnidad estado) {
        if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("Código obligatorio.");
        this.codigo = codigo.trim();
        this.tipo = Objects.requireNonNull(tipo);
        this.ubicacionId = Objects.requireNonNull(ubicacionId);
        this.estado = Objects.requireNonNull(estado);
    }

    public String getCodigo() { return codigo; }
    public TipoUnidad getTipo() { return tipo; }
    public String getUbicacionId() { return ubicacionId; }
    public EstadoUnidad getEstado() { return estado; }
    public void setUbicacionId(String ubicacionId) { this.ubicacionId = Objects.requireNonNull(ubicacionId); }
    public void setEstado(EstadoUnidad estado) { this.estado = Objects.requireNonNull(estado); }

    @Override public String toString() {
        return codigo + " — " + tipo + " — " + estado;
    }
}
