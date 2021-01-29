package juegos;

import fichas.*;

import java.awt.*;
import java.awt.event.MouseEvent;

class Cifras extends Juego {

    enum resultado {
        DERROTA, MEJORABLE, PERFECTO
    }

    final static int numeroCifras = 6;

    final ContenedorFicha cifraObjetivo;
    final ContenedorFicha[] cifrasDisponibles, operadores;
    final ContenedorOperacion[] operacionesRealizadas;
    final Puntuacion puntuacion;
    final SolucionadorCifras solucionador;

    int diferenciaPerfeccion, minDiferenciaConseguida;
    resultado resultadoPartida;

    private boolean haCambiadoMinDiferencia, resuelto;

    Cifras() {
        super();

        cifraObjetivo = new ContenedorFicha(null);
        cifrasDisponibles = new ContenedorFicha[numeroCifras];
        operacionesRealizadas = new ContenedorOperacion[numeroCifras - 1];
        operadores = new ContenedorFicha[4];
        puntuacion = new Puntuacion();
        solucionador = new SolucionadorCifras();

        for (int i = 0; i < cifrasDisponibles.length; i++) {
            cifrasDisponibles[i] = new ContenedorFicha(null);
            cifrasDisponibles[i].addMouseListener(this);
        }

        for (int i = 0; i < operacionesRealizadas.length; i++) {
            operacionesRealizadas[i] = new ContenedorOperacion(null);
            operacionesRealizadas[i].addMouseListener(this);
        }

        char[] operador = {'+', '-', '×', '÷'};
        for (int i = 0; i < operadores.length; i++) {
            operadores[i] = new ContenedorFicha(new Letra(operador[i]));
            operadores[i].addMouseListener(this);
        }

        setTiempoInicial(60);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (haEmpezado() && !estaBloqueado() && contenedorBajoPuntero != null) {
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
        while (!resuelto) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        resuelto = false; // Para que visorCifras no establezca el fondo del panel de operaciones continuamente

        return true;
    }

    boolean estaSolucionado() {
        return solucionador.estaSolucionado();
    }

    synchronized boolean haCambiadoMinDiferencia() {
        while (!haCambiadoMinDiferencia) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        haCambiadoMinDiferencia = false;
        return true;
    }

    @Override
    void iniciar() {
        setMinDiferenciaConseguida(101 + Cifra.RANDOM.nextInt(899));
        resultadoPartida = null;
        diferenciaPerfeccion = 10;

        cifraObjetivo.setFicha(new Cifra(minDiferenciaConseguida));
        cifraObjetivo.getFicha().setBackground(new Color(51, 51, 51));
        cifraObjetivo.getFicha().setForeground(Color.WHITE);

        for (ContenedorFicha cifrasDisponible : cifrasDisponibles) {
            cifrasDisponible.setFicha(new Cifra());
        }

        for (ContenedorOperacion operacionRealizada : operacionesRealizadas) {
            operacionRealizada.setFicha(null);
        }

        solucionador.setCifraObjetivo(cifraObjetivo);
        solucionador.setCifrasDisponibles(cifrasDisponibles);

        super.iniciar();
        new Thread(this).start();
        new Thread(solucionador).start();
    }

    @Override
    void limpiar() {
        while (deshacer());
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
    synchronized void resolver() {
        if (!estaBloqueado()) { // Junto con el synchronized para que sólo se pueda resolver una vez
            super.resolver();

            if (resultadoPartida == null)
                resultadoPartida = resultado.DERROTA;

            puntuacion.actualizar(diferenciaPerfeccion, false);

            resuelto = true;
            notifyAll();
        }
    }

    private void calcularDiferenciaConseguida(Operacion operacion) {
        int resultadoOperacion = operacion.getResultado().getValor();
        int valorObjetivo = ((Cifra) cifraObjetivo.getFicha()).getValor();
        int diferenciaConseguida = Math.abs(valorObjetivo - resultadoOperacion);

        if (diferenciaConseguida < minDiferenciaConseguida)
            setMinDiferenciaConseguida(diferenciaConseguida);
    }

    private int comprobarResultado(Operacion operacion) {
        calcularDiferenciaConseguida(operacion);

        int diferenciaPerfeccion = solucionador.getDistanciaPerfeccion(minDiferenciaConseguida, getNumeroCifrasUsadas());

        if (diferenciaPerfeccion == 0) {
            resultadoPartida = resultado.PERFECTO;
        } else if (diferenciaPerfeccion < 5) {
            resultadoPartida = resultado.MEJORABLE;
        }

        return Math.min(10, diferenciaPerfeccion);
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
        int i = operacionesRealizadas.length - 1;
        while (i >= 0 && !operacionesRealizadas[i].estaEmpezado())
            i--;

        if (i < 0) return null;
        else return operacionesRealizadas[i];
    }

    private Operacion.operacion operacionElegida() {
        char operador;
        int i = 0;
        while (i < operadores.length && !contenedorBajoPuntero.equals(operadores[i]))
            i++;

        if (i < operadores.length) {
            operador = ((Letra) operadores[i].getFicha()).getValor();
            for (Operacion.operacion operacion: Operacion.operacion.values()) {
                if (operacion.toString().equals(String.valueOf(operador)))
                    return operacion;
            }
        }

        return null;
    }

    private synchronized void setMinDiferenciaConseguida(int nuevoValor) {
        minDiferenciaConseguida = nuevoValor;
        haCambiadoMinDiferencia = true;
        notifyAll();
    }

    private synchronized void usar(ContenedorFicha ficha) {
        ContenedorOperacion contenedor = getPrimeraOperacionNoCompleta();

        if (contenedor != null) {
            if (!contenedor.estaOcupado())
                contenedor.setFicha(new Operacion());

            Operacion operacion = (Operacion) contenedor.getFicha();

            if (ficha.getFicha() instanceof Cifra) {
                operacion.setOperando((Cifra) ficha.getFicha());
                if (operacion.getResultado() != null) {
                    diferenciaPerfeccion = comprobarResultado(operacion);
                    if (diferenciaPerfeccion < 5) resolver(); // Perfecto o mejorable
                }

            } else {
                if (!operacion.setOperador(operacionElegida())) {
                    ContenedorOperacion contenedorOperacion = getUltimaOperacionOcupada();
                    if (contenedorOperacion != null && contenedorOperacion.estaCompleto()) {
                        usar(contenedorOperacion);
                        operacion.setOperador(operacionElegida());
                    } else { // Si la primera ficha que se elige es un operador (antes que haya cualquier operación)
                        // al ser la operación un extends de Cifra, se saca un número al azar, por tanto, se ha de
                        // quitar
                        contenedor.setFicha(null);
                    }
                }
            }
        }
    }
}
