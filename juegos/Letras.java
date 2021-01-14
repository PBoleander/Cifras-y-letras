package juegos;

import fichas.ContenedorFicha;
import fichas.Letra;

import java.awt.event.MouseEvent;

class Letras extends Juego {

    final ContenedorFicha[] letrasDisponibles, letrasPuestas;
    final int numeroLetras = 9;

    private ContenedorFicha contenedorBajoPuntero;
    private int numeroLetrasSacadas;
    private Letra letraArrastrada;

    Letras() {
        super();

        this.letrasDisponibles = new ContenedorFicha[numeroLetras];
        this.letrasPuestas = new ContenedorFicha[numeroLetras];

        for (int i = 0; i < numeroLetras; i++) {
            letrasDisponibles[i] = new ContenedorFicha(null);
            letrasDisponibles[i].addMouseListener(this);

            letrasPuestas[i] = new ContenedorFicha(null);
            letrasPuestas[i].addMouseListener(this);
        }

        this.contenedorBajoPuntero = null;
        this.numeroLetrasSacadas = 0;
        this.letraArrastrada = null;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (! estaBloqueado() && contenedorBajoPuntero != null) {
            if (contenedorBajoPuntero.estaOcupado()) {
                assert contenedorBajoPuntero.getFicha() instanceof Letra;
                Letra letraClicada = (Letra) contenedorBajoPuntero.getFicha();

                if (esLetraDisponible(contenedorBajoPuntero)) {
                    if (! letraClicada.isUsada()) {
                        usar(letraClicada);
                    }
                } else { // Es letra puesta
                    letraClicada.setUsada(false);
                    contenedorBajoPuntero.setFicha(null);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (! estaBloqueado() && contenedorBajoPuntero != null) {
            if (contenedorBajoPuntero.estaOcupado()) {
                if (! esLetraDisponible(contenedorBajoPuntero)) { // Es letra puesta
                    assert contenedorBajoPuntero.getFicha() instanceof Letra;
                    letraArrastrada = (Letra) contenedorBajoPuntero.getFicha();
                    contenedorBajoPuntero.setFicha(null);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 0) {
            if (!estaBloqueado() && contenedorBajoPuntero != null) {
                if (!contenedorBajoPuntero.estaOcupado()) {
                    if (!esLetraDisponible(contenedorBajoPuntero)) { // Es letra puesta
                        if (letraArrastrada != null) {
                            contenedorBajoPuntero.setFicha(letraArrastrada);
                            letraArrastrada = null;
                        }
                    }
                }
            }
        } else { // Se ha hecho clic
            letraArrastrada = null;
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        if (! estaBloqueado()) {
            Object source = mouseEvent.getSource();
            if (source instanceof ContenedorFicha) {
                contenedorBajoPuntero = (ContenedorFicha) source;
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        if (! estaBloqueado()) {
            Object source = mouseEvent.getSource();
            if (source instanceof ContenedorFicha) {
                contenedorBajoPuntero = null;
            }
        }
    }

    @Override
    void iniciar() {
        setTiempoInicial(45); // TODO Completar con el tiempo correcto

        for (int i = 0; i < numeroLetras; i++) {
            letrasDisponibles[i].setFicha(null);
            letrasPuestas[i].setFicha(null);
        }
    }

    @Override
    void limpiar() {
        for (ContenedorFicha contenedorFicha: letrasPuestas) {
            if (contenedorFicha.estaOcupado()) {
                contenedorFicha.setFicha(null);
            }
        }
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

    void sacar(Letra.Tipo tipo) {
        if (numeroLetrasSacadas < numeroLetras) {
            letrasDisponibles[numeroLetrasSacadas].setFicha(new Letra(tipo));

            numeroLetrasSacadas++;
        }

        if (numeroLetrasSacadas == numeroLetras)
            super.iniciar();
    }

    private boolean esLetraDisponible(ContenedorFicha contenedorFicha) {
        int i = 0;
        while (i < numeroLetras && ! letrasDisponibles[i].equals(contenedorFicha))
            i++;

        return i < numeroLetras;
    }

    private void usar(Letra letra) {
        int i = 0;
        while (i < numeroLetras && letrasPuestas[i].estaOcupado())
            i++;

        if (i < numeroLetras) {
            letrasPuestas[i].setFicha(letra);
            letra.setUsada(true);
        }
    }
}
