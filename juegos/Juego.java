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

    ContenedorFicha contenedorBajoPuntero;
    resultado resultadoPartida;

    private boolean bloqueo, contrarreloj;

    Juego() {
        this.mostradorTiempo = new MostradorTiempo();
        this.bloqueo = true;
        this.contenedorBajoPuntero = null;
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PÚBLICOS **************************************************//
    //***************************************************************************************************************//

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
    // Resuelve la partida al acabar el tiempo
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

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PACKAGE ***************************************************//
    //***************************************************************************************************************//

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
