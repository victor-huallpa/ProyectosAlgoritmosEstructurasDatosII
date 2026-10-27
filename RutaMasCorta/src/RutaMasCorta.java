import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * PROGRAMA 1 - GRAFOS
 * Problema real: encontrar la ruta más corta entre ciudades (como un GPS).
 *
 * Estructuras: Grafo no dirigido y ponderado (ciudades = vértices, carreteras = aristas)
 * Algoritmo:   Dijkstra con cola de prioridad (PriorityQueue)
 *
 * Uso:
 *  - Modo "Agregar ciudad": clic en un espacio vacío del mapa.
 *  - Modo "Conectar ciudades": clic en una ciudad y luego en otra; se pide la distancia en km.
 *  - Elegir origen y destino y pulsar "Calcular ruta".
 */
public class RutaMasCorta extends JFrame {

    // ---------- Modelo del grafo ----------
    static class Ciudad {
        final String nombre;
        final int x, y;
        Ciudad(String nombre, int x, int y) { this.nombre = nombre; this.x = x; this.y = y; }
        @Override public String toString() { return nombre; }
    }

    static class Camino {
        final Ciudad a, b;
        final int km;
        Camino(Ciudad a, Ciudad b, int km) { this.a = a; this.b = b; this.km = km; }
        Ciudad otro(Ciudad c) { return c == a ? b : a; }
        boolean une(Ciudad u, Ciudad v) { return (a == u && b == v) || (a == v && b == u); }
    }

    static class Entrada {
        final Ciudad ciudad;
        final int dist;
        Entrada(Ciudad c, int d) { ciudad = c; dist = d; }
    }

    private static final int RADIO = 20;

    private final List<Ciudad> ciudades = new ArrayList<>();
    private final List<Camino> caminos = new ArrayList<>();
    private final Set<Camino> caminosRuta = new HashSet<>();
    private final Set<Ciudad> ciudadesRuta = new HashSet<>();
    private Ciudad seleccionada;

    private final JComboBox<String> modo = new JComboBox<>(new String[]{"Agregar ciudad", "Conectar ciudades"});
    private final DefaultComboBoxModel<Ciudad> modeloOrigen = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<Ciudad> modeloDestino = new DefaultComboBoxModel<>();
    private final JComboBox<Ciudad> cbOrigen = new JComboBox<>(modeloOrigen);
    private final JComboBox<Ciudad> cbDestino = new JComboBox<>(modeloDestino);
    private final JLabel resultado = new JLabel(" Agrega ciudades, conéctalas y calcula la ruta más corta.");
    private final Lienzo lienzo = new Lienzo();

