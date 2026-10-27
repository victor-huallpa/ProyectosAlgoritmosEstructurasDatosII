import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * PROGRAMA 3 - COLAS
 * Problema real: simulación de la atención al público en un banco.
 *
 * Estructuras: dos colas FIFO (Queue<Cliente>): una preferencial y una normal.
 *              Cuando una ventanilla se libera atiende primero la cola preferencial.
 * Se calcula el tiempo de espera promedio, la longitud máxima de cola y los clientes atendidos.
 * Cada "tick" del reloj equivale a 1 minuto simulado.
 */
public class SimuladorBanco extends JFrame {

    static class Cliente {
        final int id;
        final boolean preferencial;
        final int llegada;      // minuto de llegada
        final int servicio;     // minutos que tardará su atención
        Cliente(int id, boolean pref, int llegada, int servicio) {
            this.id = id; this.preferencial = pref; this.llegada = llegada; this.servicio = servicio;
        }
    }

    static class Ventanilla {
        Cliente actual;
        int restante;
        int atendidos;
        boolean activa;
    }

    private static final int MAX_VENTANILLAS = 6;

    private final Queue<Cliente> colaNormal = new LinkedList<>();
    private final Queue<Cliente> colaPref = new LinkedList<>();
    private final Ventanilla[] ventanillas = new Ventanilla[MAX_VENTANILLAS];
    private final Random rnd = new Random();

    private int reloj, siguienteId, atendidos, maxCola;
    private long totalEspera;

    private final JButton btnIniciar = new JButton("Iniciar");
    private final JSpinner spVentanillas = new JSpinner(new SpinnerNumberModel(2, 1, MAX_VENTANILLAS, 1));
    private final JSlider slLlegada = new JSlider(0, 100, 35);
    private final JLabel estadisticas = new JLabel(" ");
    private final Timer timer;
    private final Lienzo lienzo = new Lienzo();

