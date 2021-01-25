package juegos;

import fichas.*;

import java.awt.*;
import java.awt.event.MouseEvent;

class Cifras extends Juego {

    final static int numeroCifras = 6;

    final ContenedorFicha cifraObjetivo;
    final ContenedorFicha[] cifrasDisponibles, operadores;
    final ContenedorOperacion[] operacionesRealizadas;

    Cifras() {
        super();

        cifraObjetivo = new ContenedorFicha(null);
        cifrasDisponibles = new ContenedorFicha[2 * numeroCifras - 1];
        operacionesRealizadas = new ContenedorOperacion[numeroCifras - 1];
        operadores = new ContenedorFicha[4];

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

    @Override
    void iniciar() {
        cifraObjetivo.setFicha(new Cifra(101 + Cifra.RANDOM.nextInt(899)));
        cifraObjetivo.getFicha().setBackground(new Color(51, 51, 51));
        cifraObjetivo.getFicha().setForeground(Color.WHITE);

        for (int i = 0; i < cifrasDisponibles.length; i++) {
            cifrasDisponibles[i].setFicha(i < numeroCifras ? new Cifra() : null);
        }

        for (ContenedorOperacion operacionRealizada : operacionesRealizadas) {
            operacionRealizada.setFicha(null);
        }

        super.iniciar();
        new Thread(this).start();
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
    void resolver() {
        super.resolver();
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

    private void usar(ContenedorFicha ficha) {
        ContenedorOperacion contenedor = getPrimeraOperacionNoCompleta();

        if (contenedor != null) {
            if (!contenedor.estaOcupado())
                contenedor.setFicha(new Operacion());

            Operacion operacion = (Operacion) contenedor.getFicha();

            if (ficha.getFicha() instanceof Cifra) {
                operacion.setOperando((Cifra) ficha.getFicha());

            } else {
                if (!operacion.setOperador(operacionElegida())) {
                    ContenedorOperacion contenedorOperacion = getUltimaOperacionOcupada();
                    if (contenedorOperacion != null && contenedorOperacion.estaCompleto()) {
                        usar(contenedorOperacion);
                        operacion.setOperador(operacionElegida());
                    }
                }
            }
        }
    }
}
