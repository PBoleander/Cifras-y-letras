package juegos;

import general.Colores;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

public class VisorCifras extends JPanel implements ActionListener, ContainerListener, Runnable {

    public final Cifras cifras;

    private final JLabel mostradorMinDiferencia;
    private final JPanel panelOperaciones;
    private final JTextField mostradorSolucion;
    private final PanelControl pc;

    private boolean primerRun;

    public VisorCifras() {
        super();

        GridBagLayout gridBagLayout = new GridBagLayout(); // Se usará en la mayoría de subpaneles además de en this
        setLayout(gridBagLayout);

        /* Instancias generales */
        cifras = new Cifras();
        mostradorMinDiferencia = new JLabel("Mínima diferencia conseguida:");
        mostradorSolucion = new JTextField();
        pc = new PanelControl(cifras);
        panelOperaciones = new JPanel();

        /* Se añaden los listeners */
        pc.btnResolver.addActionListener(this);
        pc.btnPausa.addActionListener(this);
        cifras.cifraObjetivo.addContainerListener(this); // Sirve para que al iniciar nueva partida cambie el fondo
        // del panel de operaciones

        /* Maquetación de la interfaz */

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 10, 5, 10);

        /* Panel que contendrá el mostrador del tiempo */
        JPanel panelTiempo = crearPanelTiempo(gridBagLayout);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(panelTiempo, constraints);

        /* Paneles de cifras, operadores y operaciones (contenedores de fichas) */
        JPanel panelFichas = crearPanelFichas(gridBagLayout);

        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridy = 1;
        add(panelFichas, constraints);

        /* 2a columna */
        JPanel columna2 = crearPanelColumna2(gridBagLayout);

        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        add(columna2, constraints);

        primerRun = true;
        new Thread(this).start();
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PÚBLICOS **************************************************//
    //***************************************************************************************************************//

