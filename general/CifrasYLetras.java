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

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Cifras", new VisorCifras());
        tabbedPane.addTab("Letras", new VisorLetras());

        add(tabbedPane);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CifrasYLetras::new);
    }
}
