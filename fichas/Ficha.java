package fichas;

import javax.swing.*;
import java.awt.*;

public abstract class Ficha extends JLabel {

    public static final int ALTO = 75;
    public static final int ANCHO = 75;

    static final Color BACKGROUND = new Color(250, 175, 100);

    private static final Color FOREGROUND = Color.BLACK;
    private static final Font FUENTE = new Font(Font.DIALOG, Font.BOLD, 32);

    private boolean usada;

    Ficha() {
        super();

        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setBackground(BACKGROUND);
        setForeground(FOREGROUND);
        setFont(FUENTE);

        setPreferredSize(new Dimension(ANCHO, ALTO));

        this.usada = false;
    }

    public boolean isUsada() {
        return usada;
    }

    public void setUsada(boolean usada) {
        this.usada = usada;

        if (isUsada()) setBackground(Color.GRAY);
        else setBackground(Ficha.BACKGROUND);
    }
}
