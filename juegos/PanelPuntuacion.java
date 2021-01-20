package juegos;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

class PanelPuntuacion extends JPanel {

    private final JLabel numPartidas, puntosTotales, puntosUltimaPartida, promedio;
    private final JLabel derrotas, mejorables, perfectas;

    PanelPuntuacion() {
        super(new GridLayout(4, 2, 10, 10));

        JLabel etiquetaPartidas = nuevoJLabel("Nº partidas:");
        numPartidas = nuevoJLabel("0");
        JLabel etiquetaPuntosActuales = nuevoJLabel("Puntos partida:");
        puntosUltimaPartida = nuevoJLabel("0");
        JLabel etiquetaPuntosTotales = nuevoJLabel("Total puntos:");
        puntosTotales = nuevoJLabel("0");
        JLabel etiquetaPromedio = nuevoJLabel("Puntos por partida:");
        promedio = nuevoJLabel("0,00");
        JLabel etiquetaDerrotas = nuevoJLabel("Derrotas:");
        derrotas = nuevoJLabel("0");
        JLabel etiquetaMejorables = nuevoJLabel("Mejorables:");
        mejorables = nuevoJLabel("0");
        JLabel etiquetaPerfectas = nuevoJLabel("Perfectas:");
        perfectas = nuevoJLabel("0");

        JPanel panelPerfectas = nuevoPanel(etiquetaPerfectas, perfectas);
        JPanel panelMejorables = nuevoPanel(etiquetaMejorables, mejorables);
        JPanel panelDerrotas = nuevoPanel(etiquetaDerrotas, derrotas);
        JPanel panelPartidas = nuevoPanel(etiquetaPartidas, numPartidas);
        JPanel panelUltimaPartida = nuevoPanel(etiquetaPuntosActuales, puntosUltimaPartida);
        JPanel panelTotal = nuevoPanel(etiquetaPuntosTotales, puntosTotales);
        JPanel panelPromedio = nuevoPanel(etiquetaPromedio, promedio);

        add(panelPerfectas);
        add(panelUltimaPartida);
        add(panelMejorables);
        add(panelTotal);
        add(panelDerrotas);
        add(panelPartidas);
        add(panelPromedio);
    }

    void actualizar(int perfectas, int mejorables, int derrotas, int numPartidas, int puntosTotales,
                    int puntosActuales, double promedio) {
        SwingUtilities.invokeLater(() -> {
            this.perfectas.setText(String.valueOf(perfectas));
            this.mejorables.setText(String.valueOf(mejorables));
            this.derrotas.setText(String.valueOf(derrotas));
            this.numPartidas.setText(String.valueOf(numPartidas));
            this.puntosTotales.setText(String.valueOf(puntosTotales));
            this.puntosUltimaPartida.setText(String.valueOf(puntosActuales));
            DecimalFormat df = new DecimalFormat("0.00");
            this.promedio.setText(df.format(promedio));
        });
    }

    private JLabel nuevoJLabel(String texto) {
        JLabel label = new JLabel(texto);

        label.setFont(new Font(Font.DIALOG, Font.BOLD, 15));

        return label;
    }

    private JPanel nuevoPanel(JLabel etiqueta, JLabel numero) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 0, 0, 20);
        c.weightx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(etiqueta, c);
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(numero, c);

        return panel;
    }
}
