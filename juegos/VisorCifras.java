package juegos;

import javax.swing.*;
import java.awt.*;

public class VisorCifras extends JPanel {

    public VisorCifras() {
        super(new GridBagLayout());

        Cifras cifras = new Cifras();

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(5, 10, 5, 10);

        add(new PanelControl(cifras), constraints);
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(cifras.mostradorTiempo, constraints);

        constraints.fill = GridBagConstraints.NONE; // Reiniciamos el valor de constraints fill al original
    }
}