    public SimuladorBanco() {
        super("Simulador de atención en un banco (Colas)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        for (int i = 0; i < MAX_VENTANILLAS; i++) ventanillas[i] = new Ventanilla();

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        JButton btnNormal = new JButton("+ Cliente normal");
        JButton btnPref = new JButton("+ Cliente preferencial");
        JButton btnReiniciar = new JButton("Reiniciar");
        slLlegada.setPreferredSize(new Dimension(140, 40));
        slLlegada.setMajorTickSpacing(25);
        slLlegada.setPaintTicks(true);
        slLlegada.setPaintLabels(true);
        controles.add(btnIniciar);
        controles.add(btnNormal);
        controles.add(btnPref);
        controles.add(new JLabel("Ventanillas:"));
        controles.add(spVentanillas);
        controles.add(new JLabel("Llegadas (%/min):"));
        controles.add(slLlegada);
        controles.add(btnReiniciar);

        estadisticas.setFont(estadisticas.getFont().deriveFont(Font.BOLD, 13f));
        estadisticas.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        add(controles, BorderLayout.NORTH);
        add(lienzo, BorderLayout.CENTER);
        add(estadisticas, BorderLayout.SOUTH);

        timer = new Timer(450, e -> tick());

        btnIniciar.addActionListener(e -> {
            if (timer.isRunning()) { timer.stop(); btnIniciar.setText("Continuar"); }
            else { timer.start(); btnIniciar.setText("Pausar"); }
        });
        btnNormal.addActionListener(e -> { nuevoCliente(false); refrescar(); });
        btnPref.addActionListener(e -> { nuevoCliente(true); refrescar(); });
        btnReiniciar.addActionListener(e -> reiniciar());
        spVentanillas.addChangeListener(e -> { aplicarVentanillas(); asignarVentanillas(); refrescar(); });

        reiniciar();
        pack();
        setLocationRelativeTo(null);
    }

    private void reiniciar() {
        timer.stop();
        btnIniciar.setText("Iniciar");
        colaNormal.clear(); colaPref.clear();
        for (Ventanilla v : ventanillas) { v.actual = null; v.restante = 0; v.atendidos = 0; }
        reloj = 0; siguienteId = 1; atendidos = 0; maxCola = 0; totalEspera = 0;
        aplicarVentanillas();
        refrescar();
    }

    private void aplicarVentanillas() {
        int n = (Integer) spVentanillas.getValue();
        for (int i = 0; i < MAX_VENTANILLAS; i++) ventanillas[i].activa = i < n;
    }

    private void nuevoCliente(boolean pref) {
        Cliente c = new Cliente(siguienteId++, pref, reloj, 3 + rnd.nextInt(6));
        if (pref) colaPref.add(c); else colaNormal.add(c);      // enqueue
        maxCola = Math.max(maxCola, colaPref.size() + colaNormal.size());
    }

    private void asignarVentanillas() {
        for (Ventanilla v : ventanillas) {
            if (!v.activa || v.actual != null) continue;
            Cliente c = !colaPref.isEmpty() ? colaPref.poll() : colaNormal.poll();   // dequeue
            if (c == null) return;
            totalEspera += reloj - c.llegada;
            v.actual = c;
            v.restante = c.servicio;
        }
    }

    private void tick() {
        reloj++;
        for (Ventanilla v : ventanillas) {                       // avanzar atenciones
            if (v.actual == null) continue;
            v.restante--;
            if (v.restante <= 0) { v.actual = null; v.atendidos++; atendidos++; }
        }
        if (rnd.nextInt(100) < slLlegada.getValue())             // llegada aleatoria
            nuevoCliente(rnd.nextInt(100) < 20);
        asignarVentanillas();
        refrescar();
    }

    private void refrescar() {
        int enAtencion = 0;
        for (Ventanilla v : ventanillas) if (v.actual != null) enAtencion++;
        int iniciados = atendidos + enAtencion;
        double prom = iniciados == 0 ? 0 : (double) totalEspera / iniciados;
        estadisticas.setText(String.format(
                "Minuto: %d   |   En cola: %d (pref: %d)   |   Atendidos: %d   |   Espera promedio: %.1f min   |   Cola máxima: %d",
                reloj, colaNormal.size() + colaPref.size(), colaPref.size(), atendidos, prom, maxCola));
        lienzo.repaint();
    }

    // ---------- Dibujo ----------
    private class Lienzo extends JPanel {
        Lienzo() {
            setPreferredSize(new Dimension(900, 400));
            setBackground(new Color(250, 250, 245));
        }

        private void dibujarCola(Graphics2D g2, Queue<Cliente> cola, int y, String titulo, Color color) {
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(getFont().deriveFont(Font.BOLD, 13f));
            g2.drawString(titulo + " (" + cola.size() + ")   ← frente de la cola", 30, y - 12);
            int i = 0;
            for (Cliente c : cola) {
                if (i >= 20) {
                    g2.setColor(Color.DARK_GRAY);
                    g2.drawString("+" + (cola.size() - 20), 30 + i * 40, y + 22);
                    break;
                }
                g2.setColor(color);
                g2.fillOval(30 + i * 40, y, 34, 34);
                g2.setColor(Color.DARK_GRAY);
                g2.drawOval(30 + i * 40, y, 34, 34);
                g2.setFont(getFont().deriveFont(Font.BOLD, 11f));
                String t = String.valueOf(c.id);
                g2.drawString(t, 30 + i * 40 + 17 - g2.getFontMetrics().stringWidth(t) / 2, y + 22);
                i++;
            }
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (int i = 0; i < MAX_VENTANILLAS; i++) {
                Ventanilla v = ventanillas[i];
                int x = 30 + i * 140, y = 25;
                g2.setColor(v.activa ? new Color(220, 237, 250) : new Color(225, 225, 225));
                g2.fillRoundRect(x, y, 125, 110, 16, 16);
                g2.setColor(Color.DARK_GRAY);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(x, y, 125, 110, 16, 16);
                g2.setFont(getFont().deriveFont(Font.BOLD, 12f));
                g2.drawString(v.activa ? "Ventanilla " + (i + 1) : "Cerrada", x + 10, y + 20);
                g2.setFont(getFont().deriveFont(11f));
                g2.drawString("Atendidos: " + v.atendidos, x + 10, y + 102);
                if (v.actual != null) {
                    g2.setColor(v.actual.preferencial ? new Color(241, 196, 15) : new Color(93, 173, 226));
                    g2.fillOval(x + 10, y + 32, 40, 40);
                    g2.setColor(Color.DARK_GRAY);
                    g2.drawOval(x + 10, y + 32, 40, 40);
                    g2.setFont(getFont().deriveFont(Font.BOLD, 12f));
                    String t = "#" + v.actual.id;
                    g2.drawString(t, x + 30 - g2.getFontMetrics().stringWidth(t) / 2, y + 57);
                    // barra de progreso de la atención
                    double p = 1.0 - (double) v.restante / v.actual.servicio;
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fillRect(x + 58, y + 46, 54, 12);
                    g2.setColor(new Color(39, 174, 96));
                    g2.fillRect(x + 58, y + 46, (int) (54 * p), 12);
                    g2.setColor(Color.DARK_GRAY);
                    g2.drawRect(x + 58, y + 46, 54, 12);
                }
            }
            dibujarCola(g2, colaPref, 200, "Cola preferencial", new Color(241, 196, 15));
            dibujarCola(g2, colaNormal, 300, "Cola normal", new Color(93, 173, 226));
        }
    }
}
