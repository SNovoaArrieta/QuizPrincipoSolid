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