    @Override
    public void run() {
        if (primerRun) {
            primerRun = false;
            new Thread(this).start(); // Realizará la otra tarea de este run() (el else)

            while (cifras.estaResuelto()) {
                SwingUtilities.invokeLater(() -> {
                    if (cifras.solucionador.getMinDiferencia() == 0) {
                        if (mostradorSolucion.getForeground() != getForeground())
                            mostradorSolucion.setForeground(getForeground());
                    } else {
                        if (mostradorSolucion.getForeground() != Colores.NARANJA)
                            mostradorSolucion.setForeground(Colores.NARANJA);
                    }

                    mostradorSolucion.setText(cifras.solucionador.toString());

                    switch (cifras.resultadoPartida) {
                        case DERROTA -> panelOperaciones.setBackground(Color.RED);
                        case PERFECTO -> panelOperaciones.setBackground(Colores.VERDE);
                        case MEJORABLE -> panelOperaciones.setBackground(Colores.NARANJA);
                    }
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
            if (cifras.haEmpezado() && !cifras.estaSolucionado())
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

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PRIVADOS **************************************************//
    //***************************************************************************************************************//

    private JPanel crearPanelColumna2(GridBagLayout gridBagLayout) {
        JPanel columna2 = new JPanel(gridBagLayout);
        GridBagConstraints columna2Constraints = new GridBagConstraints();
        columna2Constraints.insets = new Insets(0, 0, 20, 5);

        /* 1a fila (botones de nueva partida y contrarreloj) */
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 10));
        panelBotones.add(pc.btnIniciar);
        panelBotones.add(pc.chkContrarreloj);

        columna2.add(cifras.cifraObjetivo, columna2Constraints);
        columna2Constraints.gridx = 1;
        columna2.add(panelBotones, columna2Constraints);

        /* 2a fila (botones que controlan el juego actual) */
        PanelControlCifras pcc = new PanelControlCifras(cifras);
        JPanel panelBotonesCifras = new JPanel(new GridLayout(1, 3, 10, 10));
        panelBotonesCifras.add(pcc.btnDeshacer);
        panelBotonesCifras.add(pc.btnLimpiar);
        panelBotonesCifras.add(pc.btnResolver);

        columna2Constraints.gridx = 0;
        columna2Constraints.gridwidth = GridBagConstraints.REMAINDER;
        columna2.add(panelBotonesCifras, columna2Constraints);

        /* 3a fila (panel donde se muestran las soluciones) */
        JPanel solucionario = new JPanel(gridBagLayout);

        JLabel tituloSolucion = new JLabel("Mejor solución:");

        GridBagConstraints solucionarioConstraints = new GridBagConstraints();
        solucionarioConstraints.gridx = 0;
        solucionario.add(tituloSolucion, solucionarioConstraints);

        mostradorSolucion.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        mostradorSolucion.setEditable(false);
        mostradorSolucion.setBackground(Color.WHITE);
        mostradorSolucion.setHorizontalAlignment(SwingConstants.CENTER);
        mostradorSolucion.setPreferredSize(new Dimension(460, 50));
        mostradorSolucion.setBorder(BorderFactory.createLineBorder(getForeground()));

        solucionario.add(mostradorSolucion, solucionarioConstraints);

        columna2.add(solucionario, columna2Constraints);

        /* 4a y 5a filas */
        columna2.add(mostradorMinDiferencia, columna2Constraints);
        columna2.add(cifras.puntuacion.panelPuntuacion, columna2Constraints);

        return columna2;
    }

    private JPanel crearPanelFichas(GridBagLayout gridBagLayout) {
        /* Se crean los paneles que contendrán las cifras, los operadores y las operaciones */
        JPanel panelCifras = new JPanel(new GridLayout(2, 3, 5, 5));
        for (int i = 0; i < Cifras.numeroCifras; i++) {
            panelCifras.add(cifras.cifrasDisponibles[i]);
        }

        JPanel panelOperadores = new JPanel(new GridLayout(2, 2, 5, 5));
        for (int i = 0; i < 4; i++) {
            panelOperadores.add(cifras.operadores[i]);
        }

        panelOperaciones.setLayout(new GridLayout(5, 1, 5, 5));
        panelOperaciones.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        for (int i = 0; i < 5; i++) {
            panelOperaciones.add(cifras.operacionesRealizadas[i]);
        }

        JPanel panelFichas = new JPanel(gridBagLayout); // Panel que contendrá todos los contenedores de fichas
        GridBagConstraints fichasConstraints = new GridBagConstraints();
        fichasConstraints.insets = new Insets(0, 0, 5, 5);

        /* Se añaden todos los subpaneles al panel de fichas */
        panelFichas.add(panelCifras, fichasConstraints);

        fichasConstraints.gridx = 1;
        panelFichas.add(panelOperadores, fichasConstraints);

        fichasConstraints.gridx = 0;
        fichasConstraints.gridwidth = 2;
        panelFichas.add(panelOperaciones, fichasConstraints);

        return panelFichas;
    }

    private JPanel crearPanelTiempo(GridBagLayout gridBagLayout) {
        JPanel panelTiempo = new JPanel(gridBagLayout);
        GridBagConstraints panelTiempoConstraints = new GridBagConstraints();

        /* Se añade el mostrador de tiempo */
        panelTiempoConstraints.insets = new Insets(0, 0, 0, 10);
        panelTiempoConstraints.fill = GridBagConstraints.HORIZONTAL;
        panelTiempoConstraints.weightx = 1;
        panelTiempo.add(cifras.mostradorTiempo, panelTiempoConstraints);

        /* Se añade el botón de pausa */
        panelTiempoConstraints.gridx = 1;
        panelTiempoConstraints.fill = GridBagConstraints.NONE;
        panelTiempoConstraints.weightx = 0;
        panelTiempoConstraints.insets = new Insets(0, 0, 0, 0);
        panelTiempo.add(pc.btnPausa, panelTiempoConstraints);

        return panelTiempo;
    }
}
