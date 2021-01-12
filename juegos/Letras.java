package juegos;

import java.awt.event.MouseEvent;

class Letras extends Juego {

    public Letras() {
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
    void iniciar() {
        setTiempoInicial(45); // TODO Completar con el tiempo correcto

        super.iniciar();
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
