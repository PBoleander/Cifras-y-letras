package juegos;

import javax.swing.*;
import java.text.DecimalFormat;

class Puntuacion extends JTextArea {

    private int numPartidas, puntosTotales, puntosUltimaPartida;
    private int derrotas, mejorables, perfectas;

    Puntuacion() {
        super();

        setBackground(null);
        setText(toString());

        numPartidas = 0;
        puntosTotales = 0;
        derrotas = 0;
        mejorables = 0;
        perfectas = 0;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.0");
        return "Perfectos: " + perfectas +
                System.lineSeparator() +
                "Mejorables: " + mejorables +
                System.lineSeparator() +
                "Derrotas: " + derrotas +
                System.lineSeparator() +
                "Nº partidas: " + numPartidas +
                System.lineSeparator() +
                "Puntuación actual: " + puntosUltimaPartida +
                System.lineSeparator() +
                "Puntuación total: " + puntosTotales +
                System.lineSeparator() +
                "Puntos por partida: " + df.format(promedio());
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

        setText(toString());
    }

    private double promedio() {
        if (numPartidas == 0) return 0;
        else return (double) puntosTotales / numPartidas;
    }
}
