import servicio.DatosIniciales;
import vista.FrmPrincipal;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception exception) {
                System.err.println("No se pudo aplicar el estilo del sistema: " + exception.getMessage());
            }

            var escenario = DatosIniciales.crearEscenario();
            new FrmPrincipal(escenario.grafo(), escenario.unidades()).setVisible(true);
        });
    }
}
