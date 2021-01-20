package juegos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VisorLetras extends JPanel implements ActionListener, ContainerListener, FocusListener, MouseListener {

    private final JButton btnIniciar;
    private final JPanel panelLetrasPuestas;
    private final Letras letras;
    private final PanelControlLetras pcl;

    public VisorLetras() {
        super();

        letras = new Letras();
        PanelControl pc = new PanelControl(letras);
        pcl = new PanelControlLetras(letras);

        btnIniciar = pc.btnIniciar;

        // Se añaden los listeners

        setFocusable(true);
        addKeyListener(letras);
        addFocusListener(this);
        btnIniciar.addActionListener(this);
        pcl.btnComprobar.addActionListener(this);
        pcl.selectorIdioma.addActionListener(this);
        pcl.selectorIdioma.addMouseListener(this);
        pcl.selectorIdioma.addFocusListener(this);

        // Empieza todo lo relacionado con el GUI

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(5, 10, 5, 10);

        // 1a columna
        JPanel columna1 = new JPanel(new GridBagLayout());

        // 2a fila (barra de progreso y botón de pausar/reanudar) (va antes de la primera en el código porque tiene
        // propiedades especiales como el fill o weight)

        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        columna1.add(letras.mostradorTiempo, constraints);

        constraints.weightx = 0;
        constraints.fill = GridBagConstraints.NONE; // Reiniciamos el valor de constraints fill al original
        constraints.gridx = 1;
        pc.btnPausa.setPreferredSize(new Dimension(105, 25));
        columna1.add(pc.btnPausa, constraints);

        // 1a fila (botones de nueva partida, resolver; checkbox de contrarreloj y selector de idioma)

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;

        JPanel panel1 = new JPanel(new GridBagLayout());
        GridBagConstraints constraints1 = new GridBagConstraints();

        constraints1.insets = new Insets(0, 10, 0, 10);

        panel1.add(pc.btnIniciar, constraints1);
        panel1.add(pc.btnResolver, constraints1);
        panel1.add(pc.chkContrarreloj, constraints1);
        panel1.add(pcl.panelIdioma, constraints1);

        columna1.add(panel1, constraints);

        // 3a y 4a filas (espacio para las fichas con las que jugar)

        constraints.gridy = 2;
        JPanel panelLetrasDisponibles = new JPanel(new GridLayout(1, Letras.numeroLetras, 10, 0));
        for (int i = 0; i < Letras.numeroLetras; i++) {
            letras.letrasDisponibles[i].addContainerListener(this);
            panelLetrasDisponibles.add(letras.letrasDisponibles[i]);
        }

        columna1.add(panelLetrasDisponibles, constraints);

        constraints.gridy++;
        panelLetrasPuestas = new JPanel(new GridLayout(1, Letras.numeroLetras, 10, 0));
        for (int i = 0; i < Letras.numeroLetras; i++) {
            panelLetrasPuestas.add(letras.letrasPuestas[i]);
        }

        columna1.add(panelLetrasPuestas, constraints);

        // 5a fila (botones más relacionados con el juego en sí: limpiar intentos, comprobar palabra y memoria)

        JPanel panel2 = new JPanel(new GridBagLayout());
        GridBagConstraints constraints2 = new GridBagConstraints();

        constraints2.insets = new Insets(0, 10, 0, 10);

        panel2.add(pc.btnLimpiar, constraints2);
        panel2.add(pcl.btnComprobar, constraints2);
        panel2.add(pcl.panelMemoria, constraints2);

        constraints.gridy++;
        columna1.add(panel2, constraints);

        constraints.gridy++;
        columna1.add(letras.puntuacion.panelPuntuacion, constraints);

        // 2a columna
        JPanel columna2 = new JPanel(new GridBagLayout());
        GridBagConstraints constraints3 = new GridBagConstraints();
        constraints3.insets = new Insets(10, 10, 10, 10);

        JScrollPane visorListaSolucion = new JScrollPane(letras.listaSolucion);
        visorListaSolucion.setPreferredSize(new Dimension(120, 300));

        // Se añaden el panel de los botones consonante y vocal además del visor de la lista de soluciones
        columna2.add(pcl.botonesLetras, constraints3);
        constraints3.gridy = 1;
        columna2.add(visorListaSolucion, constraints3);

        // Se añaden las dos columnas a this
        add(columna1);
        add(columna2);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source.equals(btnIniciar)) {
            // Para que primero se ejecute el otro propósito de btnIniciar (el de iniciar nueva partida)
            SwingUtilities.invokeLater(this::actualizarLabelMemoria);

        } else if (source.equals(pcl.btnComprobar)) {
            SwingUtilities.invokeLater(() -> {
                if (letras.haEmpezado()) {
                    if (letras.resultadoComprobacion)
                        panelLetrasPuestas.setBackground(Color.GREEN);
                    else
                        panelLetrasPuestas.setBackground(Color.RED);
                }
            });
        }

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
        // TODO Hay un bug que no sé cómo arreglar: si se clica la flecha del desplegable no se devuelve el foco a this
        if (mouseEvent.getSource().equals(pcl.selectorIdioma)) {
            if (!pcl.selectorIdioma.isPopupVisible()) {
                if (!isFocusOwner())
                    requestFocusInWindow();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    private void actualizarLabelMemoria() {
        String palabraMemorizada = letras.getPalabraMemorizada();
        if (palabraMemorizada.length() > 0) {
            pcl.labelMemoria.setText("Memoria: " + palabraMemorizada + " (" + palabraMemorizada.length() + ")");
        } else {
            pcl.labelMemoria.setText("Memoria:");
        }
    }
}
