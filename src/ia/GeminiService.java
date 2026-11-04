package ia;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GeminiService {

    private static final String MODELO = "gemini-3.8-flash";
    private static final String URL =
            "https://generativelanguage.googleapis.com/v1beta/models/"
            + MODELO + ":generateContent";

    private final HttpClient cliente;
    private final List<Turno> historial;

    public GeminiService() {
        cliente = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        historial = new ArrayList<>();
    }

    public boolean estaConfigurado() {
        String apiKey = System.getenv("GEMINI_API_KEY");
        return apiKey != null && !apiKey.isBlank();
    }

    public String generarRespuesta(String pregunta)
            throws IOException, InterruptedException {

        String apiKey = System.getenv("GEMINI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IOException(
                    "No se encontró la variable de entorno GEMINI_API_KEY."
            );
        }

        StringBuilder contenido = new StringBuilder();

        contenido.append("{")
                .append("\"system_instruction\":{")
                .append("\"parts\":[{\"text\":\"")
                .append(escaparJson(PromptAcademico.INSTRUCCIONES))
                .append("\"}]")
                .append("},")
                .append("\"contents\":[");

        for (Turno turno : historial) {
            agregarTurno(contenido, turno.rol, turno.texto);
            contenido.append(",");
        }

        agregarTurno(contenido, "user", pregunta);

        contenido.append("]")
                .append("}");

        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .timeout(Duration.ofSeconds(90))
                .header("Content-Type", "application/json")
                .header("x-goog-api-key", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(contenido.toString()))
                .build();

        HttpResponse<String> respuesta = cliente.send(
                solicitud,
                HttpResponse.BodyHandlers.ofString()
        );

        if (respuesta.statusCode() != 200) {
            String detalle = extraerCampoJson(
                    respuesta.body(), "message"
            );

            if (detalle == null) {
                detalle = respuesta.body();
            }

            throw new IOException(
                    "Error de Gemini (HTTP "
                    + respuesta.statusCode() + "): " + detalle
            );
        }

        String texto = extraerCampoJson(respuesta.body(), "text");

        if (texto == null || texto.isBlank()) {
            throw new IOException(
                    "Gemini respondió, pero no se encontró texto en la respuesta."
            );
        }

        historial.add(new Turno("user", pregunta));
        historial.add(new Turno("model", texto));

        if (historial.size() > 24) {
            historial.subList(0, 2).clear();
        }

        return texto;
    }

    private void agregarTurno(
            StringBuilder json, String rol, String texto) {

        json.append("{")
                .append("\"role\":\"").append(rol).append("\",")
                .append("\"parts\":[{\"text\":\"")
                .append(escaparJson(texto))
                .append("\"}]")
                .append("}");
    }

    private String escaparJson(String texto) {
        StringBuilder resultado = new StringBuilder();

        for (char caracter : texto.toCharArray()) {
            switch (caracter) {
                case '"':
                    resultado.append("\\\"");
                    break;
                case '\\':
                    resultado.append("\\\\");
                    break;
                case '\n':
                    resultado.append("\\n");
                    break;
                case '\r':
                    resultado.append("\\r");
                    break;
                case '\t':
                    resultado.append("\\t");
                    break;
                case '\b':
                    resultado.append("\\b");
                    break;
                case '\f':
                    resultado.append("\\f");
                    break;
                default:
                    if (caracter < 32) {
                        resultado.append(String.format(
                                "\\u%04x", (int) caracter
                        ));
                    } else {
                        resultado.append(caracter);
                    }
            }
        }

        return resultado.toString();
    }

    private String extraerCampoJson(String json, String campo) {
        String clave = "\"" + campo + "\"";
        int posicion = json.indexOf(clave);

        if (posicion < 0) {
            return null;
        }

        int dosPuntos = json.indexOf(':', posicion + clave.length());

        if (dosPuntos < 0) {
            return null;
        }

        int inicio = json.indexOf('"', dosPuntos + 1);

        if (inicio < 0) {
            return null;
        }

        StringBuilder resultado = new StringBuilder();
        boolean escape = false;

        for (int i = inicio + 1; i < json.length(); i++) {
            char c = json.charAt(i);

            if (escape) {
                switch (c) {
                    case '"':
                        resultado.append('"');
                        break;
                    case '\\':
                        resultado.append('\\');
                        break;
                    case '/':
                        resultado.append('/');
                        break;
                    case 'n':
                        resultado.append('\n');
                        break;
                    case 'r':
                        resultado.append('\r');
                        break;
                    case 't':
                        resultado.append('\t');
                        break;
                    case 'b':
                        resultado.append('\b');
                        break;
                    case 'f':
                        resultado.append('\f');
                        break;
                    case 'u':
                        if (i + 4 < json.length()) {
                            String codigo = json.substring(i + 1, i + 5);
                            resultado.append((char) Integer.parseInt(codigo, 16));
                            i += 4;
                        }
                        break;
                    default:
                        resultado.append(c);
                }

                escape = false;

            } else if (c == '\\') {
                escape = true;

            } else if (c == '"') {
                return resultado.toString();

            } else {
                resultado.append(c);
            }
        }

        return null;
    }

    public void limpiarHistorial() {
        historial.clear();
    }

    private static class Turno {
        String rol;
        String texto;

        Turno(String rol, String texto) {
            this.rol = rol;
            this.texto = texto;
        }
    }
}
