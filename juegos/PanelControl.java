package juegos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

class PanelControl implements ActionListener {

    final JButton btnIniciar, btnLimpiar, btnPausa, btnResolver;
    final JCheckBox chkContrarreloj, chkPreguntarAntesDeResolver;

    private final Juego juego;

    PanelControl(Juego juego) {
        this.juego = juego;

        btnIniciar = new JButton("Nuevo");
        btnLimpiar = new JButton("Limpiar");
        btnPausa = new JButton("Pausar");
        btnResolver = new JButton("Resolver");
        chkContrarreloj = new JCheckBox("Contrarreloj", true);
        chkPreguntarAntesDeResolver = new JCheckBox("Preguntar si resolver", true);

        btnIniciar.setMnemonic(KeyEvent.VK_N);
        btnLimpiar.setMnemonic(KeyEvent.VK_L);
        btnPausa.setMnemonic(KeyEvent.VK_A);
        btnResolver.setMnemonic(KeyEvent.VK_S);

        btnIniciar.addActionListener(this);
        btnLimpiar.addActionListener(this);
        btnPausa.addActionListener(this);
        btnResolver.addActionListener(this);
        chkContrarreloj.addActionListener(this);

        btnPausa.setPreferredSize(new Dimension(110, 25)); // Para que quepa cuando va a poner "Reanudar"

        juego.setContrarreloj(chkContrarreloj.isSelected());
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source.equals(btnIniciar)) {
            if (!juego.haEmpezado()) juego.iniciar();

            else mostrarInformacion();

        } else if (source.equals(btnLimpiar)) {
            if (juego.haEmpezado() && juego.estaDesbloqueado()) {
                juego.limpiar();
            }

        } else if (source.equals(btnPausa)) {
            if (juego.haEmpezado()) {
                if (juego.estaPausado()) {
                    if (juego.reanudar())
                        btnPausa.setText("Pausar");

                } else {
                    if (juego.pausar())
                        btnPausa.setText("Reanudar");
                }
            }

        } else if (source.equals(btnResolver)) {
            if (!chkPreguntarAntesDeResolver.isSelected() || mostrarConfirmacion() == JOptionPane.YES_OPTION)
                juego.resolver();

        } else if (source.equals(chkContrarreloj)) {
            if (!juego.haEmpezado()) {
                juego.setContrarreloj(chkContrarreloj.isSelected());
                juego.iniciar();

            } else {
                chkContrarreloj.setSelected(!chkContrarreloj.isSelected());
                mostrarInformacion();
            }
        }
    }

    // Muestra confirmación antes de resolver la partida
    private int mostrarConfirmacion() {
        if (juego.haEmpezado() && juego.estaDesbloqueado()) {
            juego.pausar();

            Object mensaje = "Esto acabará la partida y te quedarás con el mejor resultado que has conseguido ¿Deseas" +
                    " hacerlo?";
            int respuesta =
                    JOptionPane.showConfirmDialog(null, mensaje, "Aviso", JOptionPane.YES_NO_OPTION);

            juego.reanudar();

            return respuesta;

        } else {
            return JOptionPane.CANCEL_OPTION;
        }
    }

    // Muestra información sobre empezar una nueva partida sin haber acabado la actual
    private void mostrarInformacion() {
        if (juego.haEmpezado() && juego.estaDesbloqueado()) {
            juego.pausar();
            JOptionPane.showMessageDialog(null, "Para empezar una nueva partida debes resolver.");
            juego.reanudar();
        }
    }
}