    public RutaMasCorta() {
        super("Ruta más corta entre ciudades (Grafos + Dijkstra)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        barra.add(new JLabel("Modo:"));
        barra.add(modo);
        barra.add(new JLabel("  Origen:"));
        barra.add(cbOrigen);
        barra.add(new JLabel("Destino:"));
        barra.add(cbDestino);
        JButton btnCalcular = new JButton("Calcular ruta");
        JButton btnLimpiarRuta = new JButton("Limpiar ruta");
        JButton btnReiniciar = new JButton("Borrar todo");
        barra.add(btnCalcular);
        barra.add(btnLimpiarRuta);
        barra.add(btnReiniciar);

        resultado.setFont(resultado.getFont().deriveFont(Font.BOLD, 14f));
        resultado.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        add(barra, BorderLayout.NORTH);
        add(lienzo, BorderLayout.CENTER);
        add(resultado, BorderLayout.SOUTH);

        btnCalcular.addActionListener(e -> calcular());
        btnLimpiarRuta.addActionListener(e -> { limpiarRuta(); resultado.setText(" "); lienzo.repaint(); });
        btnReiniciar.addActionListener(e -> {
            ciudades.clear(); caminos.clear(); limpiarRuta(); seleccionada = null;
            modeloOrigen.removeAllElements(); modeloDestino.removeAllElements();
            resultado.setText(" Mapa vacío. Haz clic para agregar ciudades.");
            lienzo.repaint();
        });
        modo.addActionListener(e -> { seleccionada = null; lienzo.repaint(); });

        cargarEjemplo();
        pack();
        setLocationRelativeTo(null);
    }

    // ---------- Operaciones del grafo ----------
    private void agregarCiudad(String nombre, int x, int y) {
        Ciudad c = new Ciudad(nombre, x, y);
        ciudades.add(c);
        modeloOrigen.addElement(c);
        modeloDestino.addElement(c);
    }

    private Ciudad buscar(String nombre) {
        for (Ciudad c : ciudades) if (c.nombre.equals(nombre)) return c;
        return null;
    }

    private void conectar(String a, String b, int km) {
        caminos.add(new Camino(buscar(a), buscar(b), km));
    }

    private void cargarEjemplo() {
        agregarCiudad("Cusco", 600, 110);
        agregarCiudad("Arequipa", 280, 230);
        agregarCiudad("Juliaca", 640, 290);
        agregarCiudad("Puno", 740, 380);
        agregarCiudad("Moquegua", 430, 430);
        agregarCiudad("Tacna", 330, 520);
        // Distancias aproximadas en km
        conectar("Cusco", "Juliaca", 340);
        conectar("Cusco", "Arequipa", 520);
        conectar("Arequipa", "Juliaca", 275);
        conectar("Juliaca", "Puno", 45);
        conectar("Puno", "Moquegua", 270);
        conectar("Arequipa", "Moquegua", 220);
        conectar("Moquegua", "Tacna", 160);
        conectar("Puno", "Tacna", 380);
        cbOrigen.setSelectedIndex(0);
        cbDestino.setSelectedIndex(5);
    }

    private void limpiarRuta() {
        caminosRuta.clear();
        ciudadesRuta.clear();
    }

    /** Dijkstra: devuelve la lista de ciudades del camino más corto (o null si no existe). */
    private List<Ciudad> dijkstra(Ciudad origen, Ciudad destino, int[] distanciaTotal) {
        Map<Ciudad, Integer> dist = new HashMap<>();
        Map<Ciudad, Ciudad> previo = new HashMap<>();
        for (Ciudad c : ciudades) dist.put(c, Integer.MAX_VALUE);
        dist.put(origen, 0);

        PriorityQueue<Entrada> cola = new PriorityQueue<>(Comparator.comparingInt((Entrada en) -> en.dist));
        cola.add(new Entrada(origen, 0));

        while (!cola.isEmpty()) {
            Entrada actual = cola.poll();
            Ciudad u = actual.ciudad;
            if (actual.dist > dist.get(u)) continue;      // entrada obsoleta
            if (u == destino) break;
            for (Camino cam : caminos) {
                if (cam.a != u && cam.b != u) continue;
                Ciudad v = cam.otro(u);
                int nueva = dist.get(u) + cam.km;
                if (nueva < dist.get(v)) {
                    dist.put(v, nueva);
                    previo.put(v, u);
                    cola.add(new Entrada(v, nueva));
                }
            }
        }
        if (dist.get(destino) == Integer.MAX_VALUE) return null;

        LinkedList<Ciudad> ruta = new LinkedList<>();
        for (Ciudad c = destino; c != null; c = previo.get(c)) ruta.addFirst(c);
        distanciaTotal[0] = dist.get(destino);
        return ruta;
    }

    private void calcular() {
        limpiarRuta();
        Ciudad o = (Ciudad) cbOrigen.getSelectedItem();
        Ciudad d = (Ciudad) cbDestino.getSelectedItem();
        if (o == null || d == null) { resultado.setText(" Selecciona origen y destino."); return; }
        if (o == d) { resultado.setText(" El origen y el destino son la misma ciudad."); lienzo.repaint(); return; }

        int[] total = new int[1];
        List<Ciudad> ruta = dijkstra(o, d, total);
        if (ruta == null) {
            resultado.setText(" No existe ninguna ruta entre " + o + " y " + d + ".");
        } else {
            ciudadesRuta.addAll(ruta);
            for (int i = 0; i + 1 < ruta.size(); i++)
                for (Camino cam : caminos)
                    if (cam.une(ruta.get(i), ruta.get(i + 1))) caminosRuta.add(cam);
            StringBuilder sb = new StringBuilder(" Ruta: ");
            for (int i = 0; i < ruta.size(); i++) {
                if (i > 0) sb.append("  →  ");
                sb.append(ruta.get(i));
            }
            sb.append("   |   Distancia total: ").append(total[0]).append(" km");
            resultado.setText(sb.toString());
        }
        lienzo.repaint();
    }

    // ---------- Interfaz de dibujo ----------
    private class Lienzo extends JPanel {
        Lienzo() {
            setPreferredSize(new Dimension(900, 580));
            setBackground(new Color(245, 248, 252));
            addMouseListener(new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) { clic(e.getX(), e.getY()); }
            });
        }

