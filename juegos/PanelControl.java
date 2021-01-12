package juegos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class PanelControl extends JPanel implements ActionListener {

    private final JButton btnIniciar, btnPausa, btnResolver;
    private final JCheckBox chkContrarreloj;
    private final Juego juego;

    PanelControl(Juego juego) {
        super(new GridLayout(1, 5, 10, 0));

        this.juego = juego;

        btnIniciar = new JButton("Nuevo");
        btnPausa = new JButton("Pausar");
        btnResolver = new JButton("Resolver");
        // TODO Añadir botón de limpiar intentos

        chkContrarreloj = new JCheckBox("Contrarreloj", true);

        btnIniciar.addActionListener(this);
        btnPausa.addActionListener(this);
        btnResolver.addActionListener(this);
        chkContrarreloj.addActionListener(this);

        add(btnPausa);
        add(btnResolver);
        add(btnIniciar);
        add(chkContrarreloj);

        juego.setContrarreloj(chkContrarreloj.isSelected());
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source.equals(btnIniciar)) {
            if (!juego.haEmpezado()) juego.iniciar();

            else if (mostrarConfirmacion() == JOptionPane.YES_OPTION) juego.resolver();

        } else if (source.equals(btnPausa)) {
            if (juego.haEmpezado()) {
                if (juego.estaBloqueado()) {
                    btnPausa.setText("Pausar");
                    juego.reanudar();

                } else {
                    juego.pausar();
                    btnPausa.setText("Reanudar");
                }
            }

        } else if (source.equals(btnResolver)) {
            if (juego.haEmpezado() && !juego.estaBloqueado()) juego.resolver();

        } else if (source.equals(chkContrarreloj)) {
            if (!juego.haEmpezado()) {
                juego.setContrarreloj(chkContrarreloj.isSelected());
                juego.iniciar();

            } else if (mostrarConfirmacion() == JOptionPane.YES_OPTION) {
                juego.resolver();
                juego.setContrarreloj(chkContrarreloj.isSelected());

            } else {
                chkContrarreloj.setSelected(!chkContrarreloj.isSelected());
            }
        }
    }

    private int mostrarConfirmacion() {
        if (juego.haEmpezado() && !juego.estaBloqueado()) {
            juego.pausar();

            Object mensaje = "Antes de empezar una nueva partida debes rendirte y resolver. ¿Deseas hacerlo?";
            int respuesta =
                    JOptionPane.showConfirmDialog(null, mensaje, "Aviso", JOptionPane.YES_NO_OPTION);

            juego.reanudar();

            return respuesta;

        } else {
            return JOptionPane.CANCEL_OPTION;
        }
    }
}
