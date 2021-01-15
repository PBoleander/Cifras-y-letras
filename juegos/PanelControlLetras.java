package juegos;

import fichas.Letra;
import general.Idioma;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class PanelControlLetras extends JPanel implements ActionListener {

    private final JButton btnConsonante, btnMemorizar, btnRecuperar, btnVocal;
    private final JComboBox<Idioma> selectorIdioma;
    private final Letras letras;

    private Idioma anteriorIdioma;

    PanelControlLetras(Letras letras) {
        super(new GridLayout(1, 6, 10, 0));

        this.letras = letras;

        btnConsonante = new JButton("Consonante");
        btnMemorizar = new JButton("Memorizar");
        btnRecuperar = new JButton("Recuperar");
        btnVocal = new JButton("Vocal");
        JLabel labelIdioma = new JLabel("Idioma:");
        selectorIdioma = new JComboBox<>(Idioma.values());

        btnConsonante.addActionListener(this);
        btnMemorizar.addActionListener(this);
        btnRecuperar.addActionListener(this);
        btnVocal.addActionListener(this);
        selectorIdioma.addActionListener(this);

        labelIdioma.setHorizontalAlignment(SwingConstants.RIGHT);

        add(btnConsonante);
        add(btnVocal);
        add(btnMemorizar);
        add(btnRecuperar);
        add(labelIdioma);
        add(selectorIdioma);

        anteriorIdioma = (Idioma) selectorIdioma.getSelectedItem();
        letras.setIdioma(anteriorIdioma);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source.equals(btnConsonante)) {
            letras.sacar(Letra.Tipo.CONSONANTE);

        } else if (source.equals(btnMemorizar)) {
            if (letras.haEmpezado() && !letras.estaBloqueado())
                letras.memorizar();

        } else if (source.equals(btnRecuperar)) {
            if (letras.haEmpezado() && !letras.estaBloqueado())
                letras.recuperarMemoria();

        } else if (source.equals(btnVocal)) {
            letras.sacar(Letra.Tipo.VOCAL);

        } else if (source.equals(selectorIdioma)) {
            if (!letras.haEmpezado()) {
                anteriorIdioma = (Idioma) selectorIdioma.getSelectedItem();
                letras.setIdioma(anteriorIdioma);
            } else {
                selectorIdioma.setSelectedItem(anteriorIdioma);
            }

        }
    }
}
