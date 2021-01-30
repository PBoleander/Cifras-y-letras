package juegos;

import controlTiempo.MostradorTiempo;
import fichas.ContenedorFicha;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

abstract class Juego implements MouseListener, Runnable {

    enum resultado {
        DERROTA, MEJORABLE, PERFECTO
    }

    final MostradorTiempo mostradorTiempo;
    private boolean bloqueo, contrarreloj;

    ContenedorFicha contenedorBajoPuntero;

    Juego() {
        this.mostradorTiempo = new MostradorTiempo();
        this.bloqueo = true;
        this.contenedorBajoPuntero = null;
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        if (haEmpezado() && ! estaBloqueado()) {
            Object source = mouseEvent.getSource();
            if (source instanceof ContenedorFicha) {
                contenedorBajoPuntero = (ContenedorFicha) source;
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        if (haEmpezado() && ! estaBloqueado()) {
            Object source = mouseEvent.getSource();
            if (source instanceof ContenedorFicha) {
                contenedorBajoPuntero = null;
            }
        }
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
