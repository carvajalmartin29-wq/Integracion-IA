package vista;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;

public class ContenedorChat extends JPanel implements Scrollable {

    private int ultimoAncho = -1;

    public ContenedorChat(Color fondo) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(fondo);
        setBorder(BorderFactory.createEmptyBorder(14, 0, 14, 0));

        // Al cambiar el ancho de la ventana, las burbujas recalculan su tamaño
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (getWidth() != ultimoAncho) {
                    ultimoAncho = getWidth();
                    for (Component c : getComponents()) {
                        c.invalidate();
                    }
                    revalidate();
                    repaint();
                }
            }
        });
    }

    public void agregarMensaje(String mensaje, boolean esUsuario,
                               Color fondoBurbuja, Color colorTexto) {

        BurbujaChat burbuja = new BurbujaChat(mensaje, fondoBurbuja, colorTexto);

        JPanel fila = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
        };
        fila.setOpaque(false);
        fila.setBorder(BorderFactory.createEmptyBorder(6, 22, 6, 22));
        fila.add(burbuja, esUsuario ? BorderLayout.EAST : BorderLayout.WEST);

        add(fila);
        revalidate();
        repaint();

        SwingUtilities.invokeLater(() -> desplazarA(fila));
    }

    public void limpiar() {
        removeAll();
        revalidate();
        repaint();
    }

    private void desplazarA(JPanel fila) {
        JViewport vista = (JViewport)
                SwingUtilities.getAncestorOfClass(JViewport.class, this);

        if (vista == null) {
            return;
        }

        // Si el mensaje cabe en pantalla, se baja hasta el final;
        // si es muy largo, se muestra desde su inicio para poder leerlo.
        int destino = Math.min(
                fila.getY() + fila.getHeight() - vista.getHeight(),
                fila.getY()
        );

        vista.setViewPosition(new Point(0, Math.max(0, destino)));
    }

    // ---- Scrollable: el contenido sigue el ancho de la ventana ----

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visible, int orientation, int direction) {
        return 20;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visible, int orientation, int direction) {
        return Math.max(visible.height - 40, 20);
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}