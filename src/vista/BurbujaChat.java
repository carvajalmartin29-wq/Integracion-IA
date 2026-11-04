package vista;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

public class BurbujaChat extends JPanel {

    private static final int RADIO = 24;
    private static final double PROPORCION_MAXIMA = 0.72;

    private final JTextArea texto;
    private final Color fondo;

    public BurbujaChat(String mensaje, Color fondo, Color colorTexto) {
        super(new BorderLayout());
        this.fondo = fondo;

        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        texto = new JTextArea(mensaje);
        texto.setEditable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setOpaque(false);
        texto.setForeground(colorTexto);
        texto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        texto.setBorder(null);

        add(texto, BorderLayout.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        JViewport vista = (JViewport)
                SwingUtilities.getAncestorOfClass(JViewport.class, this);

        int anchoDisponible = (vista != null && vista.getWidth() > 0)
                ? vista.getWidth()
                : 800;

        Insets margen = getInsets();
        int anchoMaximo = (int) (anchoDisponible * PROPORCION_MAXIMA)
                - margen.left - margen.right;
        anchoMaximo = Math.max(anchoMaximo, 80);

        // Ancho que ocuparía el texto sin saltos automáticos de línea
        FontMetrics metricas = texto.getFontMetrics(texto.getFont());
        int anchoLinea = 0;
        for (String linea : texto.getText().split("\n", -1)) {
            anchoLinea = Math.max(anchoLinea, metricas.stringWidth(linea));
        }

        int anchoTexto = Math.min(anchoLinea + 6, anchoMaximo);

        // Con ese ancho, calculamos cuánta altura necesita el texto
        texto.setSize(anchoTexto, Short.MAX_VALUE);
        int altoTexto = texto.getPreferredSize().height;

        return new Dimension(
                anchoTexto + margen.left + margen.right,
                altoTexto + margen.top + margen.bottom
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(fondo);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIO, RADIO);
        g2.dispose();

        super.paintComponent(g);
    }
}