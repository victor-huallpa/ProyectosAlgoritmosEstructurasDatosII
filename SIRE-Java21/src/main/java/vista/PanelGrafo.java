package vista;

import modelo.*;
import modelo.enu.TipoUbicacion;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class PanelGrafo extends JPanel {
    private final Grafo grafo;
    private List<String> ruta = List.of();
    private static final int RADIO = 22;

    public PanelGrafo(Grafo grafo) {
        this.grafo = grafo;
        setBackground(new Color(18, 25, 38));
        setPreferredSize(new Dimension(760, 620));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setToolTipText("Representación académica del grafo vial");
    }

    public void mostrarRuta(List<String> ubicaciones) {
        ruta = List.copyOf(ubicaciones);
        repaint();
    }

    public void limpiarRuta() {
        ruta = List.of();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int margen = 55;
        int ancho = Math.max(1, getWidth() - margen * 2);
        int alto = Math.max(1, getHeight() - margen * 2);
        Set<String> aristasRuta = new HashSet<>();
        for (int i = 0; i + 1 < ruta.size(); i++) aristasRuta.add(clave(ruta.get(i), ruta.get(i + 1)));

        // Las vías se dibujan primero; las vías de la ruta se resaltan en amarillo.
        for (Tramo tramo : grafo.getTramos()) {
            Ubicacion a = grafo.obtenerUbicacion(tramo.getOrigenId());
            Ubicacion b = grafo.obtenerUbicacion(tramo.getDestinoId());
            if (a == null || b == null) continue;
            int x1 = margen + a.getX() * ancho / 1000, y1 = margen + a.getY() * alto / 1000;
            int x2 = margen + b.getX() * ancho / 1000, y2 = margen + b.getY() * alto / 1000;
            boolean seleccionada = aristasRuta.contains(clave(tramo.getOrigenId(), tramo.getDestinoId()));
            g.setStroke(new BasicStroke(seleccionada ? 5f : 2f));
            g.setColor(tramo.isBloqueado() ? new Color(230, 85, 85)
                    : seleccionada ? new Color(245, 190, 65) : new Color(91, 112, 139));
            g.drawLine(x1, y1, x2, y2);
            String distancia = String.format(java.util.Locale.US, "%.1f km", tramo.getDistanciaKm());
            g.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g.setColor(new Color(218, 226, 238));
            g.drawString(distancia, (x1 + x2) / 2 + 4, (y1 + y2) / 2 - 5);
        }

        for (Ubicacion ubicacion : grafo.getUbicaciones()) {
            int x = margen + ubicacion.getX() * ancho / 1000;
            int y = margen + ubicacion.getY() * alto / 1000;
            g.setColor(new Color(0, 0, 0, 90));
            g.fill(new Ellipse2D.Double(x - RADIO + 3, y - RADIO + 4, RADIO * 2, RADIO * 2));
            g.setColor(colorPorTipo(ubicacion.getTipo()));
            g.fill(new Ellipse2D.Double(x - RADIO, y - RADIO, RADIO * 2, RADIO * 2));
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2f));
            g.draw(new Ellipse2D.Double(x - RADIO, y - RADIO, RADIO * 2, RADIO * 2));
            g.setFont(new Font("SansSerif", Font.BOLD, 11));
            FontMetrics fm = g.getFontMetrics();
            g.setColor(new Color(235, 241, 250));
            g.drawString(ubicacion.getNombre(), x - fm.stringWidth(ubicacion.getNombre()) / 2, y + RADIO + 16);
        }
        g.dispose();
    }

    private String clave(String a, String b) { return a.compareTo(b) < 0 ? a + "|" + b : b + "|" + a; }

    private Color colorPorTipo(TipoUbicacion tipo) {
        return switch (tipo) {
            case HOSPITAL -> new Color(30, 155, 125);
            case ESTACION_BOMBEROS -> new Color(220, 91, 68);
            case ESTACION_POLICIAL -> new Color(65, 125, 210);
            case INTERSECCION -> new Color(100, 116, 139);
            case OTRO -> new Color(151, 111, 190);
        };
    }
}
