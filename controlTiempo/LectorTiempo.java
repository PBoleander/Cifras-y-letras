package controlTiempo;

import javax.swing.*;

class LectorTiempo extends SwingWorker<Void, Void> {

    private final Cronometro cronometro;

    LectorTiempo(Cronometro cronometro) {
        super();

        this.cronometro = cronometro;
    }

    @Override
    protected Void doInBackground() throws InterruptedException {
        setProgress(100);

        Thread crono = new Thread(cronometro);
        crono.start();

        double porcentaje;
        double segIniciales = cronometro.getSegundosIniciales();

        while (crono.isAlive()) {
            if (cronometro.haCambiado()) {
                porcentaje = cronometro.getSegundosRestantes() / segIniciales * 100;
                setProgress((int) porcentaje);
            }
        }

        return null;
    }
}
