package juegos;

import general.Colores;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

public class VisorCifras extends JPanel implements ActionListener, ContainerListener, Runnable {

    private final Cifras cifras;
    private final JLabel mostradorMinDiferencia;
    private final JPanel panelOperaciones;
    private final JTextField mostradorSolucion;
    private final PanelControl pc;

    private boolean primerRun;

    public VisorCifras() {
        super(new GridBagLayout());

        cifras = new Cifras();
        pc = new PanelControl(cifras);
        PanelControlCifras pcc = new PanelControlCifras(cifras);

        cifras.cifraObjetivo.addContainerListener(this); // Basta para que al iniciar nueva partida cambie el fondo
        pc.btnResolver.addActionListener(this);
        pc.btnPausa.addActionListener(this);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(5, 10, 5, 10);

        JPanel panelTiempo = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        panelTiempo.add(cifras.mostradorTiempo, c);

        c.gridx = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 0, 0);
        pc.btnPausa.setPreferredSize(new Dimension(110, 25));
        panelTiempo.add(pc.btnPausa, c);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(panelTiempo, constraints);

        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;

        constraints.gridy = 1;

        JPanel panelCifras = new JPanel(new GridLayout(2, 3, 5, 5));
        for (int i = 0; i < Cifras.numeroCifras; i++) {
            panelCifras.add(cifras.cifrasDisponibles[i]);
        }

        JPanel panelOperadores = new JPanel(new GridLayout(2, 2, 5, 5));
        for (int i = 0; i < 4; i++) {
            panelOperadores.add(cifras.operadores[i]);
        }

        panelOperaciones = new JPanel(new GridLayout(5, 1, 5, 5));
        panelOperaciones.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        for (int i = 0; i < 5; i++) {
            panelOperaciones.add(cifras.operacionesRealizadas[i]);
        }

        JPanel panelFichas = new JPanel(new GridBagLayout());
        GridBagConstraints fichasConstraints = new GridBagConstraints();
        fichasConstraints.insets = new Insets(0, 0, 5, 5);

        panelFichas.add(panelCifras, fichasConstraints);
        fichasConstraints.gridx = 1;
        panelFichas.add(panelOperadores, fichasConstraints);
        fichasConstraints.gridy = 1;
        fichasConstraints.gridx = 0;
        fichasConstraints.gridwidth = 2;
        panelFichas.add(panelOperaciones, fichasConstraints);

        add(panelFichas, constraints);

        JPanel columna2 = new JPanel(new GridBagLayout());
        GridBagConstraints columna2Constraints = new GridBagConstraints();
        columna2Constraints.insets = new Insets(0, 0, 20, 5);

        columna2.add(cifras.cifraObjetivo, columna2Constraints);

        columna2Constraints.gridx = 1;
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 10));
        panelBotones.add(pc.btnIniciar);
        panelBotones.add(pc.chkContrarreloj);
        columna2.add(panelBotones, columna2Constraints);

        columna2Constraints.gridx = 0;
        columna2Constraints.gridwidth = GridBagConstraints.REMAINDER;
        JPanel panelBotonesCifras = new JPanel(new GridLayout(1, 3, 10, 10));
        panelBotonesCifras.add(pcc.btnDeshacer);
        panelBotonesCifras.add(pc.btnLimpiar);
        panelBotonesCifras.add(pc.btnResolver);
        columna2.add(panelBotonesCifras, columna2Constraints);

        mostradorSolucion = new JTextField();
        mostradorSolucion.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        mostradorSolucion.setEditable(false);
        mostradorSolucion.setBackground(Color.WHITE);
        mostradorSolucion.setHorizontalAlignment(SwingConstants.CENTER);
        mostradorSolucion.setPreferredSize(new Dimension(460, 50));
        mostradorSolucion.setBorder(BorderFactory.createLineBorder(getForeground()));
        columna2.add(mostradorSolucion, columna2Constraints);

        mostradorMinDiferencia = new JLabel("Mínima diferencia conseguida:");
        columna2.add(mostradorMinDiferencia, columna2Constraints);

        columna2.add(cifras.puntuacion.panelPuntuacion, columna2Constraints);

        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        add(columna2, constraints);

        primerRun = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        if (primerRun) {
            primerRun = false;
            new Thread(this).start();

            while (cifras.estaResuelto()) {
                SwingUtilities.invokeLater(() -> {
                    switch (cifras.resultadoPartida) {
                        case DERROTA -> panelOperaciones.setBackground(Color.RED);
                        case PERFECTO -> panelOperaciones.setBackground(Colores.VERDE);
                        case MEJORABLE -> panelOperaciones.setBackground(Colores.NARANJA);
                    }

                    if (cifras.solucionador.getMinDiferencia() == 0) {
                        if (mostradorSolucion.getForeground() != getForeground())
                            mostradorSolucion.setForeground(getForeground());
                    } else {
                        if (mostradorSolucion.getForeground() != Colores.NARANJA)
                            mostradorSolucion.setForeground(Colores.NARANJA);
                    }

                    mostradorSolucion.setText(cifras.solucionador.toString());
                });
            }
        } else { // Se ejecuta el run desde el propio run (así se consigue poder hacer dos starts que hagan cosas dif)
            while (cifras.haCambiadoMinDiferencia()) {
                SwingUtilities.invokeLater(() -> mostradorMinDiferencia.setText(
                        "Mínima diferencia conseguida: " + cifras.minDiferenciaConseguida));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source.equals(pc.btnResolver)) {
            if (!cifras.estaSolucionado())
                mostradorSolucion.setText("Calculando...");

        } else if (source.equals(pc.btnPausa)) {
            if (cifras.haEmpezado()) {
                if (mostradorSolucion.getText().isBlank()) {
                    if (mostradorSolucion.getForeground() != getForeground()) {
                        mostradorSolucion.setForeground(getForeground());
                    }
                    mostradorSolucion.setText("JUEGO PAUSADO");

                } else {
                    mostradorSolucion.setText("");
                }
            }
        }
    }

    @Override
    public void componentAdded(ContainerEvent containerEvent) {
        if (panelOperaciones.getBackground() != null)
            panelOperaciones.setBackground(null);

        mostradorSolucion.setText("");
    }

    @Override
    public void componentRemoved(ContainerEvent containerEvent) {}
}
