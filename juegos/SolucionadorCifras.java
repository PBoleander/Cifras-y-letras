package juegos;

import fichas.*;

class SolucionadorCifras implements Runnable {

    private final CifraSolucionador[] cifrasDisponibles;
    private final StringBuilder stringBuilder;

    private boolean solucionado;
    private CifraSolucionador cifraObjetivo;
    private int minDiferencia, minCifrasUsadas, minCifrasUsadasInexacto, numCifrasUsadas;

    SolucionadorCifras() {
        cifrasDisponibles = new CifraSolucionador[2 * Cifras.numeroCifras - 1];
        stringBuilder = new StringBuilder();
    }

    @Override
    public void run() {
        solucionado = false;
        buscarMejorSolucion();
        solucionado = true;
    }

    @Override
    public synchronized String toString() {
        return stringBuilder.toString();
    }

    synchronized int getDistanciaPerfeccion(int diferenciaConseguida, int cifrasUsadas) {
        int minCifrasUtilizadas = (minDiferencia == 0) ? minCifrasUsadas : minCifrasUsadasInexacto;
        if (diferenciaConseguida == minDiferencia) {
            return cifrasUsadas - minCifrasUtilizadas;
        } else {
            // Si te quedas a uno del número exacto tiene que haber una distancia de la perfección de 5, etc. (por
            // eso el + 4)
            return Math.abs(minDiferencia - diferenciaConseguida) + 4;
        }
    }

    synchronized int getMinDiferencia() {
        return minDiferencia;
    }

    boolean estaSolucionado() {
        return solucionado;
    }

    void setCifraObjetivo(ContenedorFicha cifraObjetivo) {
        this.cifraObjetivo = new CifraSolucionador(((Cifra) cifraObjetivo.getFicha()).getValor());
        minDiferencia = this.cifraObjetivo.getValor();
    }

    void setCifrasDisponibles(ContenedorFicha[] cifrasDisponibles) {
        for (int i = 0; i < this.cifrasDisponibles.length; i++) {
            if (i < Cifras.numeroCifras)
                this.cifrasDisponibles[i] = new CifraSolucionador(((Cifra) cifrasDisponibles[i].getFicha()).getValor());
            else
                this.cifrasDisponibles[i] = null;
        }
    }

    private void actualizarDiferencias(CifraSolucionador resultado) {
        int difActual = Math.abs(cifraObjetivo.getValor() - resultado.getValor());
        if (difActual <= minDiferencia) {
            if (difActual == 0) {
                if (numCifrasUsadas < minCifrasUsadas) {
                    minCifrasUsadas = numCifrasUsadas;
                    actualizarString(resultado);
                    if (minDiferencia > 0)
                        minDiferencia = 0;
                }
            } else {
                if (difActual < minDiferencia) {
                    minDiferencia = difActual;
                    minCifrasUsadasInexacto = numCifrasUsadas;
                    actualizarString(resultado);
                } else { // difActual == minDiferencia
                    if (numCifrasUsadas < minCifrasUsadasInexacto) {
                        minCifrasUsadasInexacto = numCifrasUsadas;
                        actualizarString(resultado);
                    }
                }
            }
        }
    }

    private void actualizarString(CifraSolucionador resultado) {
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append(resultado.getOperacion()).append(" = ").append(resultado.getValor());
    }

    private void addToCifrasDisponibles(CifraSolucionador cifra) {
        int i = Cifras.numeroCifras;
        while (i < cifrasDisponibles.length && cifrasDisponibles[i] != null)
            i++;

        if (i < cifrasDisponibles.length)
            cifrasDisponibles[i] = cifra;
    }

    private void borrarDeCifrasDisponibles(CifraSolucionador cifra) {
        int i = Cifras.numeroCifras;
        while (i < cifrasDisponibles.length && cifrasDisponibles[i] != cifra)
            i++;

        if (i < cifrasDisponibles.length)
            cifrasDisponibles[i] = null;
    }

    private synchronized void buscarMejorSolucion() {
        minCifrasUsadas = Cifras.numeroCifras + 1;
        minCifrasUsadasInexacto = minCifrasUsadas;
        stringBuilder.delete(0, stringBuilder.length());

        for (CifraSolucionador cifra: cifrasDisponibles) {
            numCifrasUsadas = 0;
            if (cifra != null && esValorNuevo(cifra))
                combinar(cifra);
        }

    }

    private void combinar(CifraSolucionador cifra) {
        if (esCifraInicial(cifra))
            numCifrasUsadas++;

        if (numCifrasUsadas < minCifrasUsadas) {
            cifra.setUsada(true);
            for (CifraSolucionador siguienteCifra : cifrasDisponibles) {
                if (siguienteCifra != null && !siguienteCifra.isUsada()) {
                    if (cifra.getValor() >= siguienteCifra.getValor()) {
                        if (esCifraInicial(siguienteCifra)) numCifrasUsadas++;

                        if (numCifrasUsadas < minCifrasUsadas) {
                            siguienteCifra.setUsada(true);

                            for (Operacion.operacion operador : Operacion.operacion.values()) {
                                if (esOperacionInnecesaria(operador, cifra, siguienteCifra))
                                    continue;

                                CifraSolucionador resultado = operar(cifra, siguienteCifra, operador);
                                if (resultado != null) {
                                    actualizarDiferencias(resultado);
                                    addToCifrasDisponibles(resultado);
                                    for (CifraSolucionador otraCifra : cifrasDisponibles) {
                                        if (otraCifra != null && !otraCifra.isUsada())
                                            combinar(otraCifra);
                                    }
                                    borrarDeCifrasDisponibles(resultado);
                                }
                            }
                            siguienteCifra.setUsada(false);
                        }
                        if (esCifraInicial(siguienteCifra)) numCifrasUsadas--;
                    }
                }
            }
            cifra.setUsada(false);
        }
        if (esCifraInicial(cifra))
            numCifrasUsadas--;
    }

    private boolean esCifraInicial(CifraSolucionador cifra) {
        int i = 0;
        while (i < cifrasDisponibles.length && !cifrasDisponibles[i].equals(cifra))
            i++;

        return i < Cifras.numeroCifras;
    }

    private boolean esOperacionInnecesaria(Operacion.operacion operador, CifraSolucionador cifra1,
                                           CifraSolucionador cifra2) {
        if (operador.equals(Operacion.operacion.PRODUCTO)) {
            return (cifra2.getValor() == 1) || (cifra1.getValor() == 2 && cifra2.getValor() == 2);
        } else if (operador.equals(Operacion.operacion.DIVISION)) {
            return cifra2.getValor() == 1;
        }
        return false;
    }

    private boolean esValorNuevo(CifraSolucionador cifra) {
        int i = 0;
        if (esCifraInicial(cifra)) {
            while (cifrasDisponibles[i].getValor() != cifra.getValor())
                i++;
        }
        return cifrasDisponibles[i].equals(cifra);
    }

    private CifraSolucionador operar(CifraSolucionador operando1, CifraSolucionador operando2,
                                     Operacion.operacion operador) {
        OperacionSolucionador operacion = new OperacionSolucionador(operando1, operando2, operador);

        return operacion.getResultado();
    }
}
