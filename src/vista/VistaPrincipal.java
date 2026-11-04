package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import ia.GeminiService;


public class VistaPrincipal extends JFrame {

    private static final Color FONDO = new Color(16, 27, 45);
    private static final Color PANEL = new Color(24, 39, 62);
    private static final Color ACENTO = new Color(111, 91, 240);
    private static final Color TEXTO = new Color(235, 240, 250);
    private static final Color SECUNDARIO = new Color(165, 178, 199);

    private ContenedorChat conversacion;
    private JTextField campoPregunta;
    private JButton botonEnviar;
    private JButton botonLimpiar;

    private final GeminiService gemini;

    public VistaPrincipal() {

        gemini = new GeminiService();

        setTitle("CodeMentor AI | Asistente de Ingeniería de Software");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);

        getContentPane().setBackground(FONDO);
        setLayout(new BorderLayout(0, 0));

        add(crearEncabezado(), BorderLayout.NORTH);

        conversacion = new ContenedorChat(FONDO);

       JScrollPane scroll = new JScrollPane(conversacion);
       scroll.setBorder(BorderFactory.createEmptyBorder());
       scroll.getViewport().setBackground(FONDO);

        add(scroll, BorderLayout.CENTER);
        add(crearPanelEntrada(), BorderLayout.SOUTH);

        agregarMensaje(
                "CodeMentor AI",
                "¡Hola! Soy tu asistente académico de Ingeniería de Software.\n"
                + "Puedo ayudarte con programación, bases de datos, requisitos, "
                + "arquitectura y pruebas de software.\n\n"
                + "Ya estoy conectado a Gemini. ¿Qué vamos a aprender hoy?"
        );
    }

    private JPanel crearEncabezado() {

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(PANEL);
        encabezado.setBorder(
                BorderFactory.createEmptyBorder(18, 24, 18, 24)
        );

        JPanel identidad = new JPanel(new BorderLayout(12, 2));
        identidad.setOpaque(false);

        IconoRobot icono = new IconoRobot(ACENTO, Color.WHITE, 46);

        JPanel textos = new JPanel(new BorderLayout(0, 3));
        textos.setOpaque(false);

        JLabel nombre = new JLabel("CodeMentor AI");
        nombre.setForeground(TEXTO);
        nombre.setFont(new Font("Segoe UI", Font.BOLD, 21));

        JLabel subtitulo = new JLabel(
                "Asistente inteligente de Ingeniería de Software"
        );
        subtitulo.setForeground(SECUNDARIO);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        textos.add(nombre, BorderLayout.NORTH);
        textos.add(subtitulo, BorderLayout.SOUTH);

        identidad.add(icono, BorderLayout.WEST);
        identidad.add(textos, BorderLayout.CENTER);

        JLabel estado = new JLabel(
                gemini.estaConfigurado()
                ? "● GEMINI CONFIGURADO"
                : "● API KEY NO DETECTADA"
        );

        estado.setForeground(
                gemini.estaConfigurado()
                ? new Color(130, 210, 165)
                : new Color(255, 150, 120)
        );

        estado.setFont(new Font("Segoe UI", Font.BOLD, 11));

        encabezado.add(identidad, BorderLayout.WEST);
        encabezado.add(estado, BorderLayout.EAST);

        return encabezado;
    }

    private JPanel crearPanelEntrada() {

        JPanel contenedor = new JPanel(new BorderLayout(10, 0));
        contenedor.setBackground(PANEL);
        contenedor.setBorder(
                BorderFactory.createEmptyBorder(16, 20, 18, 20)
        );

        
        campoPregunta = new CampoRedondeado(
        30,
        new Color(35, 52, 78),
        new Color(65, 82, 111)
);
        campoPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoPregunta.setForeground(TEXTO);
        campoPregunta.setCaretColor(TEXTO);
        campoPregunta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(65, 82, 111)
                        ),
                        BorderFactory.createEmptyBorder(
                                12, 14, 12, 14
                        )
                )
        );

        campoPregunta.setToolTipText(
                "Escribe tu pregunta sobre Ingeniería de Software"
        );

        botonEnviar = new BotonRedondeado("Enviar", 30);
        botonEnviar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        botonEnviar.setForeground(new Color(82, 46, 48));
        botonEnviar.setBackground(new Color(84, 67, 150));
        botonEnviar.setFocusPainted(false);
        botonEnviar.setBorder(
                BorderFactory.createEmptyBorder(0, 22, 0, 22)
        );
        botonEnviar.setCursor(
                new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)
        );

        botonEnviar.addActionListener(this::enviarPregunta);
        campoPregunta.addActionListener(this::enviarPregunta);

        botonLimpiar = new BotonRedondeado("Limpiar", 30);
        botonLimpiar.setForeground(Color.white);
        botonLimpiar.setBackground(Color.DARK_GRAY);
        botonLimpiar.setFocusPainted(false);
        botonLimpiar.setBorder(
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        );

        botonLimpiar.addActionListener(e -> {
            conversacion.limpiar();
            gemini.limpiarHistorial();
        });

        JPanel acciones = new JPanel(new BorderLayout(8, 0));
        acciones.setOpaque(false);

        acciones.add(botonLimpiar, BorderLayout.WEST);
        acciones.add(botonEnviar, BorderLayout.EAST);

        contenedor.add(campoPregunta, BorderLayout.CENTER);
        contenedor.add(acciones, BorderLayout.EAST);

        return contenedor;
    }

    private void enviarPregunta(ActionEvent evento) {

        String pregunta = campoPregunta.getText().trim();

        if (pregunta.isEmpty()) {
            return;
        }

        agregarMensaje("Tú", pregunta);

        campoPregunta.setText("");
        campoPregunta.setEnabled(false);
        botonEnviar.setEnabled(false);
        botonLimpiar.setEnabled(false);
        botonEnviar.setText("...");

        SwingWorker<String, Void> trabajador = new SwingWorker<>() {

            @Override
            protected String doInBackground() throws Exception {
                return gemini.generarRespuesta(pregunta);
            }

            @Override
            protected void done() {

                try {
                    String respuesta = get();
                    agregarMensaje("CodeMentor AI", respuesta);

                } catch (Exception e) {

                    Throwable causa = e.getCause() != null
                            ? e.getCause()
                            : e;

                    agregarMensaje(
                            "CodeMentor AI",
                            "No pude generar una respuesta.\n\n"
                            + "Detalle del error: "
                            + causa.getMessage()
                    );

                } finally {

                    campoPregunta.setEnabled(true);
                    botonEnviar.setEnabled(true);
                    botonLimpiar.setEnabled(true);
                    botonEnviar.setText("Enviar");
                    campoPregunta.requestFocusInWindow();
                }
            }
        };

        trabajador.execute();
    }

    private void agregarMensaje(String autor, String mensaje) {
    boolean esUsuario = autor.equals("Tú");

    conversacion.agregarMensaje(
            mensaje,
            esUsuario,
            esUsuario ? ACENTO : new Color(35, 52, 78),
            esUsuario ? Color.WHITE : TEXTO
    );
}
}
