package juegos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VisorLetras extends JPanel implements ActionListener, FocusListener, MouseListener {

    private final JButton btnIniciar;
    private final Letras letras;
    private final PanelControlLetras pcl;

    public VisorLetras() {
        super(new GridBagLayout());

        letras = new Letras();
        PanelControl pc = new PanelControl(letras);
        pcl = new PanelControlLetras(letras);

        btnIniciar = pc.btnIniciar;

        // Se añaden los listeners

        setFocusable(true);
        addKeyListener(letras);
        addFocusListener(this);
        btnIniciar.addActionListener(this);
        pcl.selectorIdioma.addFocusListener(this);
        pcl.selectorIdioma.addActionListener(this);
        pcl.selectorIdioma.addMouseListener(this);

        // Empieza todo lo relacionado con el GUI

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(5, 10, 5, 10);

        // 2a fila (barra de progreso y botón de pausar/reanudar) (va antes de la primera en el código porque tiene
        // propiedades especiales como el fill o weight)

        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        add(letras.mostradorTiempo, constraints);

        constraints.weightx = 0;
        constraints.fill = GridBagConstraints.NONE; // Reiniciamos el valor de constraints fill al original
        constraints.gridx = 1;
        pc.btnPausa.setPreferredSize(new Dimension(105, 25));
        add(pc.btnPausa, constraints);

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

        add(panel1, constraints);

        // 3a fila (botones de consonante y vocal) TODO Los cambiaré a la 2a columna cuando ponga la lista de palabras

        constraints.gridy = 2;

        add(pcl.botonesLetras, constraints);

        // 4a y 5a filas (espacio para las fichas con las que jugar)

        constraints.gridy++;
        JPanel panelLetrasDisponibles = new JPanel(new GridLayout(1, letras.numeroLetras, 10, 0));
        for (int i = 0; i < letras.numeroLetras; i++) {
            panelLetrasDisponibles.add(letras.letrasDisponibles[i]);
        }

        add(panelLetrasDisponibles, constraints);

        constraints.gridy++;
        JPanel panelLetrasPuestas = new JPanel(new GridLayout(1, letras.numeroLetras, 10, 0));
        for (int i = 0; i < letras.numeroLetras; i++) {
            panelLetrasPuestas.add(letras.letrasPuestas[i]);
        }

        add(panelLetrasPuestas, constraints);

        // 6a fila (botones más relacionados con el juego en sí: limpiar intentos, comprobar palabra y memoria)

        JPanel panel2 = new JPanel(new GridBagLayout());
        GridBagConstraints constraints2 = new GridBagConstraints();

        constraints2.insets = new Insets(0, 10, 0, 10);

        panel2.add(pc.btnLimpiar, constraints2);
        panel2.add(pcl.btnComprobar, constraints2);
        panel2.add(pcl.panelMemoria, constraints2);

        constraints.gridy++;
        add(panel2, constraints);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(btnIniciar)) {
            // Para que primero se ejecute el otro propósito de btnIniciar (el de iniciar nueva partida)
            SwingUtilities.invokeLater(this::actualizarLabelMemoria);
        }

        // Cuando el selector de idioma ha hecho su trabajo devuelve el foco al panel
        if (!isFocusOwner())
            SwingUtilities.invokeLater(this::requestFocusInWindow);
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
