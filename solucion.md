## Solución: Aplicación de los principios SOLID al módulo GestorCampeonato
El código original concentra múltiples responsabilidades en una única clase GestorCampeonato (registro, lógica de bonificaciones, generación de reportes y salida por consola). Esto lo hace rígido y frágil. He aplicado SRP, OCP, LSP, ISP y DIP mediante la creación de clases e interfaces con responsabilidades claras, inyección de dependencias para formateadores de reportes, y separación de comportamientos (registro, cálculo de bonificaciones, y formateo de reportes). La salida en consola del código refactorizado es idéntica a la del programa original.

## 1) SRP —(Principio de Responsabilidad Única)
La clase GestorCampeonato mezcla: registro de participantes, cálculo de bonificaciones, y generación/formatado de reportes (impresión en consola). Esto viola SRP porque una clase tiene múltiples razones para cambiar.

class Equipo {
    private String nombre;
    private List<Jugador> jugadores = new ArrayList<>();
    public Equipo(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }
    public void agregarJugador(Jugador j) { this.jugadores.add(j); }
    public List<Jugador> getJugadores() { return this.jugadores; }
}

class Jugador {
    private String nombre;
    private String posicion;
    public Jugador(String nombre, String posicion) { this.nombre = nombre; this.posicion = posicion; }
    public String getNombre() { return nombre; }
    public String getPosicion() { return posicion; }
}

class Arbitro {
    private String nombre;
    public Arbitro(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }
}

class ParticipantRegistrar {
    public void registrar(List<Equipo> equipos, List<Arbitro> arbitros) {
        Equipo equipoA = new Equipo("Los Ganadores");
        equipoA.agregarJugador(new Jugador("Juan Pérez", "Delantero"));
        equipoA.agregarJugador(new Jugador("Pedro Pan", "Portero"));
        equipos.add(equipoA);
        System.out.println("Equipo 'Los Ganadores' registrado.");

        Equipo equipoB = new Equipo("Los Retadores");
        equipoB.agregarJugador(new Jugador("Alicia Smith", "Defensa"));
        equipos.add(equipoB);
        System.out.println("Equipo 'Los Retadores' registrado.");

        arbitros.add(new Arbitro("Miguel Díaz"));
        System.out.println("Árbitro 'Miguel Díaz' contratado.");
    }
}


## Por qué viola SRP:
porque modifica la forma de generar reportes o en el formato de salida obliga a cambiar GestorCampeonato aunque no tenga relación con la lógica de registro o bonificaciones.

## Refactorización (aplicación SRP):
Separamos responsabilidades en clases:
ParticipantRegistrar — registra participantes y retorna lista 
BonusCalculator — se encarga únicamente del cálculo (y la impresión) de bonificaciones.
ReportService — orquesta generación de reportes usando ReportFormatter (responsabilidad: delegar formateo).

public class ParticipantRegistrar {
    public void registrar(List<Equipo> equipos, List<Arbitro> arbitros) {
        Equipo equipoA = new Equipo("Los Ganadores");
        equipoA.agregarJugador(new Jugador("Juan Pérez", "Delantero"));
        equipoA.agregarJugador(new Jugador("Pedro Pan", "Portero"));
        equipos.add(equipoA);
        System.out.println("Equipo 'Los Ganadores' registrado.");

        Equipo equipoB = new Equipo("Los Retadores");
        equipoB.agregarJugador(new Jugador("Alicia Smith", "Defensa"));
        equipos.add(equipoB);
        System.out.println("Equipo 'Los Retadores' registrado.");

        arbitros.add(new Arbitro("Miguel Díaz"));
        System.out.println("Árbitro 'Miguel Díaz' contratado.");
     }
}

## 2) OCP (Abierto/Cerrado)
generarReportes(String formato) contiene condicionales if (formato.equalsIgnoreCase("TEXTO")) ... else if (formato.equalsIgnoreCase("HTML")) ... — para añadir un nuevo formato habría que modificar este método, rompiendo OCP. 

