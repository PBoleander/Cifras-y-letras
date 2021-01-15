package juegos;

import javax.swing.*;
import java.awt.*;

public class VisorLetras extends JPanel {

    public VisorLetras() {
        super(new GridBagLayout());

        Letras letras = new Letras();

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(5, 10, 5, 10);

        add(new PanelControl(letras), constraints);

        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(letras.mostradorTiempo, constraints);

        constraints.fill = GridBagConstraints.NONE; // Reiniciamos el valor de constraints fill al original
        constraints.gridy = 2;
        add(new PanelControlLetras(letras), constraints);

        constraints.gridy = 3;
        JPanel panelLetrasDisponibles = new JPanel(new GridLayout(1, letras.numeroLetras, 10, 0));
        for (int i = 0; i < letras.numeroLetras; i++) {
            panelLetrasDisponibles.add(letras.letrasDisponibles[i]);
        }

        add(panelLetrasDisponibles, constraints);

        constraints.gridy = 4;
        JPanel panelLetrasPuestas = new JPanel(new GridLayout(1, letras.numeroLetras, 10, 0));
        for (int i = 0; i < letras.numeroLetras; i++) {
            panelLetrasPuestas.add(letras.letrasPuestas[i]);
        }

        add(panelLetrasPuestas, constraints);
    }
}
