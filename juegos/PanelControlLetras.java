package juegos;

import fichas.Letra;
import general.Idioma;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class PanelControlLetras implements ActionListener {

    final JButton btnComprobar;
    final JPanel botonesLetras, panelIdioma, panelMemoria;
    final JLabel labelMemoria;

    private final JButton btnConsonante, btnMemorizar, btnRecuperar, btnVocal;
    private final JComboBox<Idioma> selectorIdioma;
    private final Letras letras;

    private Idioma anteriorIdioma;

    PanelControlLetras(Letras letras) {
        this.letras = letras;

        botonesLetras = new JPanel(new GridLayout(1, 2, 10, 0));
        panelIdioma = new JPanel();
        panelMemoria = new JPanel(new GridBagLayout());

        btnComprobar = new JButton("Comprobar");
        btnConsonante = new JButton("Consonante");
        btnMemorizar = new JButton("Memorizar");
        btnRecuperar = new JButton("Recuperar");
        btnVocal = new JButton("Vocal");
        labelMemoria = new JLabel("Memoria:");
        JLabel labelIdioma = new JLabel("Idioma:");
        selectorIdioma = new JComboBox<>(Idioma.values());

        btnComprobar.addActionListener(this);
        btnConsonante.addActionListener(this);
        btnMemorizar.addActionListener(this);
        btnRecuperar.addActionListener(this);
        btnVocal.addActionListener(this);
        selectorIdioma.addActionListener(this);

        labelIdioma.setHorizontalAlignment(SwingConstants.RIGHT);
        labelMemoria.setPreferredSize(new Dimension(200, 10));

        botonesLetras.add(btnConsonante);
        botonesLetras.add(btnVocal);

        panelIdioma.add(labelIdioma);
        panelIdioma.add(selectorIdioma);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 0, 10);

        panelMemoria.add(btnMemorizar, c);
        panelMemoria.add(btnRecuperar, c);
        panelMemoria.add(labelMemoria, c);

        anteriorIdioma = (Idioma) selectorIdioma.getSelectedItem();
        letras.setIdioma(anteriorIdioma);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source.equals(btnComprobar)) {
            // TODO

        } else if (source.equals(btnConsonante)) {
            letras.sacar(Letra.Tipo.CONSONANTE);

        } else if (source.equals(btnMemorizar)) {
            if (letras.haEmpezado() && !letras.estaBloqueado()) {
                letras.memorizar();
                actualizarLabelMemoria();
            }

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

    private void actualizarLabelMemoria() {
        String palabraMemorizada = letras.getPalabraMemorizada();
        if (palabraMemorizada.length() > 0) {
            labelMemoria.setText("Memoria: " + palabraMemorizada + " (" + palabraMemorizada.length() + ")");
        } else {
            labelMemoria.setText("Memoria:");
        }
    }
}
