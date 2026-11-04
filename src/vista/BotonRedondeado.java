package vista;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

public class BotonRedondeado extends JButton {

    private final int radio;

    public BotonRedondeado(String texto, int radio) {
        super(texto);
        this.radio = radio;
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Color fondo = getBackground();
        if (!isEnabled()) {
            fondo = fondo.darker();
        } else if (getModel().isPressed()) {
            fondo = fondo.darker();
        } else if (getModel().isRollover()) {
            fondo = fondo.brighter();
        }

        g2.setColor(fondo);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
        g2.dispose();

        super.paintComponent(g);
    }
}