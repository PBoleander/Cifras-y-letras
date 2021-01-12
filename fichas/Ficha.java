package fichas;

import javax.swing.*;
import java.awt.*;

abstract class Ficha extends JLabel {

    public static final int ALTO = 75;
    public static final int ANCHO = 75;

    private static final Color BACKGROUND = new Color(250, 175, 100);
    private static final Color FOREGROUND = Color.BLACK;
    private static final Font FUENTE = new Font(Font.DIALOG, Font.BOLD, 32);

    Ficha() {
        super();

        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setBackground(BACKGROUND);
        setForeground(FOREGROUND);
        setFont(FUENTE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        setPreferredSize(new Dimension(ANCHO, ALTO));
    }
}
