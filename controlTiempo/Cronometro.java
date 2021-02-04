package controlTiempo;

class Cronometro implements Runnable {

    private boolean cambio, parado, pausa;
    private int decimas, segundosIniciales, segundosRestantes;

    Cronometro() {}

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PÚBLICOS **************************************************//
    //***************************************************************************************************************//

    @Override
    public void run() {
        pausa = false;
        parado = false;

        setup();

        try {
            while (segundosRestantes > 0 && !parado) {
                // Corren las décimas de segundo (para cuando se pausa no pase un segundo entero en el sleep)
                pasarDecimas();
                esperarSiPausa();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        int min = segundosRestantes / 60;
        int seg = segundosRestantes % 60;

        return (min < 10 ? "0" : "") + min + ":" + (seg < 10 ? "0" : "") + seg;
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PACKAGE ***************************************************//
    //***************************************************************************************************************//

    int getSegundosIniciales() {
        return segundosIniciales;
    }

    int getSegundosRestantes() {
        return segundosRestantes;
    }

    synchronized boolean haCambiado() throws InterruptedException {
        while (!cambio) wait();
        cambio = false;

        return true;
    }

    synchronized void parar() {
        parado = true;
        notifyAll();
    }

    synchronized void setPausa(boolean pausa) {
        this.pausa = pausa;
        notifyAll();
    }

    void setSegundosIniciales(int segundosIniciales) {
        this.segundosIniciales = segundosIniciales;
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PRIVADOS **************************************************//
    //***************************************************************************************************************//

    private synchronized void esperarSiPausa() throws InterruptedException {
        while (!parado && pausa) wait();
    }

    private void pasarDecimas() throws InterruptedException {
        Thread.sleep(100);
        decimas++;
        if (decimas == 10) {
            pasarSegundos();
            decimas = 0;
        }
    }

    private synchronized void pasarSegundos() {
        segundosRestantes--;
        cambio = true;
        notifyAll();
    }

    private synchronized void setup() {
        this.segundosRestantes = segundosIniciales;
        this.decimas = 0;
        cambio = true;
        notifyAll();
    }
}
