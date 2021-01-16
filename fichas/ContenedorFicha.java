package fichas;

import javax.swing.*;
import java.awt.*;

public class ContenedorFicha extends JPanel {

    private static final GridLayout gl = new GridLayout();

    private Ficha ficha;

    public ContenedorFicha(Ficha ficha) {
        super(gl);

        setPreferredSize(new Dimension(Ficha.ANCHO, Ficha.ALTO));
        setOpaque(false);
        setBorder(BorderFactory.createLoweredBevelBorder());

        setFicha(ficha);
    }

    public boolean estaOcupado() {
        return ficha != null;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public void setFicha(Ficha ficha) {
        removeAll();

        this.ficha = ficha;
        if (ficha != null)
            add(ficha);

        revalidate();
        repaint();
    }
}