        private Ciudad ciudadEn(int x, int y) {
            for (Ciudad c : ciudades)
                if (Math.hypot(c.x - x, c.y - y) <= RADIO) return c;
            return null;
        }

        private void clic(int x, int y) {
            Ciudad c = ciudadEn(x, y);
            if (modo.getSelectedIndex() == 0) {                       // agregar ciudad
                if (c != null) return;
                String nombre = JOptionPane.showInputDialog(this, "Nombre de la ciudad:");
                if (nombre == null || nombre.trim().isEmpty()) return;
                nombre = nombre.trim();
                if (buscar(nombre) != null) {
                    JOptionPane.showMessageDialog(this, "Esa ciudad ya existe.");
                    return;
                }
                agregarCiudad(nombre, x, y);
                limpiarRuta();
            } else {                                                   // conectar
                if (c == null) { seleccionada = null; repaint(); return; }
                if (seleccionada == null) {
                    seleccionada = c;
                } else if (seleccionada != c) {
                    boolean existe = false;
                    for (Camino cam : caminos) if (cam.une(seleccionada, c)) existe = true;
                    if (existe) {
                        JOptionPane.showMessageDialog(this, "Esas ciudades ya están conectadas.");
                    } else {
                        int sugerida = (int) (Math.hypot(seleccionada.x - c.x, seleccionada.y - c.y));
                        String s = JOptionPane.showInputDialog(this,
                                "Distancia en km entre " + seleccionada + " y " + c + ":", sugerida);
                        try {
                            int km = Integer.parseInt(s.trim());
                            if (km > 0) { caminos.add(new Camino(seleccionada, c, km)); limpiarRuta(); }
                        } catch (Exception ex) { /* cancelado o inválido */ }
                    }
                    seleccionada = null;
                } else {
                    seleccionada = null;
                }
            }
            repaint();
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(getFont().deriveFont(Font.BOLD, 12f));
            FontMetrics fm = g2.getFontMetrics();

            // aristas
            for (Camino cam : caminos) {
                boolean enRuta = caminosRuta.contains(cam);
                g2.setStroke(new BasicStroke(enRuta ? 5f : 2f));
                g2.setColor(enRuta ? new Color(39, 174, 96) : new Color(150, 160, 170));
                g2.drawLine(cam.a.x, cam.a.y, cam.b.x, cam.b.y);
            }
            // pesos
            g2.setStroke(new BasicStroke(1f));
            for (Camino cam : caminos) {
                String t = cam.km + " km";
                int mx = (cam.a.x + cam.b.x) / 2, my = (cam.a.y + cam.b.y) / 2;
                int w = fm.stringWidth(t) + 8;
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(mx - w / 2, my - 10, w, 20, 8, 8);
                g2.setColor(new Color(90, 90, 90));
                g2.drawRoundRect(mx - w / 2, my - 10, w, 20, 8, 8);
                g2.drawString(t, mx - w / 2 + 4, my + 5);
            }
            // vértices
            for (Ciudad c : ciudades) {
                Color relleno = new Color(52, 120, 220);
                if (ciudadesRuta.contains(c)) relleno = new Color(39, 174, 96);
                if (c == seleccionada) relleno = new Color(243, 156, 18);
                g2.setColor(relleno);
                g2.fillOval(c.x - RADIO, c.y - RADIO, RADIO * 2, RADIO * 2);
                g2.setColor(Color.DARK_GRAY);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(c.x - RADIO, c.y - RADIO, RADIO * 2, RADIO * 2);
                g2.setColor(Color.BLACK);
                g2.drawString(c.nombre, c.x - fm.stringWidth(c.nombre) / 2, c.y + RADIO + 16);
            }
        }
    }
}