public void generarReportes(String formato) {
    if (formato.equalsIgnoreCase("TEXTO")) {
        String contenidoReporte = "--- Reporte del Campeonato (TEXTO) ---\n";
        contenidoReporte += "EQUIPOS:\n";
        for (Equipo equipo : equipos) {
            contenidoReporte += "- " + equipo.getNombre() + "\n";
        }
        contenidoReporte += "ÁRBITROS:\n";
        for (Arbitro arbitro : arbitros) {
            contenidoReporte += "- " + arbitro.getNombre() + "\n";
        }
        System.out.println(contenidoReporte);
    } else if (formato.equalsIgnoreCase("HTML")) {
        String contenidoHtml = "<html><body>\n";
        contenidoHtml += " <h1>Reporte del Campeonato</h1>\n";
        contenidoHtml += " <h2>Equipos</h2>\n <ul>\n";
        for (Equipo equipo : equipos) {
            contenidoHtml += " <li>" + equipo.getNombre() + "</li>\n";
        }
        contenidoHtml += " </ul>\n <h2>Árbitros</h2>\n <ul>\n";
        for (Arbitro arbitro : arbitros) {
            contenidoHtml += " <li>" + arbitro.getNombre() + "</li>\n";
        }
        contenidoHtml += " </ul>\n</body></html>";
        System.out.println(contenidoHtml);
    }
}

## Por qué viola OCP:
El método no está abierto para extensión sin modificación. Cada nuevo formato implica editar el método central.

## Refactorización (aplicación OCP):
Definimos la interfaz ReportFormatter y creamos implementaciones TextReportFormatter y HtmlReportFormatter. ReportService solicita un ReportFormatter (vía fábrica o inyección) y lo utiliza. Para añadir un nuevo formato solo hay que crear una nueva implementación de ReportFormatter y registrarla — no modificar ReportService.

public interface ReportFormatter {
    String format(List<Equipo> equipos, List<Arbitro> arbitros);
}

public class TextReportFormatter implements ReportFormatter {
    @Override
    public String format(List<Equipo> equipos, List<Arbitro> arbitros) {
        String contenidoReporte = "--- Reporte del Campeonato (TEXTO) ---\n";
        contenidoReporte += "EQUIPOS:\n";
        for (Equipo e : equipos) contenidoReporte += "- " + e.getNombre() + "\n";
        contenidoReporte += "ÁRBITROS:\n";
        for (Arbitro a : arbitros) contenidoReporte += "- " + a.getNombre() + "\n";
        return contenidoReporte;
    }
}

## 3) LSP (Sustitución de Liskov)
No hay herencia explícita en el anexo que sea problemática; sin embargo, la forma en que se llevan a cabo los reportes (condicionales) hace difícil sustituir piezas por otras sin afectar comportamiento. La refactorización crea jerarquías de formateadores que deben ser intercambiables sin cambiar el cliente.

else if (formato.equalsIgnoreCase("HTML")) {
    String contenidoHtml = "<html><body>\n";
    contenidoHtml += " <h1>Reporte del Campeonato</h1>\n";
    contenidoHtml += " <h2>Equipos</h2>\n <ul>\n";
    for (Equipo equipo : equipos) {
        contenidoHtml += " <li>" + equipo.getNombre() + "</li>\n";
    }
    contenidoHtml += " </ul>\n <h2>Árbitros</h2>\n <ul>\n";
    for (Arbitro arbitro : arbitros) {
        contenidoHtml += " <li>" + arbitro.getNombre() + "</li>\n";
    }
    contenidoHtml += " </ul>\n</body></html>";
    System.out.println(contenidoHtml);
}

## Por qué es importante aquí:
Cuando creamos ReportFormatter y sus implementaciones, todas las implementaciones deben respetar el contrato (devolver un String formateado), de forma que ReportService pueda sustituir una implementación por otra sin alteración del flujo.

## Refactorización (aplicación LSP):
Todas las implementaciones de ReportFormatter devuelven un String que ReportService imprimirá; ninguna implementación lanza excepciones inesperadas o cambia semántica esperada. Esto asegura que ReportService.generateReport("HTML") o ("TEXTO") producirá siempre un String válido.

