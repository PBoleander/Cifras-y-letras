package juegos;

import java.awt.event.MouseEvent;

class Cifras extends Juego {

    Cifras() {
        super();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (!estaBloqueado()) {

        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

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

    @Override
    void iniciar() {
        setTiempoInicial(60);

        super.iniciar();
        new Thread(this).start();
    }

    @Override
    void limpiar() {

    }

    @Override
    void pausar() {
        super.pausar();

        // TODO Mostrar mensaje
    }

    @Override
    void reanudar() {
        super.reanudar();

        // TODO Quitar mensaje pausa
    }

    @Override
    void resolver() {
        super.resolver();
    }
}
