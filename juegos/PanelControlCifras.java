package juegos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

class PanelControlCifras implements ActionListener {

    final JButton btnDeshacer;

    private final Cifras cifras;

    PanelControlCifras(Cifras cifras) {
        this.cifras = cifras;

        btnDeshacer = new JButton("Deshacer");
        btnDeshacer.addActionListener(this);
        btnDeshacer.setMnemonic(KeyEvent.VK_D);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(btnDeshacer)) {
            if (cifras.haEmpezado() && !cifras.estaBloqueado())
                cifras.deshacer();
        }
    }
}
