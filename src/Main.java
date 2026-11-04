import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import vista.VistaPrincipal;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("No se pudo aplicar el estilo del sistema: " + e.getMessage());
            }

            VistaPrincipal ventana = new VistaPrincipal();
            ventana.setVisible(true);
        });
    }
}
