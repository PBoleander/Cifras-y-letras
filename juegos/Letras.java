package juegos;

import fichas.ContenedorFicha;
import fichas.Letra;
import general.Idioma;

import java.awt.event.MouseEvent;
import java.util.Arrays;

class Letras extends Juego {

    final ContenedorFicha[] letrasDisponibles, letrasPuestas;
    final int numeroLetras = 9;
    final int[] memoria;

    int longitudMemoria;

    private final Letra[] letrasDisponiblesAux;
    private ContenedorFicha contenedorBajoPuntero;
    private Idioma idioma;
    private int numeroLetrasSacadas;
    private Letra letraArrastrada;

    Letras() {
        super();

        this.memoria = new int[numeroLetras];
        this.letrasDisponibles = new ContenedorFicha[numeroLetras];
        this.letrasDisponiblesAux = new Letra[numeroLetras];
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
        if (haEmpezado() && ! estaBloqueado() && contenedorBajoPuntero != null) {
            if (contenedorBajoPuntero.estaOcupado()) {
                Letra letraClicada = (Letra) contenedorBajoPuntero.getFicha();

                if (esLetraDisponible(contenedorBajoPuntero)) {
                    if (! letraClicada.isUsada()) {
                        usar(letraClicada);
                    }
                } else { // Es letra puesta
                    desusar(letraClicada);
                    contenedorBajoPuntero.setFicha(null);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (haEmpezado() && ! estaBloqueado() && contenedorBajoPuntero != null) {
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
            if (haEmpezado() && !estaBloqueado() && contenedorBajoPuntero != null) {
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

    String getPalabraMemorizada() {
        if (longitudMemoria == 0) return "";
        else {
            StringBuilder palabraMemorizada = new StringBuilder();

            for (int i = 0; i < longitudMemoria; i++) {
                palabraMemorizada.append(letrasDisponiblesAux[memoria[i]].getValor());
            }

            return palabraMemorizada.toString();
        }
    }

    @Override
    void iniciar() {
        setTiempoInicial(idioma == Idioma.INGLES ? 60 : 45);
        Arrays.fill(memoria, -1);
        longitudMemoria = 0;
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
                desusar((Letra) contenedorFicha.getFicha());
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
            usar(letrasDisponiblesAux[memoria[i]]);
        }
    }

    @Override
    void resolver() {
        super.resolver();
    }

    void sacar(Letra.Tipo tipo) {
        if (numeroLetrasSacadas < numeroLetras) {
            Letra letra = new Letra(tipo);
            letrasDisponibles[numeroLetrasSacadas].setFicha(letra);
            letrasDisponiblesAux[numeroLetrasSacadas] = letra;

            numeroLetrasSacadas++;

            if (numeroLetrasSacadas == numeroLetras)
                super.iniciar();
        }
    }

    void setIdioma(Idioma idioma) {
        this.idioma = idioma;
        Letra.generador.setIdioma(idioma);
    }

    private void desusar(Letra letra) {
        letrasDisponibles[quePosicionOcupa(letra)].setFicha(letra);
        letra.setUsada(false);
    }

    private boolean esLetraDisponible(ContenedorFicha contenedorFicha) {
        int i = 0;
        while (i < numeroLetras && ! letrasDisponibles[i].equals(contenedorFicha))
            i++;

        return i < numeroLetras;
    }

    private void memorizar(Letra letra) {
        int i = 0;
        while (i < numeroLetras && memoria[i] != -1)
            i++;

        memoria[i] = quePosicionOcupa(letra);
    }

    private int quePosicionOcupa(Letra letra) {
        for (int i = 0; i < numeroLetras; i++) {
            if (letrasDisponiblesAux[i].equals(letra)) {
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
            letrasDisponibles[quePosicionOcupa(letra)].setFicha(null);
            letrasPuestas[i].setFicha(letra);
            letra.setUsada(true);
        }
    }
}
