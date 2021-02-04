package juegos;

class Puntuacion {

    final PanelPuntuacion panelPuntuacion;

    private final boolean partidaLetras;

    private int numPartidas;
    private int puntosTotales;
    private int derrotas, mejorables, perfectas;

    Puntuacion(boolean partidaLetras) {
        this.partidaLetras = partidaLetras;

        panelPuntuacion = new PanelPuntuacion();

        numPartidas = 0;
        puntosTotales = 0;
        derrotas = 0;
        mejorables = 0;
        perfectas = 0;
    }

    void actualizar(int diferenciaPerfeccion) {
        if (partidaLetras) {
            switch (diferenciaPerfeccion) {
                case 0 -> perfectas++;
                case 10 -> derrotas++;
                default -> mejorables++;
            }
        } else {
            switch (diferenciaPerfeccion) {
                case 0 -> perfectas++;
                case 1, 2, 3, 4 -> mejorables++;
                default -> derrotas++;
            }
        }

        int puntosUltimaPartida = 10 - diferenciaPerfeccion;
        puntosTotales += puntosUltimaPartida;
        numPartidas++;

        // Muestra los cambios al usuario
        panelPuntuacion.actualizar(perfectas, mejorables, derrotas, numPartidas, puntosTotales, puntosUltimaPartida,
                promedio());
    }

    private double promedio() {
        if (numPartidas == 0) return 0;
        else return (double) puntosTotales / numPartidas;
    }
}