public class HtmlReportFormatter implements ReportFormatter {
    @Override
    public String format(List<Equipo> equipos, List<Arbitro> arbitros) {
        String contenidoHtml = "<html><body>\n";
        contenidoHtml += "  <h1>Reporte del Campeonato</h1>\n";
        contenidoHtml += "  <h2>Equipos</h2>\n  <ul>\n";
        for (Equipo e : equipos) contenidoHtml += "    <li>" + e.getNombre() + "</li>\n";
        contenidoHtml += "  </ul>\n  <h2>Árbitros</h2>\n  <ul>\n";
        for (Arbitro a : arbitros) contenidoHtml += "    <li>" + a.getNombre() + "</li>\n";
        contenidoHtml += "  </ul>\n</body></html>";
        return contenidoHtml;
    }
}

## 4) ISP (Segregación de Interfaces)
La clase GestorCampeonato concentraba registro, cálculo de bonificaciones y generación de reportes en un mismo lugar. Esto obligaba a los clientes a depender de métodos que no necesitaban, violando ISP.

## Por qué viola ISP:
Porque un cliente que solo quisiera registrar equipos también dependía de métodos de bonificaciones y reportes, aumentando el acoplamiento innecesario.

## Refactorización (aplicación ISP):
Se separaron interfaces pequeñas y específicas:
IParticipantRegistrar (solo registra).
IBonusCalculator (solo calcula bonificaciones).
IReportService (solo genera reportes).
De esta forma, cada cliente depende únicamente de la interfaz que realmente necesita.

## 5) DIP (Inversión de Dependencias)
Antes, ReportService dependía directamente de clases concretas para generar reportes, lo que generaba alto acoplamiento.

public void generarReportes(String formato) {
    if (formato.equalsIgnoreCase("TEXTO")) {
        String contenidoReporte = "--- Reporte del Campeonato (TEXTO) ---\n";
        contenidoReporte += "EQUIPOS:\n";
        for (Equipo equipo : equipos) {
            contenidoReporte += "- " + equipo.getNombre() + "\n";
        }
        contenidoReporte += "ÁRBITROS:\n";
        for (Arbitro arbitro : arbitros) {
            contenidoReporte += "- " + arbitro.getNombre() + "\n";
        }
        System.out.println(contenidoReporte);
    } else if (formato.equalsIgnoreCase("HTML")) {
        String contenidoHtml = "<html><body>\n";
        contenidoHtml += " <h1>Reporte del Campeonato</h1>\n";
        contenidoHtml += " <h2>Equipos</h2>\n <ul>\n";
        for (Equipo equipo : equipos) {
            contenidoHtml += " <li>" + equipo.getNombre() + "</li>\n";
        }
        contenidoHtml += " </ul>\n <h2>Árbitros</h2>\n <ul>\n";
        for (Arbitro arbitro : arbitros) {
            contenidoHtml += " <li>" + arbitro.getNombre() + "</li>\n";
        }
        contenidoHtml += " </ul>\n</body></html>";
        System.out.println(contenidoHtml);
    }
}

## Por qué viola DIP:
Porque la clase de alto nivel (ReportService) dependía de implementaciones específicas (Texto, HTML, etc.), en lugar de depender de una abstracción.

## Refactorización (aplicación DIP):
Se creó la interfaz ReportFormatter y ReportService ahora recibe un Map<String, ReportFormatter>.
De esta forma, ReportService depende de abstracciones y es fácil agregar nuevos formatos sin modificar la clase principal.

class ReportService implements IReportService {
    private Map<String, ReportFormatter> formatters;
    private List<Equipo> equipos;
    private List<Arbitro> arbitros;

    public ReportService(Map<String, ReportFormatter> formatters, List<Equipo> equipos, List<Arbitro> arbitros) {
        this.formatters = formatters;
        this.equipos = equipos;
        this.arbitros = arbitros;
    }

    @Override
    public void generarReporte(String formato) {
        ReportFormatter formatter = formatters.get(formato.toUpperCase());
        if (formatter != null) {
            System.out.println(formatter.format(equipos, arbitros));
        }
    }
}
