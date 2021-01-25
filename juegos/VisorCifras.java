package juegos;

import javax.swing.*;
import java.awt.*;

public class VisorCifras extends JPanel {

    public VisorCifras() {
        super(new GridBagLayout());

        Cifras cifras = new Cifras();
        PanelControl pc = new PanelControl(cifras);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(5, 10, 5, 10);

        add(cifras.cifraObjetivo, constraints);

        constraints.gridx = 1;
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 5, 5));
        panelBotones.add(pc.btnIniciar);
        panelBotones.add(pc.btnLimpiar);
        add(panelBotones, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        add(cifras.mostradorTiempo, constraints);

        constraints.fill = GridBagConstraints.NONE; // Reiniciamos el valor de constraints fill al original
        constraints.gridwidth = 1;
        constraints.gridy++;

        JPanel panelCifras = new JPanel(new GridLayout(2, 3, 5, 5));
        for (int i = 0; i < Cifras.numeroCifras; i++) {
            panelCifras.add(cifras.cifrasDisponibles[i]);
        }

        JPanel panelOperadores = new JPanel(new GridLayout(2, 2, 5, 5));
        for (int i = 0; i < 4; i++) {
            panelOperadores.add(cifras.operadores[i]);
        }

        JPanel panelOperaciones = new JPanel(new GridLayout(5, 1, 5, 5));
        for (int i = 0; i < 5; i++) {
            panelOperaciones.add(cifras.operacionesRealizadas[i]);
        }

        add(panelCifras, constraints);
        constraints.gridx = 1;
        add(panelOperadores, constraints);
        constraints.gridy++;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        add(panelOperaciones, constraints);
        constraints.gridwidth = 1;
    }
}
