import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * PROGRAMA 4 - PILAS
 * Problema real: editor de texto con Deshacer / Rehacer (Ctrl+Z / Ctrl+Y)
 *                y validador de paréntesis, corchetes y llaves (como en un IDE).
 *
 * Estructuras: - Pila "deshacer" y pila "rehacer" (Deque usado como Stack: push / pop)
 *              - Pila de símbolos de apertura para validar el balanceo.
 * El panel derecho muestra el contenido de ambas pilas en tiempo real.
 */
public class EditorConPilas extends JFrame {

    private final JTextArea area = new JTextArea();
    private final DefaultListModel<String> modeloDeshacer = new DefaultListModel<>();
    private final DefaultListModel<String> modeloRehacer = new DefaultListModel<>();
    private final JLabel estado = new JLabel(" Escribe algo; cada cambio se guarda en la pila de deshacer.");

    private final Deque<String> pilaDeshacer = new ArrayDeque<>();
    private final Deque<String> pilaRehacer = new ArrayDeque<>();
    private String actual = "";
    private boolean aplicando = false;

    public EditorConPilas() {
        super("Editor con Deshacer/Rehacer y validador de símbolos (Pilas)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        area.setMargin(new Insets(8, 8, 8, 8));
        area.setText("");

        JButton bDes = new JButton("Deshacer (Ctrl+Z)");
        JButton bReh = new JButton("Rehacer (Ctrl+Y)");
        JButton bVal = new JButton("Validar ( ) [ ] { }");
        JButton bEj = new JButton("Texto de ejemplo");
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        barra.add(bDes); barra.add(bReh); barra.add(bVal); barra.add(bEj);

        JList<String> listaDes = new JList<>(modeloDeshacer);
        JList<String> listaReh = new JList<>(modeloRehacer);
        JScrollPane spDes = new JScrollPane(listaDes);
        JScrollPane spReh = new JScrollPane(listaReh);
        spDes.setBorder(BorderFactory.createTitledBorder("Pila DESHACER (tope arriba)"));
        spReh.setBorder(BorderFactory.createTitledBorder("Pila REHACER (tope arriba)"));
        JPanel derecha = new JPanel(new GridLayout(2, 1));
        derecha.add(spDes); derecha.add(spReh);
        derecha.setPreferredSize(new Dimension(280, 0));

        estado.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        estado.setFont(estado.getFont().deriveFont(Font.BOLD, 13f));

        add(barra, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);
        add(derecha, BorderLayout.EAST);
        add(estado, BorderLayout.SOUTH);

        // Cada cambio del texto apila el estado anterior
        area.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { cambio(); }
            public void removeUpdate(DocumentEvent e) { cambio(); }
            public void changedUpdate(DocumentEvent e) { cambio(); }
        });

        bDes.addActionListener(e -> deshacer());
        bReh.addActionListener(e -> rehacer());
        bVal.addActionListener(e -> validar());
        bEj.addActionListener(e -> area.setText(
                "public static int suma(int[] a) {\n    int s = 0;\n    for (int i = 0; i < a.length; i++) {\n"
              + "        s += (a[i] * 2);\n    }\n    return s;\n}\n"));

        // Atajos de teclado
        area.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "deshacer");
        area.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "rehacer");
        area.getActionMap().put("deshacer", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { deshacer(); }
        });
        area.getActionMap().put("rehacer", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { rehacer(); }
        });

        setSize(950, 560);
        setLocationRelativeTo(null);
    }

    // ---------- Deshacer / Rehacer ----------
    private void cambio() {
        if (aplicando) return;
        pilaDeshacer.push(actual);          // push del estado anterior
        pilaRehacer.clear();                // una edición nueva invalida el "rehacer"
        actual = area.getText();
        refrescarPilas();
    }

    private void deshacer() {
        if (pilaDeshacer.isEmpty()) { estado.setText(" No hay nada que deshacer."); return; }
        aplicando = true;
        pilaRehacer.push(actual);
        actual = pilaDeshacer.pop();        // pop
        area.setText(actual);
        aplicando = false;
        estado.setText(" Deshacer aplicado.");
        refrescarPilas();
    }

    private void rehacer() {
        if (pilaRehacer.isEmpty()) { estado.setText(" No hay nada que rehacer."); return; }
        aplicando = true;
        pilaDeshacer.push(actual);
        actual = pilaRehacer.pop();
        area.setText(actual);
        aplicando = false;
        estado.setText(" Rehacer aplicado.");
        refrescarPilas();
    }

    private String resumen(String s) {
        String t = s.replace("\n", "↵ ");
        t = t.length() > 28 ? t.substring(0, 28) + "…" : t;
        return "\"" + t + "\"  (" + s.length() + " car.)";
    }

    private void refrescarPilas() {
        modeloDeshacer.clear();
        modeloRehacer.clear();
        int n = 0;
        for (String s : pilaDeshacer) { if (n++ > 60) break; modeloDeshacer.addElement(resumen(s)); }   // el iterador empieza por el tope
        n = 0;
        for (String s : pilaRehacer) { if (n++ > 60) break; modeloRehacer.addElement(resumen(s)); }
    }

    // ---------- Validación de símbolos con una pila ----------
    private void validar() {
        String texto = area.getText();
        Deque<int[]> pila = new ArrayDeque<>();      // {símbolo, posición}
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == '(' || c == '[' || c == '{') {
                pila.push(new int[]{c, i});
            } else if (c == ')' || c == ']' || c == '}') {
                if (pila.isEmpty()) {
                    error(i, "Sobra el cierre '" + c + "' en la posición " + i);
                    return;
                }
                int[] tope = pila.pop();
                if (!coinciden((char) tope[0], c)) {
                    error(i, "Se esperaba '" + cierre((char) tope[0]) + "' pero se encontró '" + c
                            + "' (posición " + i + ")");
                    return;
                }
            }
        }
        if (!pila.isEmpty()) {
            int[] tope = pila.peek();
            error(tope[1], "Falta cerrar '" + (char) tope[0] + "' abierto en la posición " + tope[1]);
            return;
        }
        estado.setForeground(new Color(30, 130, 76));
        estado.setText(" ✔ Todos los símbolos están balanceados.");
    }

    private boolean coinciden(char a, char c) { return cierre(a) == c; }

    private char cierre(char a) { return a == '(' ? ')' : a == '[' ? ']' : '}'; }

    private void error(int pos, String mensaje) {
        estado.setForeground(new Color(192, 57, 43));
        estado.setText(" ✘ " + mensaje);
        area.requestFocusInWindow();
        area.select(pos, pos + 1);
    }
}
