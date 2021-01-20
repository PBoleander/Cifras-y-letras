package juegos;

import javax.swing.*;
import java.text.DecimalFormat;

class Puntuacion {

    final PanelPuntuacion panelPuntuacion;

    private int numPartidas, puntosTotales, puntosUltimaPartida;
    private int derrotas, mejorables, perfectas;

    Puntuacion() {
        panelPuntuacion = new PanelPuntuacion();

        numPartidas = 0;
        puntosTotales = 0;
        derrotas = 0;
        mejorables = 0;
        perfectas = 0;
    }

    void actualizar(int diferenciaPerfeccion) {
        switch (diferenciaPerfeccion) {
            case 0 -> perfectas++;
            case 10 -> derrotas++;
            default -> mejorables++;
        }

        puntosUltimaPartida = 10 - diferenciaPerfeccion;
        puntosTotales += puntosUltimaPartida;
        numPartidas++;

        panelPuntuacion.actualizar(perfectas, mejorables, derrotas, numPartidas, puntosTotales, puntosUltimaPartida,
                promedio());
    }

    private double promedio() {
        if (numPartidas == 0) return 0;
        else return (double) puntosTotales / numPartidas;
    }
}
