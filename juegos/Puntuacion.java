package juegos;

class Puntuacion {

    final PanelPuntuacion panelPuntuacion;

    private int numPartidas;
    private int puntosTotales;
    private int racha;
    private int derrotas, mejorables, perfectas;
    private Juego.resultado resultadoPrevio;

    Puntuacion() {
        panelPuntuacion = new PanelPuntuacion();

        numPartidas = 0;
        puntosTotales = 0;
        racha = 0;
        derrotas = 0;
        mejorables = 0;
        perfectas = 0;
    }

    void actualizar(int diferenciaPerfeccion, Juego.resultado resultado) {
        switch (resultado) {
            case DERROTA -> derrotas++;
            case MEJORABLE -> mejorables++;
            case PERFECTO -> perfectas++;
        }

        actualizarRacha(resultado);
        resultadoPrevio = resultado;

        int puntosUltimaPartida = 10 - diferenciaPerfeccion;
        puntosTotales += puntosUltimaPartida;
        numPartidas++;

        // Muestra los cambios al usuario
        panelPuntuacion.actualizar(perfectas, mejorables, derrotas, porcentaje(perfectas), porcentaje(mejorables),
                porcentaje(derrotas), numPartidas, puntosTotales, racha, resultado, puntosUltimaPartida, promedio());
    }

    private void actualizarRacha(Juego.resultado resultado) {
        if (resultado.equals(resultadoPrevio)) racha++;
        else racha = 1;
    }

    private double porcentaje(int numero) {
        if (numPartidas == 0) return 0;
        else return (double) numero / numPartidas * 100;
    }

    private double promedio() {
        if (numPartidas == 0) return 0;
        else return (double) puntosTotales / numPartidas;
    }
}
