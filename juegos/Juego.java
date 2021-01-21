package juegos;

import controlTiempo.MostradorTiempo;

import java.awt.event.MouseListener;

abstract class Juego implements MouseListener, Runnable {

    final MostradorTiempo mostradorTiempo;
    private boolean bloqueo, contrarreloj;

    Juego() {
        this.mostradorTiempo = new MostradorTiempo();
        this.bloqueo = true;
    }

    @Override
    public void run() {
        while (haEmpezado()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        resolver();
    }

    abstract void limpiar();

    boolean estaBloqueado() {
        return bloqueo;
    }

    boolean haEmpezado() {
        return mostradorTiempo.haEmpezado();
    }

    void iniciar() {
        bloqueo = false;
        mostradorTiempo.iniciar(contrarreloj);
        if (contrarreloj)
            new Thread(this).start();
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
