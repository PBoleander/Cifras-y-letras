package juegos;

import javax.swing.*;
import java.awt.*;

class CellRenderer extends JLabel implements ListCellRenderer<String> {

    private final Color defaultForeground = getForeground();

    CellRenderer() {
        setOpaque(true);
        setVerticalAlignment(BOTTOM); // Servirá para dejar una línea en blanco cuando la altura sea el doble
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> jList, String text, int index,
                                                   boolean isSelected, boolean cellHasFocus) {
        int height = 16;
        if (text.length() == 1) { // Corresponde al número de letras que contienen las siguientes palabras
            text += " LETRAS";
            if (index > 0) height *= 2; // Si no es la primera línea, hará que deje una línea en blanco antes
        } else // Palabras solución
            text = "    " + text.toLowerCase();

        setPreferredSize(new Dimension(0, height));
        setText(text);

        Color background, foreground;

        if (isSelected) {
            background = new Color(80, 80, 255);
            foreground = Color.WHITE;
        } else {
            background = Color.WHITE;
            foreground = defaultForeground;
        }

        setBackground(background);
        setForeground(foreground);

        return this;
    }
}
