package juegos;

import general.Colores;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VisorLetras extends JPanel implements ActionListener, ContainerListener, FocusListener, MouseListener,
        Runnable {

    public final Letras letras;

    private final JPanel panelLetrasPuestas;
    private final PanelControl pc;
    private final PanelControlLetras pcl;

    private boolean primerRun;

    public VisorLetras() {
        super();

        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        letras = new Letras();
        panelLetrasPuestas = new JPanel();
        pc = new PanelControl(letras);
        pcl = new PanelControlLetras(letras);

        // Se añaden los listeners

        setFocusable(true);
        addKeyListener(letras);
        addFocusListener(this);
        pcl.selectorIdioma.addActionListener(this);
        pcl.selectorIdioma.addMouseListener(this);
        pcl.selectorIdioma.addFocusListener(this);

        // Empieza todo lo relacionado con el GUI
        GridBagConstraints c = new GridBagConstraints();

        JPanel columna1 = crearPanelColumna1(gridBagLayout);
        JPanel columna2 = crearPanelColumna2(gridBagLayout);

        // Se añaden las dos columnas a this
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTH;
        add(columna1, c);

        c.weighty = 1;
        c.fill = GridBagConstraints.VERTICAL;
        c.gridheight = GridBagConstraints.REMAINDER;
        add(columna2, c);

        primerRun = true;
        new Thread(this).start(); // Actualiza el fondo del panel de letras puestas
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PÚBLICOS **************************************************//
    //***************************************************************************************************************//

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // Cuando el selector de idioma ha hecho su trabajo devuelve el foco al panel
        if (!isFocusOwner())
            SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    @Override
    public void componentAdded(ContainerEvent containerEvent) {
        if (panelLetrasPuestas.getBackground() != null)
            panelLetrasPuestas.setBackground(null);
    }

    @Override
    public void componentRemoved(ContainerEvent containerEvent) {
        if (panelLetrasPuestas.getBackground() != null)
            panelLetrasPuestas.setBackground(null);
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {}

    @Override
    public void focusLost(FocusEvent focusEvent) {
        if (focusEvent.getSource().equals(this) && focusEvent.getOppositeComponent() != pcl.selectorIdioma)
            SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}
    @Override
    public void mousePressed(MouseEvent mouseEvent) {}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        // FIXME Hay un bug que no sé cómo arreglar: si se clica la flecha del desplegable no se devuelve el foco a this
        if (mouseEvent.getSource().equals(pcl.selectorIdioma)) {
            if (!pcl.selectorIdioma.isPopupVisible()) {
                if (!isFocusOwner())
                    SwingUtilities.invokeLater(this::requestFocusInWindow);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}
    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    @Override
    public void run() {
        if (primerRun) {
            primerRun = false;
            new Thread(this).start(); // Actualiza el label de memoria

            while (letras.estaComprobado()) {
                SwingUtilities.invokeLater(() -> {
                    if (letras.haEmpezado()) {
                        if (letras.resultadoComprobacion)
                            panelLetrasPuestas.setBackground(Colores.VERDE);
                        else
                            panelLetrasPuestas.setBackground(Color.RED);

                    } else {
                        switch (letras.resultadoPartida) {
                            case PERFECTO -> panelLetrasPuestas.setBackground(Colores.VERDE);
                            case MEJORABLE -> panelLetrasPuestas.setBackground(Colores.NARANJA);
                            case DERROTA -> panelLetrasPuestas.setBackground(Color.RED);
                        }

                    }
                });
            }
        } else { // 2º run
            while (letras.cambioEnMensajeMemoria())
                actualizarLabelMemoria();
        }
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PRIVADOS **************************************************//
    //***************************************************************************************************************//

    private void actualizarLabelMemoria() {
        String palabraMemorizada = letras.getPalabraMemorizada();
        StringBuilder sb = new StringBuilder("Memoria: ");

        if (!letras.estaPausado() && palabraMemorizada.length() > 0) {
            sb.append(palabraMemorizada).append(" (").append(palabraMemorizada.length()).append(" letras)");
        }

        SwingUtilities.invokeLater(() -> pcl.labelMemoria.setText(sb.toString()));
    }

    private JPanel crearPanelColumna1(GridBagLayout gridBagLayout) {
        JPanel columna1 = new JPanel(gridBagLayout);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 10, 5, 10);

        // 2a fila (barra de progreso y botón de pausar/reanudar) (va antes de la primera en el código porque tiene
        // propiedades especiales como el fill o weight)
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        columna1.add(letras.mostradorTiempo, constraints);

        constraints.weightx = 0;
        constraints.fill = GridBagConstraints.NONE; // Reiniciamos el valor de constraints fill al original
        constraints.gridx = 1;
        columna1.add(pc.btnPausa, constraints);

        // 1a fila (botones de nueva partida, resolver; checkbox de contrarreloj y selector de idioma)
        JPanel panelBotonesGenerales = new JPanel(gridBagLayout);

        GridBagConstraints botonesGeneralesConstraints = new GridBagConstraints();
        botonesGeneralesConstraints.insets = new Insets(0, 10, 0, 10);

        panelBotonesGenerales.add(pc.btnIniciar, botonesGeneralesConstraints);
        panelBotonesGenerales.add(pc.btnResolver, botonesGeneralesConstraints);
        panelBotonesGenerales.add(pc.chkContrarreloj, botonesGeneralesConstraints);
        panelBotonesGenerales.add(pcl.panelIdioma, botonesGeneralesConstraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        columna1.add(panelBotonesGenerales, constraints);

        /* 3a y 4a filas (espacio para las fichas con las que jugar) */
        // 3a fila
        JPanel panelLetrasDisponibles = new JPanel(new GridLayout(1, Letras.numeroLetras, 10, 0));
        for (int i = 0; i < Letras.numeroLetras; i++) {
            letras.letrasDisponibles[i].addContainerListener(this);
            panelLetrasDisponibles.add(letras.letrasDisponibles[i]);
        }

        constraints.gridy = 2;
        columna1.add(panelLetrasDisponibles, constraints);

        // 4a fila
        panelLetrasPuestas.setLayout(new GridLayout(1, Letras.numeroLetras, 10, 0));
        panelLetrasPuestas.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for (int i = 0; i < Letras.numeroLetras; i++) {
            letras.letrasPuestas[i].addContainerListener(this);
            panelLetrasPuestas.add(letras.letrasPuestas[i]);
        }

        constraints.gridy = GridBagConstraints.RELATIVE;
        columna1.add(panelLetrasPuestas, constraints);

        // 5a fila (botones más relacionados con el juego en sí: limpiar intentos, comprobar palabra y memoria)
        JPanel panelControlesPartidaActual = new JPanel(gridBagLayout);

        GridBagConstraints controlesPartidaActualConstraints = new GridBagConstraints();
        controlesPartidaActualConstraints.insets = new Insets(0, 10, 0, 10);

        panelControlesPartidaActual.add(pc.btnLimpiar, controlesPartidaActualConstraints);
        panelControlesPartidaActual.add(pcl.btnComprobar, controlesPartidaActualConstraints);
        panelControlesPartidaActual.add(pcl.panelMemoria, controlesPartidaActualConstraints);

        columna1.add(panelControlesPartidaActual, constraints);

        // 6a fila (panel puntuación)
        columna1.add(letras.puntuacion.panelPuntuacion, constraints);

        return columna1;
    }

    private JPanel crearPanelColumna2(GridBagLayout gridBagLayout) {
        JPanel columna2 = new JPanel(gridBagLayout);

        JPanel solucionario = new JPanel(gridBagLayout);

        // Se crean el label y el scrollPane
        JLabel labelSoluciones = new JLabel("Soluciones:");
        JScrollPane visorListaSolucion = new JScrollPane(letras.listaSolucion);
        visorListaSolucion.setPreferredSize(new Dimension(120, 0));

        // Se añaden al panel solucionario
        GridBagConstraints solucionarioConstraints = new GridBagConstraints();
        solucionarioConstraints.gridx = 0;

        solucionario.add(labelSoluciones, solucionarioConstraints);

        solucionarioConstraints.weighty = 1;
        solucionarioConstraints.fill = GridBagConstraints.VERTICAL;
        solucionario.add(visorListaSolucion, solucionarioConstraints);

        // Se añade todo al panel columna2
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;

        columna2.add(pcl.botonesLetras, constraints); // Botones consonante y vocal

        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        columna2.add(solucionario, constraints);

        return columna2;
    }
}
