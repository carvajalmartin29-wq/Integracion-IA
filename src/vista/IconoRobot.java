package vista;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;

public class IconoRobot extends JComponent {

    private final Color fondo;
    private final Color robot;

    public IconoRobot(Color fondo, Color robot, int tamano) {
        this.fondo = fondo;
        this.robot = robot;
        setPreferredSize(new Dimension(tamano, tamano));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);

        // Se dibuja en una cuadrícula de 46x46 y se escala al tamaño real
        g2.scale(getWidth() / 46.0, getHeight() / 46.0);

        // Fondo redondeado
        g2.setColor(fondo);
        g2.fill(new RoundRectangle2D.Double(0, 0, 46, 46, 16, 16));

        g2.setColor(robot);

        // Antena
        g2.fill(new RoundRectangle2D.Double(21.5, 9, 3, 6, 2, 2));
        g2.fill(new Ellipse2D.Double(19.5, 5, 7, 7));

        // Orejas
        g2.fill(new RoundRectangle2D.Double(6, 21, 4, 9, 3, 3));
        g2.fill(new RoundRectangle2D.Double(36, 21, 4, 9, 3, 3));

        // Cabeza
        g2.fill(new RoundRectangle2D.Double(10, 14, 26, 22, 10, 10));

        // Ojos y boca (del color del fondo, para que parezcan huecos)
        g2.setColor(fondo);
        g2.fill(new Ellipse2D.Double(15.5, 21, 5.5, 5.5));
        g2.fill(new Ellipse2D.Double(25, 21, 5.5, 5.5));
        g2.fill(new RoundRectangle2D.Double(17, 29.5, 12, 2.8, 3, 3));

        g2.dispose();
    }
}