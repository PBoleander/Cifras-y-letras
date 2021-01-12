package juegos;

import controlTiempo.MostradorTiempo;

import java.awt.event.MouseListener;

abstract class Juego implements MouseListener {

    final MostradorTiempo mostradorTiempo;
    private boolean bloqueo, contrarreloj;

    Juego() {
        this.mostradorTiempo = new MostradorTiempo();
        this.bloqueo = false;
    }

    boolean estaBloqueado() {
        return bloqueo;
    }

    boolean haEmpezado() {
        return mostradorTiempo.haEmpezado();
    }

    void iniciar() {
        bloqueo = false;
        mostradorTiempo.iniciar(contrarreloj);
    }

    void pausar() {
        if (contrarreloj) {
            bloqueo = true;
            mostradorTiempo.setPausa(true);
        }
    }

    void reanudar() {
        if (contrarreloj) {
            bloqueo = false;
            mostradorTiempo.setPausa(false);
        }
    }

    void resolver() {
        bloqueo = true;
        mostradorTiempo.parar(contrarreloj);
    }

    void setContrarreloj(boolean contrarreloj) {
        this.contrarreloj = contrarreloj;
    }

    void setTiempoInicial(int segundos) {
        mostradorTiempo.setSegundosIniciales(segundos);
    }
}
