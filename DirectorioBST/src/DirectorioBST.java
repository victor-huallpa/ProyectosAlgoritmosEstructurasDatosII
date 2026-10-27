import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * PROGRAMA 2 - ÁRBOLES
 * Problema real: agenda / directorio telefónico con búsqueda rápida y ordenada.
 *
 * Estructura: Árbol Binario de Búsqueda (ordenado por nombre)
 * Operaciones: insertar, buscar (muestra el camino recorrido), eliminar (3 casos),
 *              recorridos inorden / preorden / postorden, altura y cantidad de nodos.
 */
public class DirectorioBST extends JFrame {

    // ---------- Árbol Binario de Búsqueda ----------
    static class Nodo {
        String nombre, telefono;
        Nodo izq, der;
        Nodo(String n, String t) { nombre = n; telefono = t; }
    }

    private Nodo raiz;
    private int total = 0;
    private boolean eliminado;

    private Nodo insertar(Nodo r, String nombre, String tel) {
        if (r == null) { total++; return new Nodo(nombre, tel); }
        int c = nombre.compareToIgnoreCase(r.nombre);
        if (c < 0) r.izq = insertar(r.izq, nombre, tel);
        else if (c > 0) r.der = insertar(r.der, nombre, tel);
        else r.telefono = tel;                       // mismo nombre: actualiza el teléfono
        return r;
    }

    private Nodo eliminar(Nodo r, String nombre) {
        if (r == null) return null;
        int c = nombre.compareToIgnoreCase(r.nombre);
        if (c < 0) r.izq = eliminar(r.izq, nombre);
        else if (c > 0) r.der = eliminar(r.der, nombre);
        else {
            eliminado = true;
            if (r.izq == null) return r.der;         // caso 1 y 2: sin hijo o un hijo
            if (r.der == null) return r.izq;
            Nodo suc = r.der;                        // caso 3: dos hijos -> sucesor inorden
            while (suc.izq != null) suc = suc.izq;
            r.nombre = suc.nombre;
            r.telefono = suc.telefono;
            r.der = eliminar(r.der, suc.nombre);
        }
        return r;
    }

    private int altura(Nodo r) {
        return r == null ? 0 : 1 + Math.max(altura(r.izq), altura(r.der));
    }

    private void inorden(Nodo r, List<String> out) {
        if (r == null) return;
        inorden(r.izq, out); out.add(r.nombre); inorden(r.der, out);
    }

    private void preorden(Nodo r, List<String> out) {
        if (r == null) return;
        out.add(r.nombre); preorden(r.izq, out); preorden(r.der, out);
    }

    private void postorden(Nodo r, List<String> out) {
        if (r == null) return;
        postorden(r.izq, out); postorden(r.der, out); out.add(r.nombre);
    }

    // ---------- Interfaz ----------
    private final JTextField tfNombre = new JTextField(12);
    private final JTextField tfTelefono = new JTextField(10);
    private final JLabel estado = new JLabel(" ");
    private final JLabel recorrido = new JLabel(" ");
    private final Lienzo lienzo = new Lienzo();

    private final Set<Nodo> camino = new HashSet<>();
    private Nodo encontrado;

