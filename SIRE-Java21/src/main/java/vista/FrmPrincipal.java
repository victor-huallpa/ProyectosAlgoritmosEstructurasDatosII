package vista;

import modelo.*;
import servicio.Dijkstra;
import servicio.ResultadoRuta;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class FrmPrincipal extends JFrame {
    private final Grafo grafo;
    private final List<UnidadEmergencia> unidades;
    private final PanelGrafo panelGrafo;
    private final DefaultListModel<String> modeloUbicaciones = new DefaultListModel<>();
    private final DefaultListModel<String> modeloTramos = new DefaultListModel<>();
    private final DefaultListModel<String> modeloUnidades = new DefaultListModel<>();
    private final JLabel etiquetaResumen = new JLabel();
    private PanelEmergencias panelEmergencias;

    public FrmPrincipal(Grafo grafo, List<UnidadEmergencia> unidades) {
        this.grafo = grafo;
        this.unidades = unidades;
        this.panelGrafo = new PanelGrafo(grafo);
        configurarVentana();
        construirInterfaz();
        actualizarPaneles();
    }

    private void configurarVentana() {
        setTitle("SIRE | Sistema Inteligente de Respuesta ante Emergencias");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 760));
        setSize(1450, 900);
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel raiz = new JPanel(new BorderLayout(12, 12));
        raiz.setBackground(Color.BLACK);
        raiz.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(raiz);

        raiz.add(crearEncabezado(), BorderLayout.NORTH);
        JTabbedPane pestañas = new JTabbedPane();
        JPanel panelRed = new JPanel(new BorderLayout(10, 10));
        panelRed.add(panelGrafo, BorderLayout.CENTER);
        panelRed.add(crearPanelLateral(), BorderLayout.EAST);
        panelRed.setBackground(Color.BLACK);
        panelEmergencias = new PanelEmergencias(grafo, unidades, ruta -> {
            panelGrafo.mostrarRuta(ruta);
            actualizarPaneles();
        });
        pestañas.addTab("Mapa de la ciudad", panelRed);
        pestañas.addTab("Centro de emergencias", panelEmergencias);
        raiz.add(pestañas, BorderLayout.CENTER);
        raiz.add(crearBarraInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel titulos = new JPanel();
        titulos.setOpaque(false);
        titulos.setLayout(new BoxLayout(titulos, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("SIRE");
        titulo.setForeground(new Color(241, 245, 249));
        titulo.setFont(new Font("SansSerif", Font.BOLD, 27));

        JLabel subtitulo = new JLabel("Centro de operaciones · Red vial editable");
        subtitulo.setForeground(new Color(157, 172, 193));
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        titulos.add(titulo);
        titulos.add(subtitulo);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        acciones.setOpaque(false);
        JButton agregarUbicacion = crearBoton("+ Ubicación");
        agregarUbicacion.setForeground(Color.black);
        JButton agregarTramo = crearBoton("+ Vía");
        agregarTramo.setForeground(Color.black);
        JButton calcularRuta = crearBoton("Calcular ruta");
        calcularRuta.setForeground(Color.black);
        JButton bloquearVia = crearBoton("Bloquear / habilitar vía");
        bloquearVia.setForeground(Color.black);
        agregarUbicacion.addActionListener(e -> agregarUbicacion());
        agregarTramo.addActionListener(e -> agregarTramo());
        calcularRuta.addActionListener(e -> calcularRuta());
        bloquearVia.addActionListener(e -> cambiarEstadoVia());
        acciones.add(agregarUbicacion);
        acciones.add(agregarTramo);
        acciones.add(calcularRuta);
        acciones.add(bloquearVia);

        panel.add(titulos, BorderLayout.WEST);
        panel.add(acciones, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearPanelLateral() {
        JPanel lateral = new JPanel();
        lateral.setPreferredSize(new Dimension(310, 10));
        lateral.setBackground(new Color(17, 12, 0));
        lateral.setBorder(new EmptyBorder(12, 12, 12, 12));
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));

        lateral.add(tituloSeccion("Inventario de la red"));
        lateral.add(etiquetaResumen);
        lateral.add(Box.createVerticalStrut(10));

        lateral.add(tituloSeccion("Ubicaciones"));
        lateral.add(crearLista(modeloUbicaciones, 8));
        lateral.add(Box.createVerticalStrut(10));

        lateral.add(tituloSeccion("Vías registradas"));
        lateral.add(crearLista(modeloTramos, 7));
        lateral.add(Box.createVerticalStrut(10));

        lateral.add(tituloSeccion("Unidades precargadas"));
        lateral.add(crearLista(modeloUnidades, 6));

        JLabel nota = new JLabel("<html><br>Los datos del escenario son ficticios y editables.</html>");
        nota.setForeground(new Color(150, 165, 185));
        nota.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lateral.add(nota);
        return lateral;
    }

    private JLabel tituloSeccion(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(new Color(226, 232, 240));
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setBorder(new EmptyBorder(4, 0, 6, 0));
        return label;
    }

    private JScrollPane crearLista(DefaultListModel<String> modelo, int filas) {
        JList<String> lista = new JList<>(modelo);
        lista.setVisibleRowCount(filas);
        lista.setBackground(new Color(15, 23, 36));
        lista.setForeground(new Color(211, 220, 232));
        lista.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lista.setSelectionBackground(Color.gray);
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return scroll;
    }

    private JPanel crearBarraInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel leyenda = new JLabel("  ● Intersección     ● Hospital     ● Bomberos     ● Policía");
        leyenda.setForeground(new Color(170, 184, 203));
        leyenda.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JLabel fase = new JLabel("FASE 4 · GESTIÓN Y SEGUIMIENTO DE EMERGENCIAS");
        fase.setForeground(new Color(113, 190, 176));
        fase.setFont(new Font("SansSerif", Font.BOLD, 11));
        panel.add(leyenda, BorderLayout.WEST);
        panel.add(fase, BorderLayout.EAST);
        return panel;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(new Color(40, 111, 130));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("SansSerif", Font.BOLD, 12));
        boton.setBorder(new EmptyBorder(9, 14, 9, 14));
        return boton;
    }

    private void agregarUbicacion() {
        DialogoUbicacion dialogo = new DialogoUbicacion(this);
        dialogo.setVisible(true);
        Ubicacion nueva = dialogo.getResultado();
        if (nueva == null) return;

        try {
            grafo.agregarUbicacion(nueva);
            actualizarPaneles();
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void agregarTramo() {
        if (grafo.cantidadUbicaciones() < 2) {
            mostrarError("Primero debes registrar al menos dos ubicaciones.");
            return;
        }

        List<Ubicacion> ubicaciones = grafo.getUbicaciones().stream().toList();
        JComboBox<Ubicacion> origen = new JComboBox<>(ubicaciones.toArray(Ubicacion[]::new));
        JComboBox<Ubicacion> destino = new JComboBox<>(ubicaciones.toArray(Ubicacion[]::new));
        JSpinner distancia = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 1000.0, 0.1));

        JPanel formulario = new JPanel(new GridLayout(3, 2, 8, 8));
        formulario.add(new JLabel("Origen:"));
        formulario.add(origen);
        formulario.add(new JLabel("Destino:"));
        formulario.add(destino);
        formulario.add(new JLabel("Distancia (km):"));
        formulario.add(distancia);

        int opcion = JOptionPane.showConfirmDialog(this, formulario, "Registrar vía",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;

        try {
            Ubicacion a = (Ubicacion) origen.getSelectedItem();
            Ubicacion b = (Ubicacion) destino.getSelectedItem();
            if (a == null || b == null) throw new IllegalArgumentException("Selecciona origen y destino.");
            grafo.agregarTramo(a.getId(), b.getId(), ((Number) distancia.getValue()).doubleValue());
            actualizarPaneles();
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void calcularRuta() {
        if (grafo.cantidadUbicaciones() < 2) {
            mostrarError("Registra al menos dos ubicaciones para calcular una ruta.");
            return;
        }
        List<Ubicacion> ubicaciones = grafo.getUbicaciones().stream().toList();
        JComboBox<Ubicacion> origen = new JComboBox<>(ubicaciones.toArray(Ubicacion[]::new));
        JComboBox<Ubicacion> destino = new JComboBox<>(ubicaciones.toArray(Ubicacion[]::new));
        if (ubicaciones.size() > 1) destino.setSelectedIndex(1);
        JPanel formulario = new JPanel(new GridLayout(2, 2, 8, 8));
        formulario.add(new JLabel("Punto de origen:")); formulario.add(origen);
        formulario.add(new JLabel("Punto de destino:")); formulario.add(destino);
        int opcion = JOptionPane.showConfirmDialog(this, formulario, "Calcular ruta más corta",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;
        Ubicacion a = (Ubicacion) origen.getSelectedItem();
        Ubicacion b = (Ubicacion) destino.getSelectedItem();
        if (a == null || b == null) return;
        try {
            var resultado = new Dijkstra().calcular(grafo, a.getId(), b.getId());
            if (resultado.isEmpty()) {
                panelGrafo.limpiarRuta();
                JOptionPane.showMessageDialog(this,
                        "No existe una ruta disponible entre esos puntos. Revisa las vías bloqueadas.",
                        "Ruta no encontrada", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ResultadoRuta ruta = resultado.get();
            panelGrafo.mostrarRuta(ruta.ubicaciones());
            String nombres = ruta.ubicaciones().stream()
                    .map(id -> grafo.obtenerUbicacion(id).getNombre())
                    .collect(java.util.stream.Collectors.joining(" → "));
            JOptionPane.showMessageDialog(this,
                    "Ruta de menor distancia:\n" + nombres + "\n\nDistancia total: "
                            + String.format(java.util.Locale.US, "%.2f km", ruta.distanciaTotalKm()),
                    "Resultado de Dijkstra", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void cambiarEstadoVia() {
        if (grafo.getTramos().isEmpty()) {
            mostrarError("No hay vías registradas para modificar.");
            return;
        }
        JComboBox<Tramo> selector = new JComboBox<>(grafo.getTramos().toArray(Tramo[]::new));
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.add(new JLabel("Selecciona la vía que deseas bloquear o habilitar:"), BorderLayout.NORTH);
        panel.add(selector, BorderLayout.CENTER);
        int opcion = JOptionPane.showConfirmDialog(this, panel, "Estado de las vías",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;
        Tramo tramo = (Tramo) selector.getSelectedItem();
        if (tramo == null) return;
        tramo.setBloqueado(!tramo.isBloqueado());
        panelGrafo.limpiarRuta();
        actualizarPaneles();
        JOptionPane.showMessageDialog(this,
                tramo.isBloqueado() ? "La vía quedó BLOQUEADA." : "La vía quedó HABILITADA.",
                "Estado actualizado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarPaneles() {
        if (panelEmergencias != null) panelEmergencias.actualizarUbicaciones();
        modeloUbicaciones.clear();
        for (Ubicacion u : grafo.getUbicaciones()) {
            modeloUbicaciones.addElement(u.getNombre() + " · " + u.getTipo());
        }

        modeloTramos.clear();
        for (Tramo t : grafo.getTramos()) {
            Ubicacion a = grafo.obtenerUbicacion(t.getOrigenId());
            Ubicacion b = grafo.obtenerUbicacion(t.getDestinoId());
            modeloTramos.addElement(a.getNombre() + " ↔ " + b.getNombre()
                    + " · " + String.format(java.util.Locale.US, "%.1f km", t.getDistanciaKm()));
        }

        modeloUnidades.clear();
        for (UnidadEmergencia unidad : unidades) {
            Ubicacion ubicacion = grafo.obtenerUbicacion(unidad.getUbicacionId());
            String lugar = ubicacion == null ? "Ubicación no disponible" : ubicacion.getNombre();
            modeloUnidades.addElement(unidad + " · " + lugar);
        }

        etiquetaResumen.setForeground(new Color(145, 190, 205));
        etiquetaResumen.setFont(new Font("SansSerif", Font.PLAIN, 12));
        etiquetaResumen.setText("<html>" + grafo.cantidadUbicaciones() + " nodos · "
                + grafo.cantidadTramos() + " vías · " + unidades.size() + " unidades</html>");
        panelGrafo.repaint();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "No se pudo completar la operación",
                JOptionPane.WARNING_MESSAGE);
    }
}
