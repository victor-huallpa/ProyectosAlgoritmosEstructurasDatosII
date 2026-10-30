package modelo;

import modelo.enu.TipoUbicacion;

import java.util.Objects;

public class Ubicacion {
    private final String id;
    private String nombre;
    private TipoUbicacion tipo;
    private int x;
    private int y;

    public Ubicacion(String id, String nombre, TipoUbicacion tipo, int x, int y) {
        this.id = validarTexto(id, "El identificador");
        this.nombre = validarTexto(nombre, "El nombre");
        this.tipo = Objects.requireNonNull(tipo, "El tipo es obligatorio");
        setCoordenadas(x, y);
    }

    private String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
        return valor.trim();
    }

    public void setNombre(String nombre) {
        this.nombre = validarTexto(nombre, "El nombre");
    }

    public void setTipo(TipoUbicacion tipo) {
        this.tipo = Objects.requireNonNull(tipo);
    }

    public void setCoordenadas(int x, int y) {
        if (x < 0 || x > 1000 || y < 0 || y > 1000) {
            throw new IllegalArgumentException("Las coordenadas deben estar entre 0 y 1000.");
        }
        this.x = x;
        this.y = y;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public TipoUbicacion getTipo() { return tipo; }
    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public String toString() {
        return nombre + " [" + tipo + "]";
    }
}
