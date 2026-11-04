
# CodeMentor AI
## Asistente Inteligente de Ingeniería de Software

## 1. Descripción del proyecto

CodeMentor AI es una aplicación de escritorio desarrollada en Java que integra inteligencia artificial generativa mediante la API de Google Gemini. Su propósito es proporcionar una herramienta de apoyo académico para estudiantes de Ingeniería de Software, facilitando la consulta y comprensión de conceptos relacionados con las diferentes áreas de esta disciplina.

La aplicación permite realizar preguntas sobre programación, bases de datos, ingeniería de requisitos, arquitectura de software y pruebas de software. A partir de las consultas realizadas, el modelo de inteligencia artificial genera respuestas explicativas, ejemplos y orientaciones que contribuyen al proceso de aprendizaje.

El sistema cuenta con una interfaz gráfica desarrollada mediante Java Swing, desde la cual el usuario puede interactuar con el asistente a través de una conversación.

## 2. Objetivo

Desarrollar un prototipo de asistente académico que integre un modelo de lenguaje de inteligencia artificial en una aplicación de escritorio, utilizando Java y servicios externos mediante una API.

Se busca explorar la aplicación de la inteligencia artificial generativa en contextos educativos, así como sus posibilidades para apoyar la comprensión de conceptos técnicos y el aprendizaje autónomo.

## 3. Características principales

- **Interfaz gráfica:** permite la interacción entre el usuario y el asistente mediante una ventana desarrollada con Java Swing.
- **Integración con inteligencia artificial:** utiliza Google Gemini API para procesar consultas y generar respuestas.
- **Enfoque académico:** orienta las respuestas hacia contenidos relacionados con Ingeniería de Software.
- **Contexto conversacional:** conserva el historial de mensajes durante la sesión para proporcionar continuidad a las consultas.
- **Procesamiento asíncrono:** realiza las solicitudes a la API sin bloquear la interfaz gráfica.
- **Manejo de errores:** contempla situaciones relacionadas con problemas de conexión, disponibilidad del servicio y respuestas de la API.
- **Gestión de credenciales:** utiliza variables de entorno para acceder a la clave API sin incluirla directamente en el código fuente.

## 4. Tecnologías utilizadas

| Tecnología | Descripción |
|---|---|
| Java 21 | Lenguaje de programación utilizado para desarrollar la aplicación. |
| Java Swing | Biblioteca utilizada para construir la interfaz gráfica. |
| Java HttpClient | Herramienta utilizada para realizar solicitudes HTTP. |
| Google Gemini API | Servicio de inteligencia artificial generativa encargado de procesar las consultas. |
| Visual Studio Code | Entorno de desarrollo utilizado para la implementación del proyecto. |

## 5. Estructura del proyecto

```text
CodeMentorAI/
│
├── src/
│   ├── Main.java
│   ├── VistaPrincipal.java
│   ├── GeminiService.java
│   └── PromptAcademico.java
│
├── out/
│   └── Archivos compilados (.class)
│
└── README.md
```

### Descripción de los archivos

**Main.java**

Contiene el punto de entrada de la aplicación. Se encarga de inicializar la interfaz gráfica y ejecutar el programa.

**VistaPrincipal.java**

Implementa la interfaz gráfica mediante Java Swing. Gestiona los componentes visuales, la recepción de preguntas, la presentación de mensajes y la interacción entre el usuario y el servicio de inteligencia artificial.

**GeminiService.java**

Gestiona la comunicación con Google Gemini API mediante solicitudes HTTP. Se encarga de construir las peticiones, enviar las consultas, recibir las respuestas y administrar el historial de conversación.

**PromptAcademico.java**

Contiene las instrucciones que definen el comportamiento del asistente. Establece su rol académico, las áreas de conocimiento que debe abordar y los criterios que debe seguir al generar sus respuestas.

## 6. Requisitos del sistema

Para ejecutar el proyecto se requiere:

- Java Development Kit (JDK) 21 o superior.
- Un entorno de desarrollo compatible con Java.
- Conexión a Internet.
- Una clave de acceso a Google Gemini API.

## 7. Configuración de la API

La aplicación utiliza una clave de acceso para establecer comunicación con Google Gemini API. Por motivos de seguridad, esta clave se obtiene mediante una variable de entorno y no se almacena directamente en los archivos fuente.

Para obtener una clave de acceso:

1. Ingresar a [Google AI Studio](https://aistudio.google.com/apikey).
2. Generar una clave API.
3. Abrir una terminal de PowerShell.
4. Configurar la siguiente variable de entorno:

```powershell
$env:GEMINI_API_KEY="TU_API_KEY"
```

Para verificar que la variable se encuentra configurada:

```powershell
if ($env:GEMINI_API_KEY) {
    Write-Host "API Key configurada correctamente"
} else {
    Write-Host "API Key no encontrada"
}
```

La variable configurada mediante este procedimiento estará disponible durante la sesión actual de PowerShell. Si se abre una nueva terminal, será necesario configurarla nuevamente.

**Consideración de seguridad:** la clave API es información privada y no debe publicarse en repositorios públicos ni compartirse con terceros.

## 8. Compilación y ejecución

Una vez descargado o clonado el repositorio, se debe abrir una terminal de PowerShell desde la carpeta principal del proyecto.

### 8.1. Compilación

Ejecutar el siguiente comando:

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem .\src\*.java).FullName
```

Este comando compila los archivos fuente del proyecto y almacena los archivos `.class` generados en la carpeta `out`.

### 8.2. Ejecución

Una vez finalizada la compilación, ejecutar:

```powershell
java -cp out Main
```

Al ejecutar el programa se abrirá la interfaz gráfica de CodeMentor AI, desde la cual será posible realizar consultas al asistente.

## 9. Funcionamiento de la aplicación

El funcionamiento general de CodeMentor AI se desarrolla mediante la interacción de sus componentes principales:

1. El usuario ingresa una pregunta desde la interfaz gráfica.
2. La clase `VistaPrincipal` recibe el mensaje y solicita su procesamiento.
3. La clase `GeminiService` construye la solicitud HTTP, incorporando las instrucciones académicas y el historial de conversación.
4. La solicitud se envía a Google Gemini API para su procesamiento.
5. El modelo genera una respuesta de acuerdo con las instrucciones proporcionadas.
6. La aplicación recibe la respuesta y la presenta al usuario mediante la interfaz gráfica.

### Arquitectura general

```text
             USUARIO
                |
                v
         VISTA PRINCIPAL
            Java Swing
                |
                v
          GEMINI SERVICE
             HttpClient
                |
                v
         GOOGLE GEMINI API
                |
                v
        RESPUESTA GENERADA
                |
                v
         VISTA PRINCIPAL
                |
                v
             USUARIO
```

## 10. Alcance del proyecto

CodeMentor AI corresponde a un prototipo académico de aplicación de inteligencia artificial generativa. Su implementación permite demostrar la integración de un modelo de lenguaje externo en una aplicación desarrollada en Java, así como el intercambio de información mediante servicios web.

La aplicación está orientada al acompañamiento académico y constituye una aproximación al uso de modelos de lenguaje como herramientas de apoyo para el aprendizaje de Ingeniería de Software.

## Docente: 
Juan José Osorio Tabares 
## Programa: 
Ingeniería de Software
Universidad Manuela Beltrán

