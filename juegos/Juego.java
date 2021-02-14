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

    private boolean bloqueo, contrarreloj, pausado;

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
        if (haEmpezado() && estaDesbloqueado()) {
            Object source = mouseEvent.getSource();
            if (source instanceof ContenedorFicha) {
                contenedorBajoPuntero = (ContenedorFicha) source;
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        if (haEmpezado() && estaDesbloqueado()) {
            Object source = mouseEvent.getSource();
            if (source instanceof ContenedorFicha) {
                contenedorBajoPuntero = null;
            }
        }
    }

    @Override
    // Resuelve la partida al acabar el tiempo
    public void run() {
        try {
            mostradorTiempo.esperarHastaFin();
            resolver();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PACKAGE ***************************************************//
    //***************************************************************************************************************//

    abstract void limpiar();

    boolean estaDesbloqueado() {
        return !bloqueo;
    }

    public boolean estaPausado() {
        return pausado;
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

    boolean pausar() {
        if (contrarreloj && estaDesbloqueado() && !estaPausado()) {
            bloqueo = true;
            pausado = true;
            mostradorTiempo.setPausa(true);

            return true;
        }
        return false;
    }

    boolean reanudar() {
        if (contrarreloj && estaPausado()) {
            bloqueo = false;
            pausado = false;
            mostradorTiempo.setPausa(false);

            return true;
        }
        return false;
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
