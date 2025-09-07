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
