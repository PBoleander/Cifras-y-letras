package general;

import juegos.VisorCifras;
import juegos.VisorLetras;

import javax.swing.*;
import java.awt.*;

class CifrasYLetras extends JFrame {

    CifrasYLetras() {
        super();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Cifras y letras");
        setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/iconos/blackboard.png")));

        VisorLetras visorLetras = new VisorLetras();

        JTabbedPane tabbedPane = new JTabbedPane();
        //tabbedPane.addTab("Cifras", new VisorCifras());
        tabbedPane.addTab("Letras", visorLetras);

        add(tabbedPane);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        visorLetras.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CifrasYLetras::new);
    }
}
