package juegos;

import fichas.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Cifras extends Juego {

    final static int numeroCifras = 6;

    final ContenedorFicha cifraObjetivo;
    final ContenedorFicha[] cifrasDisponibles, operadores;
    final ContenedorOperacion[] operacionesRealizadas;
    final Puntuacion puntuacion;
    final SolucionadorCifras solucionador;

    int diferenciaPerfeccion, minDiferenciaConseguida;

    private final ContenedorFicha[] contenedorFichasEnPausa, contenedorFichasMejorDiferencia;

    private boolean haCambiadoMinDiferencia, resuelto;
    private int minCifrasUsadasMejorDiferencia;

    Cifras() {
        super();

        cifraObjetivo = new ContenedorFicha(null);
        cifrasDisponibles = new ContenedorFicha[numeroCifras];
        operacionesRealizadas = new ContenedorOperacion[numeroCifras - 1];
        contenedorFichasEnPausa = new ContenedorFicha[cifrasDisponibles.length + operacionesRealizadas.length];
        contenedorFichasMejorDiferencia = new ContenedorFicha[cifrasDisponibles.length + operacionesRealizadas.length];
        operadores = new ContenedorFicha[4];
        puntuacion = new Puntuacion(false);
        solucionador = new SolucionadorCifras();

        for (int i = 0; i < cifrasDisponibles.length; i++) {
            cifrasDisponibles[i] = new ContenedorFicha(null);
            cifrasDisponibles[i].addMouseListener(this);

            contenedorFichasEnPausa[i] = new ContenedorFicha(null);
            contenedorFichasMejorDiferencia[i] = new ContenedorFicha(null);
        }

        for (int i = 0; i < operacionesRealizadas.length; i++) {
            operacionesRealizadas[i] = new ContenedorOperacion(null);
            operacionesRealizadas[i].addMouseListener(this);

            contenedorFichasEnPausa[i + cifrasDisponibles.length] = new ContenedorOperacion(null);
            contenedorFichasMejorDiferencia[i + cifrasDisponibles.length] = new ContenedorOperacion(null);
        }

        Operacion.operacion[] operador = Operacion.operacion.values();
        for (int i = 0; i < operadores.length; i++) {
            operadores[i] = new ContenedorFicha(new Letra(operador[i].toString().charAt(0)));
            operadores[i].addMouseListener(this);
        }

        setTiempoInicial(60);
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PÚBLICOS **************************************************//
    //***************************************************************************************************************//

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (haEmpezado() && estaDesbloqueado() && contenedorBajoPuntero != null) {
            if (contenedorBajoPuntero.estaOcupado()) {
                if (!contenedorBajoPuntero.getFicha().isUsada())
                    usar(contenedorBajoPuntero);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {}
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public boolean pausar() {
        if (super.pausar()) {
            intercambiarFichasConGuardadas(contenedorFichasEnPausa);

            return true;
        }
        return false;
    }

    @Override
    public boolean reanudar() {
        if (super.reanudar()) {
            intercambiarFichasConGuardadas(contenedorFichasEnPausa);

            return true;
        }
        return false;
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PACKAGE ***************************************************//
    //***************************************************************************************************************//

    boolean deshacer() {
        boolean resultado;
        ContenedorOperacion contenedorOperacion = getUltimaOperacionOcupada();
        if (contenedorOperacion != null) {
            Operacion operacion = (Operacion) contenedorOperacion.getFicha();

            resultado = operacion.borrarUltimoElemento();

            if (!contenedorOperacion.estaEmpezado())
                contenedorOperacion.setFicha(null);

            return resultado;
        }
        return false;
    }

    synchronized boolean estaResuelto() {
        try {
            while (!resuelto) wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        resuelto = false; // Para que visorCifras no establezca el fondo del panel de operaciones continuamente

        return true;
    }

    boolean estaSolucionado() {
        return solucionador.estaSolucionado();
    }

    synchronized boolean haCambiadoMinDiferencia() {
        try {
            while (!haCambiadoMinDiferencia) wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        haCambiadoMinDiferencia = false;
        return true;
    }

    @Override
    void iniciar() {
        resultadoPartida = null;
        diferenciaPerfeccion = 10; // Te da 0 puntos al resolver
        minCifrasUsadasMejorDiferencia = numeroCifras + 1;

        for (ContenedorFicha cifrasDisponible : cifrasDisponibles) {
            cifrasDisponible.setFicha(new Cifra()); // Saca nuevas cifras al azar
        }

        for (ContenedorOperacion operacionRealizada : operacionesRealizadas) {
            operacionRealizada.setFicha(null);
        }

        for (ContenedorFicha contenedorFicha: contenedorFichasMejorDiferencia) {
            contenedorFicha.setFicha(null);
        }

        setMinDiferenciaConseguida(101 + Cifra.RANDOM.nextInt(899)); // Valor de cifra objetivo
        cifraObjetivo.setFicha(new Cifra(minDiferenciaConseguida));
        cifraObjetivo.getFicha().setBackground(new Color(51, 51, 51));
        cifraObjetivo.getFicha().setForeground(Color.WHITE);

        solucionador.setCifraObjetivo(cifraObjetivo);
        solucionador.setCifrasDisponibles(cifrasDisponibles);

        new Thread(solucionador).start(); // Busca la mejor solución
        super.iniciar();
    }

    @Override
    void limpiar() {
        while (deshacer());
    }

    @Override
    synchronized void resolver() {
        if (estaDesbloqueado()) { // Junto con el synchronized para que sólo se pueda resolver una vez
            super.resolver();

            if (resultadoPartida == null || resultadoPartida.equals(resultado.MEJORABLE)) { // Si no se ha hecho una
                // partida perfecta, muestra las operaciones que han llevado a la mejor aproximación (o al mejorable)
                if (resultadoPartida == null) resultadoPartida = resultado.DERROTA;

                intercambiarFichasConGuardadas(contenedorFichasMejorDiferencia);

                ContenedorOperacion ultimaOperacion = getUltimaOperacionOcupada();
                if (ultimaOperacion != null)
                    diferenciaPerfeccion = comprobarResultado((Operacion) ultimaOperacion.getFicha());
            }

            puntuacion.actualizar(diferenciaPerfeccion, resultadoPartida);

            resuelto = true;
            notifyAll();
        }
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PRIVADOS **************************************************//
    //***************************************************************************************************************//

    // Intercambia las fichas en contenedorFichasGuardadas con las de cifrasDisponibles y operacionesRealizadas
    private void intercambiarFichasConGuardadas(ContenedorFicha[] contenedorFichasGuardadas) {
        Ficha fichaDisponible, fichaGuardada;

        for (int i = 0; i < cifrasDisponibles.length; i++) {
            fichaDisponible = cifrasDisponibles[i].getFicha();
            fichaGuardada = contenedorFichasGuardadas[i].getFicha();

            cifrasDisponibles[i].setFicha(fichaGuardada);
            contenedorFichasGuardadas[i].setFicha(fichaDisponible);
        }

        for (int i = 0; i < operacionesRealizadas.length; i++) {
            fichaDisponible = operacionesRealizadas[i].getFicha();
            fichaGuardada = contenedorFichasGuardadas[i + cifrasDisponibles.length].getFicha();

            operacionesRealizadas[i].setFicha(fichaGuardada);
            contenedorFichasGuardadas[i + cifrasDisponibles.length].setFicha(fichaDisponible);
        }
    }

    private int calcularDiferenciaConseguida(Operacion operacion, int numeroCifrasUsadas) {
        int resultadoOperacion = operacion.getResultado().getValor();
        int valorObjetivo = ((Cifra) cifraObjetivo.getFicha()).getValor();
        int diferenciaConseguida = Math.abs(valorObjetivo - resultadoOperacion);

        if (diferenciaConseguida < minDiferenciaConseguida ||
                (diferenciaConseguida == minDiferenciaConseguida && numeroCifrasUsadas < minCifrasUsadasMejorDiferencia))
        {
            setMinDiferenciaConseguida(diferenciaConseguida);
            minCifrasUsadasMejorDiferencia = numeroCifrasUsadas;
        }

        return diferenciaConseguida;
    }

    // Comprueba si el resultado obtenido es el mejor y devuelve la diferencia respecto a la perfección
    private int comprobarResultado(Operacion operacion) {
        int numCifrasUsadas = getNumeroCifrasUsadas();

        int diferenciaConseguida = calcularDiferenciaConseguida(operacion, numCifrasUsadas);

        int diferenciaPerfeccion = solucionador.getDistanciaPerfeccion(diferenciaConseguida, numCifrasUsadas);

        if (diferenciaPerfeccion == 0) {
            resultadoPartida = resultado.PERFECTO;
        } else if (diferenciaPerfeccion < 5) {
            resultadoPartida = resultado.MEJORABLE;
        }

        return Math.min(10, diferenciaPerfeccion); // diferenciaPerfección ha de estar entre 0 y 10
    }

    private int getNumeroCifrasUsadas() {
        int numCifrasUsadas = 0;

        for (ContenedorFicha contenedorFicha: cifrasDisponibles) {
            if (contenedorFicha.getFicha().isUsada())
                numCifrasUsadas++;
        }
        return numCifrasUsadas;
    }

    private ContenedorOperacion getPrimeraOperacionNoCompleta() {
        for (ContenedorOperacion operacion: operacionesRealizadas) {
            if (!operacion.estaOcupado() || !operacion.estaCompleto())
                return operacion;
        }
        return null;
    }

    private ContenedorOperacion getUltimaOperacionOcupada() {
        for (int i = operacionesRealizadas.length; i > 0; i--) {
            if (operacionesRealizadas[i - 1].estaEmpezado())
                return operacionesRealizadas[i - 1];
        }
        return null;
    }

    // Guarda la situación de las cifras disponibles y las operaciones realizadas en contenedorFichasMejorDiferencia
    private void guardarSituacionFichas() {
        for (int i = 0; i < cifrasDisponibles.length; i++) {
            contenedorFichasMejorDiferencia[i].setFicha(new Cifra((Cifra) cifrasDisponibles[i].getFicha()));
        }

        for (int i = 0; i < operacionesRealizadas.length; i++) {
            Operacion operacion = (Operacion) operacionesRealizadas[i].getFicha();
            if (operacion != null)
                contenedorFichasMejorDiferencia[i + cifrasDisponibles.length].setFicha(new Operacion(operacion));
            else
                contenedorFichasMejorDiferencia[i + cifrasDisponibles.length].setFicha(null);
        }
    }

    private Operacion.operacion operacionElegida() {
        int i = 0;
        while (i < operadores.length && !contenedorBajoPuntero.equals(operadores[i]))
            i++;

        if (i < operadores.length) {
            char operador = ((Letra) operadores[i].getFicha()).getValor();
            for (Operacion.operacion operacion: Operacion.operacion.values()) {
                if (operacion.toString().equals(String.valueOf(operador)))
                    return operacion;
            }
        }

        return null;
    }

    private synchronized void setMinDiferenciaConseguida(int nuevoValor) {
        minDiferenciaConseguida = nuevoValor;
        guardarSituacionFichas();
        haCambiadoMinDiferencia = true;
        notifyAll();
    }

    private synchronized void usar(ContenedorFicha ficha) {
        ContenedorOperacion contenedor = getPrimeraOperacionNoCompleta();

        if (contenedor != null) {
            if (!contenedor.estaOcupado())
                contenedor.setFicha(new Operacion());

            Operacion operacion = (Operacion) contenedor.getFicha();

            if (ficha.getFicha() instanceof Cifra) { // Ficha es Cifra u Operación
                operacion.setOperando((Cifra) ficha.getFicha());
                if (operacion.getResultado() != null) {
                    diferenciaPerfeccion = comprobarResultado(operacion);
                    if (diferenciaPerfeccion == 0) resolver(); // Perfecto
                    else if (diferenciaPerfeccion < 5) { // Mejorable
                        pausar();
                        JOptionPane.showMessageDialog(null, "Se puede mejorar");
                        reanudar();
                    }
                }

            } else { // La ficha es un operador
                // Si el operador no se puede poner (la operación no está empezada) se coge el último resultado
                // obtenido como primer operando
                if (!operacion.setOperador(operacionElegida())) {
                    ContenedorOperacion contenedorOperacion = getUltimaOperacionOcupada();
                    if (contenedorOperacion != null && contenedorOperacion.estaCompleto()) {
                        usar(contenedorOperacion);
                        operacion.setOperador(operacionElegida());
                    } else { // Si la primera ficha que se elige es un operador (antes que haya cualquier operación)
                        // al ser la operación un extends de Cifra, se saca un número al azar, por tanto, se ha de
                        // quitar, además de que una operación vacía tiene un fondo que no queda bien estéticamente
                        contenedor.setFicha(null);
                    }
                }
            }
        }
    }
}
