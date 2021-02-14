package juegos;

import fichas.ContenedorFicha;
import fichas.Letra;
import general.Idioma;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class Letras extends Juego implements KeyListener {

    final static int numeroLetras = 9;

    final ContenedorFicha[] letrasDisponibles, letrasPuestas;
    final int[] memoria;
    final JList<String> listaSolucion;
    final Puntuacion puntuacion;
    final SolucionadorLetras solucionador;

    boolean comprobado, resultadoComprobacion;
    int longitudMemoria, numeroLetrasSacadas;

    private final Letra[] letrasDisponiblesAux, letrasDisponiblesPausa, letrasPuestasPausa;
    private boolean cambioMensajePausa;
    private Letra letraArrastrada;

    Letras() {
        super();

        this.memoria = new int[numeroLetras];
        this.listaSolucion = new JList<>();
        this.puntuacion = new Puntuacion(true);
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

        this.letraArrastrada = null;

        listaSolucion.setEnabled(false);
        listaSolucion.setCellRenderer(new CellRenderer());

        iniciar();
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PÚBLICOS **************************************************//
    //***************************************************************************************************************//

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        if (haEmpezado() && estaDesbloqueado() && !keyEvent.isAltDown()) {
            if (keyEvent.getKeyChar() == KeyEvent.VK_BACK_SPACE) { // Borra la última letra puesta
                int ultimaPosicionPuesta = ultimaPosicionPuesta();
                if (ultimaPosicionPuesta > -1) {
                    desusar(letrasPuestas[ultimaPosicionPuesta]);
                }

            } else { // La tecla pulsada no es la de retroceso (usa la letra si está disponible)
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
    // Usa/desusa letras
    public void mouseClicked(MouseEvent mouseEvent) {
        if (haEmpezado() && estaDesbloqueado() && contenedorBajoPuntero != null) {
            if (contenedorBajoPuntero.estaOcupado()) {
                if (contenedorBajoPuntero.getFicha().isUsada()) {
                    desusar(contenedorBajoPuntero);
                } else {
                    usar(contenedorBajoPuntero);
                }
            }
        }
    }

    @Override
    // Arrastra las letras puestas para cambiarlas de posición
    public void mousePressed(MouseEvent mouseEvent) {
        if (haEmpezado() && estaDesbloqueado() && contenedorBajoPuntero != null) {
            if (contenedorBajoPuntero.estaOcupado()) {
                if (esLetraPuesta(contenedorBajoPuntero)) {
                    letraArrastrada = (Letra) contenedorBajoPuntero.getFicha();
                }
            }
        }
    }

    @Override
    // Suelta la letra arrastrada en el lugar indicado
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 0) {
            if (haEmpezado() && estaDesbloqueado() && contenedorBajoPuntero != null) {
                if (esLetraPuesta(contenedorBajoPuntero)) {
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
    public boolean pausar() {
        if (super.pausar()) {
            alternarMensajePausa();
            setCambioMensajePausa();

            return true;
        }
        return false;
    }

    @Override
    public boolean reanudar() {
        if (super.reanudar()) {
            alternarMensajePausa();
            setCambioMensajePausa();

            return true;
        }
        return false;
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PACKAGE ***************************************************//
    //***************************************************************************************************************//

    synchronized boolean cambioEnMensajePausa() {
        try {
            while (!cambioMensajePausa) wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cambioMensajePausa = false;
        return true;
    }

    // Comprueba si la palabra puesta está en el diccionario. Si la partida ha terminado, actualiza el resultado de
    // la partida
    synchronized void comprobar() {
        String palabraPuesta = getPalabraPuesta();
        resultadoComprobacion = solucionador.contiene(palabraPuesta);

        if (!haEmpezado()) { // Se ha procedido a resolver (en otro caso, no es necesario)
            // Si la palabra puesta al terminar la partida ofrece un resultado peor que la de la memoria, la cambia
            // por ésta
            if (resultadoPeorAlDeMemoria(palabraPuesta)) {
                recuperarMemoria();
                palabraPuesta = getPalabraPuesta();
                resultadoComprobacion = solucionador.contiene(palabraPuesta);
            }

            actualizarResultadoPartida(palabraPuesta);
        }

        comprobado = true;
        notifyAll();
    }

    synchronized boolean estaComprobado() {
        try {
            while (!comprobado) wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        resultadoPartida = null;
        listaSolucion.setModel(new DefaultListModel<>()); // Vacía la lista solución
        numeroLetrasSacadas = 0;
        vaciarMemoria();

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
        if (seQuiereMemorizar()) { // Controla si se pretende memorizar una palabra incorrecta o más corta de la ya
            // memorizada
            if (longitudMemoria > 0)
                vaciarMemoria();

            Letra letra;
            for (ContenedorFicha contenedorFicha : letrasPuestas) {
                if (contenedorFicha.estaOcupado()) {
                    letra = (Letra) contenedorFicha.getFicha();
                    memorizar(letra);
                    longitudMemoria++;
                }
            }
            setCambioMensajePausa();
        }
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
        if (estaDesbloqueado()) {
            super.resolver();

            comprobar();
            String palabraPuesta = getPalabraPuesta();

            if (resultadoComprobacion) { // La palabra puesta es correcta
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

            if (numeroLetrasSacadas == numeroLetras) { // Ya no quedan letras por sacar
                solucionador.setLetrasDisponibles(letrasDisponiblesAux);
                new Thread(solucionador).start();

                super.iniciar();
            }
        }
    }

    void setIdioma(Idioma idioma) {
        solucionador.setIdioma(idioma);
        Letra.generador.setIdioma(idioma);
        setTiempoInicial(idioma == Idioma.INGLES ? 60 : 45);
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PRIVADOS **************************************************//
    //***************************************************************************************************************//

    private void actualizarResultadoPartida(String palabraPuesta) {
        if (resultadoComprobacion) {
            if (solucionador.getNumLongitudesMejores(palabraPuesta.length()) == 0)
                resultadoPartida = resultado.PERFECTO;
            else
                resultadoPartida = resultado.MEJORABLE;

        } else {
            resultadoPartida = resultado.DERROTA;
        }
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

    // Indica si el contenedorFicha pertenece a las letras puestas
    private boolean esLetraPuesta(ContenedorFicha contenedorFicha) {
        int i = 0;
        while (i < numeroLetras && ! letrasDisponibles[i].equals(contenedorFicha))
            i++;

        return i == numeroLetras;
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

    private int mostrarAvisoDeMemorizadoIlogico() {
        String mensaje = "La palabra a memorizar es incorrecta o de menor longitud a la ya guardada. ¿Seguro que " +
                "quieres continuar?";
        return JOptionPane.showConfirmDialog(null, mensaje, "Aviso", JOptionPane.YES_NO_OPTION);
    }

    private boolean palabraAMemorizarEsIlogica(String palabraPuesta) {
        return !solucionador.contiene(palabraPuesta) ||
                (longitudMemoria > 0 &&
                        solucionador.contiene(getPalabraMemorizada()) && palabraPuesta.length() < longitudMemoria);
    }

    // Devuelve el índice que ocupa el carácter letra en las letras disponibles si no está usada, -1 en caso contrario
    private int quePosicionDisponibleOcupa(char letra) {
        for (int i = 0; i < numeroLetras; i++) {
            if (!letrasDisponiblesAux[i].isUsada() && letrasDisponiblesAux[i].getValor() == letra) {
                return i;
            }
        }
        return -1;
    }

    // Devuelve el índice que ocupa letra en letras disponibles
    private int quePosicionDisponibleOcupa(Letra letra) {
        for (int i = 0; i < numeroLetras; i++) {
            if (letrasDisponiblesAux[i].equals(letra)) {
                return i;
            }
        }
        return -1; // Aquí no debe llegar
    }

    // Indica si la palabra que se quiere memorizar es correcta y más larga o igual a la ya memorizada, en caso
    // contrario, pregunta si se quiere proceder a memorizarla
    private boolean seQuiereMemorizar() {
        if (palabraAMemorizarEsIlogica(getPalabraPuesta())) {
            pausar();
            if (mostrarAvisoDeMemorizadoIlogico() != JOptionPane.YES_OPTION) {
                reanudar();
                return false;
            }
            reanudar();
        }
        return true; // Si la palabra a memorizar no es una palabra ilógica o se elige que sí a memorizar una ilógica
    }

    private boolean resultadoPeorAlDeMemoria(String palabraPuesta) {
        return longitudMemoria > 0 && solucionador.contiene(getPalabraMemorizada()) &&
                (
                    !resultadoComprobacion || longitudMemoria > palabraPuesta.length()
                );
    }

    private synchronized void setCambioMensajePausa() {
        cambioMensajePausa = true;
        notifyAll();
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
                contenedorFicha.getFicha().setUsada(true);
                contenedorFicha.setFicha(null);
            }
        }
    }

    private void vaciarMemoria() {
        Arrays.fill(memoria, -1);
        longitudMemoria = 0;
        setCambioMensajePausa();
    }
}
