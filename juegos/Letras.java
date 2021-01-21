package juegos;

import fichas.ContenedorFicha;
import fichas.Letra;
import general.Idioma;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Arrays;

class Letras extends Juego implements KeyListener {

    final static int numeroLetras = 9;

    final ContenedorFicha[] letrasDisponibles, letrasPuestas;
    final int[] memoria;
    final JList<String> listaSolucion;
    final Puntuacion puntuacion;
    final SolucionadorLetras solucionador;

    boolean comprobado, resultadoComprobacion;
    int longitudMemoria, numeroLetrasSacadas;

    private final Letra[] letrasDisponiblesAux, letrasDisponiblesPausa, letrasPuestasPausa;
    private ContenedorFicha contenedorBajoPuntero;
    private Idioma idioma;
    private Letra letraArrastrada;

    Letras() {
        super();

        this.memoria = new int[numeroLetras];
        this.listaSolucion = new JList<>();
        this.puntuacion = new Puntuacion();
        this.solucionador = new SolucionadorLetras();
        this.letrasDisponibles = new ContenedorFicha[numeroLetras];
        this.letrasDisponiblesAux = new Letra[numeroLetras];
        this.letrasPuestas = new ContenedorFicha[numeroLetras];
        this.letrasDisponiblesPausa = new Letra[numeroLetras];
        this.letrasPuestasPausa = new Letra[numeroLetras];

        char[] caracteresPausaDisponibles = "  JUEGO  ".toCharArray();
        char[] caracteresPausaPuestas = " PAUSADO ".toCharArray();

        for (int i = 0; i < numeroLetras; i++) {
            letrasDisponibles[i] = new ContenedorFicha(null);
            letrasDisponibles[i].addMouseListener(this);

            letrasPuestas[i] = new ContenedorFicha(null);
            letrasPuestas[i].addMouseListener(this);

            letrasDisponiblesPausa[i] = new Letra(caracteresPausaDisponibles[i]);
            letrasPuestasPausa[i] = new Letra(caracteresPausaPuestas[i]);
        }

        this.contenedorBajoPuntero = null;
        this.letraArrastrada = null;

        listaSolucion.setEnabled(false);
        listaSolucion.setCellRenderer(new CellRenderer());

        iniciar();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        if (haEmpezado() && !estaBloqueado() && !keyEvent.isAltDown()) {
            if (keyEvent.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                int ultimaPosicionPuesta = ultimaPosicionPuesta();
                if (ultimaPosicionPuesta > -1) {
                    desusar(letrasPuestas[ultimaPosicionPuesta]);
                }
            } else {
                char letraEscrita = keyEvent.getKeyChar();
                letraEscrita = String.valueOf(letraEscrita).toUpperCase().charAt(0);
                usar(letraEscrita);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {}

    @Override
    public void keyReleased(KeyEvent keyEvent) {}

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (haEmpezado() && ! estaBloqueado() && contenedorBajoPuntero != null) {
            if (contenedorBajoPuntero.estaOcupado()) {
                if (esLetraDisponible(contenedorBajoPuntero)) {
                    usar(contenedorBajoPuntero);
                } else { // Es letra puesta
                    desusar(contenedorBajoPuntero);
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

    synchronized void comprobar() {
        resultadoComprobacion = solucionador.contiene(getPalabraPuesta());
        comprobado = true;
        notifyAll();
    }

    synchronized boolean estaComprobado() {
        while (!comprobado) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        comprobado = false;

        return true;
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
        listaSolucion.setModel(new DefaultListModel<>());
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
                desusar(contenedorFicha);
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
                memorizar(letra);
                longitudMemoria++;
            }
        }
    }

    @Override
    void pausar() {
        super.pausar();

        alternarMensajePausa();
    }

    @Override
    void reanudar() {
        super.reanudar();

        alternarMensajePausa();
    }

    void recuperarMemoria() {
        if (longitudMemoria > 0) {
            limpiar();

            for (int i = 0; i < longitudMemoria; i++) {
                usar(letrasDisponibles[memoria[i]]);
            }
        }
    }

    @Override
    synchronized void resolver() {
        if (!estaBloqueado()) {
            super.resolver();

            comprobar();
            String palabraPuesta = getPalabraPuesta();

            if (resultadoComprobacion) {
                puntuacion.actualizar(solucionador.getNumLongitudesMejores(palabraPuesta.length()));
            } else {
                puntuacion.actualizar(10); // Así da 0 puntos
            }

            SwingUtilities.invokeLater(() -> {
                listaSolucion.setModel(solucionador.getListaSolucion());
                listaSolucion.setSelectedValue(palabraPuesta, false);
            });
        }
    }

    void sacar(Letra.Tipo tipo) {
        if (numeroLetrasSacadas < numeroLetras) {
            Letra letra = new Letra(tipo);
            letrasDisponibles[numeroLetrasSacadas].setFicha(letra);
            letrasDisponiblesAux[numeroLetrasSacadas] = letra;

            numeroLetrasSacadas++;

            if (numeroLetrasSacadas == numeroLetras) {
                solucionador.setLetrasDisponibles(letrasDisponiblesAux);
                new Thread(solucionador).start();
                setTiempoInicial(idioma == Idioma.INGLES ? 60 : 45);
                super.iniciar();
            }
        }
    }

    void setIdioma(Idioma idioma) {
        this.idioma = idioma;
        solucionador.setIdioma(idioma);
        Letra.generador.setIdioma(idioma);
    }

    private void alternarMensajePausa() {
        Letra letraDisponible, letraPuesta;

        for (int i = 0; i < numeroLetras; i++) {
            letraDisponible = (Letra) letrasDisponibles[i].getFicha();
            letraPuesta = (Letra) letrasPuestas[i].getFicha();

            letrasDisponibles[i].setFicha(letrasDisponiblesPausa[i]);
            letrasPuestas[i].setFicha(letrasPuestasPausa[i]);

            letrasDisponiblesPausa[i] = letraDisponible;
            letrasPuestasPausa[i] = letraPuesta;
        }
    }

    private void desusar(ContenedorFicha contenedorFicha) {
        if (contenedorFicha.estaOcupado()) {
            Letra letra = (Letra) contenedorFicha.getFicha();

            letrasDisponibles[quePosicionDisponibleOcupa(letra)].setFicha(letra);
            letra.setUsada(false);
            contenedorFicha.setFicha(null);
        }
    }

    private boolean esLetraDisponible(ContenedorFicha contenedorFicha) {
        int i = 0;
        while (i < numeroLetras && ! letrasDisponibles[i].equals(contenedorFicha))
            i++;

        return i < numeroLetras;
    }

    private String getPalabraPuesta() {
        StringBuilder palabraPuesta = new StringBuilder();

        for (ContenedorFicha contenedorFicha: letrasPuestas) {
            if (contenedorFicha.estaOcupado()) {
                palabraPuesta.append(((Letra) contenedorFicha.getFicha()).getValor());
            }
        }

        return palabraPuesta.toString();
    }

    private void memorizar(Letra letra) {
        int i = 0;
        while (i < numeroLetras && memoria[i] != -1)
            i++;

        memoria[i] = quePosicionDisponibleOcupa(letra);
    }

    private int quePosicionDisponibleOcupa(char letra) {
        for (int i = 0; i < numeroLetras; i++) {
            if (!letrasDisponiblesAux[i].isUsada() && letrasDisponiblesAux[i].getValor() == letra) {
                return i;
            }
        }
        return -1;
    }

    private int quePosicionDisponibleOcupa(Letra letra) {
        for (int i = 0; i < numeroLetras; i++) {
            if (letrasDisponiblesAux[i].equals(letra)) {
                return i;
            }
        }
        return -1; // Aquí no debe llegar
    }

    private int ultimaPosicionPuesta() {
        int i = numeroLetras - 1;
        while (i >= 0 && !letrasPuestas[i].estaOcupado())
            i--;

        return i;
    }

    private void usar(char letra) {
        int posicion = quePosicionDisponibleOcupa(letra);
        if (posicion != -1) {
            usar(letrasDisponibles[posicion]);
        }
    }

    private void usar(ContenedorFicha contenedorFicha) {
        if (contenedorFicha.estaOcupado()) {
            int i = 0;
            while (i < numeroLetras && letrasPuestas[i].estaOcupado())
                i++;

            if (i < numeroLetras) {
                letrasPuestas[i].setFicha(contenedorFicha.getFicha());
                ((Letra) contenedorFicha.getFicha()).setUsada(true);
                contenedorFicha.setFicha(null);
            }
        }
    }
}
