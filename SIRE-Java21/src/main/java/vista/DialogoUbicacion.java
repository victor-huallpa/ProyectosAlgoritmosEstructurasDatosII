package vista;

import modelo.enu.TipoUbicacion;
import modelo.Ubicacion;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class DialogoUbicacion extends JDialog {
    private final JTextField campoNombre = new JTextField(18);
    private final JComboBox<TipoUbicacion> comboTipo = new JComboBox<>(TipoUbicacion.values());
    private final JSpinner campoX = new JSpinner(new SpinnerNumberModel(500, 0, 1000, 10));
    private final JSpinner campoY = new JSpinner(new SpinnerNumberModel(500, 0, 1000, 10));
    private Ubicacion resultado;

    public DialogoUbicacion(Frame owner) {
        super(owner, "Agregar ubicación", true);
        construirInterfaz();
    }

    private void construirInterfaz() {
        JPanel formulario = new JPanel(new GridLayout(4, 2, 8, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        formulario.add(new JLabel("Nombre:"));
        formulario.add(campoNombre);
        formulario.add(new JLabel("Tipo:"));
        formulario.add(comboTipo);
        formulario.add(new JLabel("Coordenada X (0–1000):"));
        formulario.add(campoX);
        formulario.add(new JLabel("Coordenada Y (0–1000):"));
        formulario.add(campoY);

        JButton aceptar = new JButton("Agregar");
        JButton cancelar = new JButton("Cancelar");
        aceptar.addActionListener(e -> guardar());
        cancelar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(cancelar);
        botones.add(aceptar);

        add(formulario, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void guardar() {
        try {
            String id = "U-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            resultado = new Ubicacion(id, campoNombre.getText(),
                    (TipoUbicacion) comboTipo.getSelectedItem(),
                    (Integer) campoX.getValue(), (Integer) campoY.getValue());
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dato inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    public Ubicacion getResultado() { return resultado; }
}
