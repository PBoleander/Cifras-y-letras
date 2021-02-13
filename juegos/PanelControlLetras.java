package juegos;

import fichas.Letra;
import general.Idioma;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

class PanelControlLetras implements ActionListener {

    final JButton btnComprobar;
    final JPanel botonesLetras, panelIdioma, panelMemoria;
    final JLabel labelMemoria;

    private final JButton btnConsonante, btnMemorizar, btnRecuperar, btnVocal;
    final JComboBox<Idioma> selectorIdioma;
    private final Letras letras;

    private Idioma anteriorIdioma;

    PanelControlLetras(Letras letras) {
        this.letras = letras;

        botonesLetras = new JPanel(new GridLayout(2, 1, 0, 10));
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

        btnComprobar.setMnemonic(KeyEvent.VK_P);
        btnConsonante.setMnemonic(KeyEvent.VK_C);
        btnMemorizar.setMnemonic(KeyEvent.VK_M);
        btnRecuperar.setMnemonic(KeyEvent.VK_R);
        btnVocal.setMnemonic(KeyEvent.VK_V);

        btnComprobar.addActionListener(this);
        btnConsonante.addActionListener(this);
        btnMemorizar.addActionListener(this);
        btnRecuperar.addActionListener(this);
        btnVocal.addActionListener(this);
        selectorIdioma.addActionListener(this);

        labelIdioma.setHorizontalAlignment(SwingConstants.RIGHT);
        // Mejor de este modo ya que si se deja a GridBagLayout ajustarlo y el nº de letras fuese menor podría no
        // caber la palabra más larga (poco probable que se cambien el nº de letras pero más vale prevenir)
        labelMemoria.setPreferredSize(new Dimension(240, 15));

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
            letras.comprobar();

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
            if (!letras.haEmpezado() && letras.numeroLetrasSacadas == 0) {
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
