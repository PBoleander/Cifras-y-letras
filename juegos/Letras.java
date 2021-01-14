package juegos;

import fichas.ContenedorFicha;
import fichas.Letra;

import java.awt.event.MouseEvent;
import java.util.Arrays;

class Letras extends Juego {

    final ContenedorFicha[] letrasDisponibles, letrasPuestas;
    final int numeroLetras = 9;
    final int[] memoria;

    int longitudMemoria;

    private ContenedorFicha contenedorBajoPuntero;
    private int numeroLetrasSacadas;
    private Letra letraArrastrada;

    Letras() {
        super();

        this.memoria = new int[numeroLetras];
        this.letrasDisponibles = new ContenedorFicha[numeroLetras];
        this.letrasPuestas = new ContenedorFicha[numeroLetras];

        for (int i = 0; i < numeroLetras; i++) {
            letrasDisponibles[i] = new ContenedorFicha(null);
            letrasDisponibles[i].addMouseListener(this);

            letrasPuestas[i] = new ContenedorFicha(null);
            letrasPuestas[i].addMouseListener(this);
        }

        this.contenedorBajoPuntero = null;
        this.letraArrastrada = null;

        iniciar();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (! estaBloqueado() && contenedorBajoPuntero != null) {
            if (contenedorBajoPuntero.estaOcupado()) {
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
                    letraArrastrada = (Letra) contenedorBajoPuntero.getFicha();
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 0) {
            if (!estaBloqueado() && contenedorBajoPuntero != null) {
                if (!esLetraDisponible(contenedorBajoPuntero)) { // Es letra puesta
                    if (!contenedorBajoPuntero.estaOcupado()) {
                        if (letraArrastrada != null) {
                            contenedorBajoPuntero.setFicha(letraArrastrada);
                            ((ContenedorFicha) mouseEvent.getSource()).setFicha(null);
                            letraArrastrada = null;
                        }
                    } else {
                        if (letraArrastrada != null) {
                            ((ContenedorFicha) mouseEvent.getSource()).setFicha(letraArrastrada);
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
        Arrays.fill(memoria, -1);
        numeroLetrasSacadas = 0;

        for (int i = 0; i < numeroLetras; i++) {
            letrasDisponibles[i].setFicha(null);
            letrasPuestas[i].setFicha(null);
        }
    }

    @Override
    void limpiar() {
        for (ContenedorFicha contenedorFicha: letrasPuestas) {
            if (contenedorFicha.estaOcupado()) {
                ((Letra) contenedorFicha.getFicha()).setUsada(false);
                contenedorFicha.setFicha(null);
            }
        }
    }

    void memorizar() {
        Letra letra;
        Arrays.fill(memoria, -1);
        longitudMemoria = 0;

        for (ContenedorFicha contenedorFicha: letrasPuestas) {
            if (contenedorFicha.estaOcupado()) {
                letra = (Letra) contenedorFicha.getFicha();
                if (letra != null) {
                    memorizar(letra);
                    longitudMemoria++;
                }
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

    void recuperarMemoria() {
        limpiar();

        for (int i = 0; i < longitudMemoria; i++) {
            usar((Letra) letrasDisponibles[memoria[i]].getFicha());
        }
    }

    @Override
    void resolver() {
        super.resolver();
    }

    void sacar(Letra.Tipo tipo) {
        if (numeroLetrasSacadas < numeroLetras) {
            letrasDisponibles[numeroLetrasSacadas].setFicha(new Letra(tipo));

            numeroLetrasSacadas++;

            if (numeroLetrasSacadas == numeroLetras)
                super.iniciar();
        }
    }

    private void memorizar(Letra letra) {
        int i = 0;
        while (i < numeroLetras && memoria[i] != -1)
            i++;

        memoria[i] = quePosicionOcupa(letra);
    }

    private boolean esLetraDisponible(ContenedorFicha contenedorFicha) {
        int i = 0;
        while (i < numeroLetras && ! letrasDisponibles[i].equals(contenedorFicha))
            i++;

        return i < numeroLetras;
    }

    private int quePosicionOcupa(Letra letra) {
        for (int i = 0; i < numeroLetras; i++) {
            if (((Letra) letrasDisponibles[i].getFicha()).equals(letra)) {
                return i;
            }
        }
        return -1; // Aquí no debe llegar
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
