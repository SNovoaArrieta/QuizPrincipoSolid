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
