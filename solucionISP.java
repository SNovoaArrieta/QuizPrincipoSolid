public interface IParticipantRegistrar {
    void registrar(List<Equipo> equipos, List<Arbitro> arbitros);
}

public interface IBonusCalculator {
    void calcularBonificaciones(List<Equipo> equipos);
}

public interface IReportService {
    void generarReporte(String formato);
}
