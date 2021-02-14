package general;

import juegos.VisorCifras;
import juegos.VisorLetras;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

class CifrasYLetras extends JFrame implements ChangeListener {

    private final JTabbedPane tabbedPane;
    private final VisorCifras visorCifras;
    private final VisorLetras visorLetras;

    CifrasYLetras() {
        super();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Cifras y letras");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/iconos/blackboard.png")));

        visorCifras = new VisorCifras();
        visorLetras = new VisorLetras();

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Cifras", visorCifras);
        tabbedPane.addTab("Letras", visorLetras);

        tabbedPane.addChangeListener(this);

        add(tabbedPane);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        Component activeTab = tabbedPane.getSelectedComponent();

        if (activeTab.equals(visorLetras)) {
            visorLetras.requestFocusInWindow();
            visorCifras.cifras.pausar();
            visorLetras.letras.reanudar();

        } else if (activeTab.equals(visorCifras)) {
            visorLetras.letras.pausar();
            visorCifras.cifras.reanudar();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CifrasYLetras::new);
    }
}