    public DirectorioBST() {
        super("Directorio de contactos (Árbol Binario de Búsqueda)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel arriba = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        arriba.add(new JLabel("Nombre:"));
        arriba.add(tfNombre);
        arriba.add(new JLabel("Teléfono:"));
        arriba.add(tfTelefono);
        JButton bIns = new JButton("Insertar");
        JButton bBus = new JButton("Buscar");
        JButton bEli = new JButton("Eliminar");
        JButton bEj = new JButton("Cargar ejemplo");
        JButton bLim = new JButton("Vaciar");
        arriba.add(bIns); arriba.add(bBus); arriba.add(bEli); arriba.add(bEj); arriba.add(bLim);

        JPanel abajo = new JPanel(new GridLayout(3, 1));
        JPanel botonesRec = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JButton bIn = new JButton("Inorden (A-Z)");
        JButton bPre = new JButton("Preorden");
        JButton bPost = new JButton("Postorden");
        botonesRec.add(new JLabel("Recorridos:"));
        botonesRec.add(bIn); botonesRec.add(bPre); botonesRec.add(bPost);
        estado.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        recorrido.setBorder(BorderFactory.createEmptyBorder(0, 10, 4, 10));
        abajo.add(botonesRec); abajo.add(recorrido); abajo.add(estado);

        add(arriba, BorderLayout.NORTH);
        add(new JScrollPane(lienzo), BorderLayout.CENTER);
        add(abajo, BorderLayout.SOUTH);

        bIns.addActionListener(e -> accionInsertar());
        bBus.addActionListener(e -> accionBuscar());
        bEli.addActionListener(e -> accionEliminar());
        bEj.addActionListener(e -> cargarEjemplo());
        bLim.addActionListener(e -> { raiz = null; total = 0; limpiarMarcas(); actualizarInfo("Árbol vacío."); });
        bIn.addActionListener(e -> mostrarRecorrido("Inorden", 0));
        bPre.addActionListener(e -> mostrarRecorrido("Preorden", 1));
        bPost.addActionListener(e -> mostrarRecorrido("Postorden", 2));
        tfNombre.addActionListener(e -> accionInsertar());

        setSize(1000, 600);
        setLocationRelativeTo(null);
        cargarEjemplo();
    }

    private void limpiarMarcas() { camino.clear(); encontrado = null; }

    private void actualizarInfo(String mensaje) {
        estado.setText(" " + mensaje + "   |   Contactos: " + total + "   |   Altura del árbol: " + altura(raiz));
        lienzo.refrescar();
    }

    private void accionInsertar() {
        String n = tfNombre.getText().trim(), t = tfTelefono.getText().trim();
        if (n.isEmpty()) { estado.setText(" Escribe un nombre."); return; }
        if (t.isEmpty()) t = "---";
        limpiarMarcas();
        raiz = insertar(raiz, n, t);
        actualizarInfo("Insertado: " + n);
        tfNombre.setText(""); tfTelefono.setText(""); tfNombre.requestFocus();
    }

    private void accionBuscar() {
        String n = tfNombre.getText().trim();
        if (n.isEmpty()) { estado.setText(" Escribe el nombre a buscar."); return; }
        limpiarMarcas();
        Nodo actual = raiz;
        int comparaciones = 0;
        while (actual != null) {
            camino.add(actual);
            comparaciones++;
            int c = n.compareToIgnoreCase(actual.nombre);
            if (c == 0) { encontrado = actual; break; }
            actual = c < 0 ? actual.izq : actual.der;
        }
        if (encontrado != null)
            actualizarInfo("Encontrado: " + encontrado.nombre + " (" + encontrado.telefono + ") en "
                    + comparaciones + " comparaciones");
        else
            actualizarInfo("\"" + n + "\" no existe (" + comparaciones + " comparaciones)");
    }

    private void accionEliminar() {
        String n = tfNombre.getText().trim();
        if (n.isEmpty()) { estado.setText(" Escribe el nombre a eliminar."); return; }
        limpiarMarcas();
        eliminado = false;
        raiz = eliminar(raiz, n);
        if (eliminado) total--;
        actualizarInfo(eliminado ? "Eliminado: " + n : "\"" + n + "\" no existe");
        tfNombre.setText("");
    }

    private void mostrarRecorrido(String titulo, int tipo) {
        List<String> out = new ArrayList<>();
        if (tipo == 0) inorden(raiz, out);
        else if (tipo == 1) preorden(raiz, out);
        else postorden(raiz, out);
        recorrido.setText(" " + titulo + ":  " + (out.isEmpty() ? "(vacío)" : String.join("  →  ", out)));
    }

    private void cargarEjemplo() {
        raiz = null; total = 0; limpiarMarcas();
        String[][] datos = {
            {"Marta", "951000001"}, {"Carlos", "951000002"}, {"Zoila", "951000003"},
            {"Ana", "951000004"}, {"Luis", "951000005"}, {"Rosa", "951000006"},
            {"Wilber", "951000007"}, {"Elena", "951000008"}, {"Pedro", "951000009"},
            {"Sonia", "951000010"}, {"Julia", "951000011"}
        };
        for (String[] d : datos) raiz = insertar(raiz, d[0], d[1]);
        actualizarInfo("Ejemplo cargado");
    }

    // ---------- Dibujo del árbol ----------
    private class Lienzo extends JPanel {
        private static final int MARGEN = 50, GAP_X = 95, GAP_Y = 80, ANCHO = 86, ALTO = 42;
        private final Map<Nodo, Point> pos = new HashMap<>();
        private int contador;

        Lienzo() { setBackground(Color.WHITE); }

        void refrescar() {
            pos.clear();
            contador = 0;
            asignar(raiz, 0);
            setPreferredSize(new Dimension(MARGEN * 2 + Math.max(1, total) * GAP_X,
                    MARGEN * 2 + Math.max(1, altura(raiz)) * GAP_Y));
            revalidate();
            repaint();
        }

        // x = orden inorden, y = profundidad (así ningún nodo se superpone)
        private void asignar(Nodo n, int prof) {
            if (n == null) return;
            asignar(n.izq, prof + 1);
            pos.put(n, new Point(MARGEN + contador * GAP_X, MARGEN + prof * GAP_Y));
            contador++;
            asignar(n.der, prof + 1);
        }

        private void dibujarLineas(Graphics2D g2, Nodo n) {
            if (n == null) return;
            Point p = pos.get(n);
            for (Nodo h : new Nodo[]{n.izq, n.der}) {
                if (h == null) continue;
                Point q = pos.get(h);
                boolean enCamino = camino.contains(n) && camino.contains(h);
                g2.setStroke(new BasicStroke(enCamino ? 3f : 1.5f));
                g2.setColor(enCamino ? new Color(230, 126, 34) : Color.GRAY);
                g2.drawLine(p.x, p.y, q.x, q.y);
                dibujarLineas(g2, h);
            }
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            dibujarLineas(g2, raiz);

            for (Map.Entry<Nodo, Point> en : pos.entrySet()) {
                Nodo n = en.getKey();
                Point p = en.getValue();
                Color fondo = new Color(214, 234, 248);
                if (camino.contains(n)) fondo = new Color(250, 215, 160);
                if (n == encontrado) fondo = new Color(171, 235, 198);
                g2.setColor(fondo);
                g2.fillRoundRect(p.x - ANCHO / 2, p.y - ALTO / 2, ANCHO, ALTO, 14, 14);
                g2.setColor(Color.DARK_GRAY);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(p.x - ANCHO / 2, p.y - ALTO / 2, ANCHO, ALTO, 14, 14);

                g2.setFont(getFont().deriveFont(Font.BOLD, 12f));
                String nom = n.nombre.length() > 11 ? n.nombre.substring(0, 10) + "…" : n.nombre;
                FontMetrics fm = g2.getFontMetrics();
                g2.setColor(Color.BLACK);
                g2.drawString(nom, p.x - fm.stringWidth(nom) / 2, p.y - 3);
                g2.setFont(getFont().deriveFont(10f));
                fm = g2.getFontMetrics();
                g2.setColor(new Color(70, 70, 70));
                g2.drawString(n.telefono, p.x - fm.stringWidth(n.telefono) / 2, p.y + 12);
            }
        }
    }
}
