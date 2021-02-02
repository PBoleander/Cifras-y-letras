package juegos;

import general.Colores;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

class PanelPuntuacion extends JPanel {

    private final JLabel numPartidas, puntosTotales;
    private final JLabel derrotas, mejorables, perfectas;
    private final JTextField promedio, puntosUltimaPartida;

    PanelPuntuacion() {
        super(new GridBagLayout());

        JLabel etiquetaPartidas = nuevoJLabel("Nº partidas:");
        numPartidas = nuevoJLabel("0");
        JLabel etiquetaPuntosActuales = nuevoJLabel("Puntos última partida:");
        puntosUltimaPartida = nuevoMarcador("0");
        JLabel etiquetaPuntosTotales = nuevoJLabel("Total puntos:");
        puntosTotales = nuevoJLabel("0");
        JLabel etiquetaPromedio = nuevoJLabel("Puntos por partida:");
        promedio = nuevoMarcador("0,00");
        JLabel etiquetaDerrotas = nuevoJLabel("Derrotas:");
        derrotas = nuevoJLabel("0", Color.RED);
        JLabel etiquetaMejorables = nuevoJLabel("Mejorables:");
        mejorables = nuevoJLabel("0", Colores.NARANJA);
        JLabel etiquetaPerfectas = nuevoJLabel("Perfectas:");
        perfectas = nuevoJLabel("0", Colores.VERDE);

        JPanel panelPerfectas = nuevoPanel(etiquetaPerfectas, perfectas);
        JPanel panelMejorables = nuevoPanel(etiquetaMejorables, mejorables);
        JPanel panelDerrotas = nuevoPanel(etiquetaDerrotas, derrotas);
        JPanel panelPartidas = nuevoPanel(etiquetaPartidas, numPartidas);
        JPanel panelUltimaPartida = nuevoPanel(etiquetaPuntosActuales, puntosUltimaPartida);
        JPanel panelTotal = nuevoPanel(etiquetaPuntosTotales, puntosTotales);
        JPanel panelPromedio = nuevoPanel(etiquetaPromedio, promedio);

        GridBagConstraints c = new GridBagConstraints();
        JPanel columna1 = new JPanel(new GridBagLayout());
        JPanel columna2 = new JPanel(new GridBagLayout());

        c.gridx = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        columna1.add(panelPerfectas, c); // FIXME Son éste y...
        columna1.add(panelMejorables, c); // FIXME ¿Por qué está desalineado un píxel respecto a los otros?
        columna1.add(panelDerrotas, c); // FIXME ...éste los que están mal alineados, el de mejorables está correcto

        columna2.add(panelTotal, c);
        columna2.add(panelPartidas, c);

        c.insets = new Insets(20, 20, 0, 20);
        add(columna1, c);
        c.gridx = 1;
        add(columna2, c);
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        add(panelUltimaPartida, c);
        add(panelPromedio, c);
    }

    void actualizar(int perfectas, int mejorables, int derrotas, int numPartidas, int puntosTotales,
                    int puntosActuales, double promedio) {
        DecimalFormat df = new DecimalFormat("0.00");
        SwingUtilities.invokeLater(() -> {
            this.perfectas.setText(String.valueOf(perfectas));
            this.mejorables.setText(String.valueOf(mejorables));
            this.derrotas.setText(String.valueOf(derrotas));
            this.numPartidas.setText(String.valueOf(numPartidas));
            this.puntosTotales.setText(String.valueOf(puntosTotales));
            this.puntosUltimaPartida.setText(String.valueOf(puntosActuales));
            this.promedio.setText(df.format(promedio));
        });
    }

    private JLabel nuevoJLabel(String texto) {
        return nuevoJLabel(texto, getForeground());
    }

    private JLabel nuevoJLabel(String texto, Color foreground) {
        JLabel label = new JLabel(texto);

        label.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        label.setForeground(foreground);

        return label;
    }

    private JTextField nuevoMarcador(String texto) {
        JTextField textField = new JTextField(texto);

        textField.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
        textField.setEditable(false);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setForeground(Color.WHITE);
        textField.setBackground(Color.BLACK);
        textField.setPreferredSize(new Dimension(100, 40));

        return textField;
    }

    private JPanel nuevoPanel(JLabel etiqueta, JComponent numero) {
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
