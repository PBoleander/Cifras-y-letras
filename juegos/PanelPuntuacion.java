package juegos;

import general.Colores;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

class PanelPuntuacion extends JPanel {

    private final JLabel numPartidas, puntosTotales, racha;
    private final JLabel derrotas, mejorables, perfectas;
    private final JLabel porcentajeDerrotas, porcentajeMejorables, porcentajePerfectas;
    private final JTextField promedio, puntosUltimaPartida;

    PanelPuntuacion() {
        super();

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        JLabel etiquetaPartidas = nuevoJLabel("Nº partidas:");
        numPartidas = nuevoJLabel();
        JLabel etiquetaPuntosActuales = nuevoJLabel("Puntos última partida:");
        puntosUltimaPartida = nuevoMarcador();
        JLabel etiquetaPuntosTotales = nuevoJLabel("Total puntos:");
        puntosTotales = nuevoJLabel();
        JLabel etiquetaPromedio = nuevoJLabel("Puntos por partida:");
        promedio = nuevoMarcador();
        JLabel etiquetaRacha = nuevoJLabel("Racha:");
        racha = nuevoJLabel();
        JLabel etiquetaDerrotas = nuevoJLabel("Derrotas:");
        derrotas = nuevoJLabel(Color.RED, false);
        porcentajeDerrotas = nuevoJLabel(Color.RED, true);
        JLabel etiquetaMejorables = nuevoJLabel("Mejorables:");
        mejorables = nuevoJLabel(Colores.NARANJA, false);
        porcentajeMejorables = nuevoJLabel(Colores.NARANJA, true);
        JLabel etiquetaPerfectas = nuevoJLabel("Perfectas:");
        perfectas = nuevoJLabel(Colores.VERDE, false);
        porcentajePerfectas = nuevoJLabel(Colores.VERDE, true);

        JPanel panelPerfectas = nuevoPanel(etiquetaPerfectas, perfectas, porcentajePerfectas);
        JPanel panelMejorables = nuevoPanel(etiquetaMejorables, mejorables, porcentajeMejorables);
        JPanel panelDerrotas = nuevoPanel(etiquetaDerrotas, derrotas, porcentajeDerrotas);
        JPanel panelRacha = nuevoPanel(etiquetaRacha, racha);
        JPanel panelPartidas = nuevoPanel(etiquetaPartidas, numPartidas);
        JPanel panelUltimaPartida = nuevoPanel(etiquetaPuntosActuales, puntosUltimaPartida);
        JPanel panelTotal = nuevoPanel(etiquetaPuntosTotales, puntosTotales);
        JPanel panelPromedio = nuevoPanel(etiquetaPromedio, promedio);

        GridBagConstraints c = new GridBagConstraints();
        JPanel columna1 = new JPanel(layout);
        JPanel columna2 = new JPanel(layout);

        c.gridx = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        columna1.add(panelPerfectas, c);
        columna1.add(panelMejorables, c);
        columna1.add(panelDerrotas, c);

        columna2.add(panelRacha, c);
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

        actualizar(0, 0, 0, 0, 0, 0,
                0, 0, 0, null, 0, 0);
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PACKAGE ***************************************************//
    //***************************************************************************************************************//

    void actualizar(int perfectas, int mejorables, int derrotas, double porcentajePerfectas,
                    double porcentajeMejorables, double porcentajeDerrotas, int numPartidas, int puntosTotales,
                    int racha, Juego.resultado resultado, int puntosActuales, double promedio) {
        DecimalFormat dfPorcentaje = new DecimalFormat("0.0");
        DecimalFormat dfPromedio = new DecimalFormat("0.00");
        String rachaString = (resultado == null) ? "0" : (racha + " " + resultado.toString().charAt(0));

        SwingUtilities.invokeLater(() -> {
            this.perfectas.setText(String.valueOf(perfectas));
            this.porcentajePerfectas.setText("(" + dfPorcentaje.format(porcentajePerfectas) + " %)");
            this.mejorables.setText(String.valueOf(mejorables));
            this.porcentajeMejorables.setText("(" + dfPorcentaje.format(porcentajeMejorables) + " %)");
            this.derrotas.setText(String.valueOf(derrotas));
            this.porcentajeDerrotas.setText("(" + dfPorcentaje.format(porcentajeDerrotas) + " %)");
            this.numPartidas.setText(String.valueOf(numPartidas));
            this.puntosTotales.setText(String.valueOf(puntosTotales));
            this.puntosUltimaPartida.setText(String.valueOf(puntosActuales));
            this.promedio.setText(dfPromedio.format(promedio));
            this.racha.setText(rachaString);
        });
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PRIVADOS **************************************************//
    //***************************************************************************************************************//

    private JLabel nuevoJLabel() {
        return nuevoJLabel("");
    }

    private JLabel nuevoJLabel(Color foreground, boolean porcentaje) {
        return nuevoJLabel("", foreground, porcentaje);
    }

    private JLabel nuevoJLabel(String texto) {
        return nuevoJLabel(texto, getForeground(), false);
    }

    private JLabel nuevoJLabel(String texto, Color foreground, boolean porcentaje) {
        JLabel label = new JLabel(texto);

        label.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        label.setForeground(foreground);
        label.setHorizontalAlignment(SwingConstants.RIGHT);

        if (porcentaje)
            label.setPreferredSize(new Dimension(100, 21));
        else if (texto.isEmpty())
            label.setPreferredSize(new Dimension(50, 21));

        return label;
    }

    private JTextField nuevoMarcador() {
        JTextField textField = new JTextField();

        textField.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
        textField.setEditable(false);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setForeground(Color.WHITE);
        textField.setBackground(Color.BLACK);
        textField.setPreferredSize(new Dimension(100, 40));

        return textField;
    }

    private JPanel nuevoPanel(JLabel etiqueta, JComponent numero) {
        return nuevoPanel(etiqueta, numero, null);
    }

    private JPanel nuevoPanel(JLabel etiqueta, JComponent numero, JLabel porcentaje) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 0, 0, 0);
        c.weightx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(etiqueta, c);
        c.weightx = 0;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(numero, c);
        if (porcentaje != null)
            panel.add(porcentaje, c);

        return panel;
    }
}
