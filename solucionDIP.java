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
