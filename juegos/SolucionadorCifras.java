package juegos;

import fichas.*;

class SolucionadorCifras implements Runnable {

    private final CifraSolucionador[] cifrasDisponibles;
    private final StringBuilder stringBuilderOperacion;

    private boolean solucionado;
    private CifraSolucionador cifraObjetivo;
    private int minDiferencia, minCifrasUsadas, minCifrasUsadasInexacto, numCifrasUsadas;

    SolucionadorCifras() {
        cifrasDisponibles = new CifraSolucionador[2 * Cifras.numeroCifras - 1];
        stringBuilderOperacion = new StringBuilder();
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PÚBLICOS **************************************************//
    //***************************************************************************************************************//

    @Override
    public void run() {
        solucionado = false;
        buscarMejorSolucion();
        solucionado = true;
    }

    @Override
    public synchronized String toString() {
        return stringBuilderOperacion.toString();
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PACKAGE ***************************************************//
    //***************************************************************************************************************//

    // Devuelve la distancia a la que el usuario ha quedado de la perfección
    synchronized int getDistanciaPerfeccion(int diferenciaConseguida, int cifrasUsadas) {
        int minCifrasRequeridas = (minDiferencia == 0) ? minCifrasUsadas : minCifrasUsadasInexacto;
        if (diferenciaConseguida == minDiferencia) { // Si se ha conseguido la mejor aproximación posible
            return cifrasUsadas - minCifrasRequeridas;

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

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PRIVADOS **************************************************//
    //***************************************************************************************************************//

    private void actualizarDiferencias(CifraSolucionador resultado) {
        int difActual = Math.abs(cifraObjetivo.getValor() - resultado.getValor());
        if (difActual <= minDiferencia) {
            if (difActual == 0) { // Se ha conseguido el resultado exacto
                if (numCifrasUsadas < minCifrasUsadas) {
                    minCifrasUsadas = numCifrasUsadas;
                    actualizarString(resultado); // Se actualiza la operación a la que da el mejor resultado con las
                    // menos cifras
                    if (minDiferencia > 0)
                        minDiferencia = 0;
                }

            } else { // Todavía no se ha llegado a la cifra objetivo (se utiliza minCifrasInexacto para no interferir
                // con minCifrasUsadas que es sólo para el valor exacto)
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
        stringBuilderOperacion.delete(0, stringBuilderOperacion.length());
        stringBuilderOperacion.append(resultado.getOperacion()).append(" = ").append(resultado.getValor());
    }

    // Añade cifra a las cifras disponibles para que el solucionador pueda utilizarla para realizar sus cálculos
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
        // minCifrasUsadas(Inexacto) tienen que valer un nº grande para que se pueda ir bajando al irse encontrando
        // soluciones
        minCifrasUsadas = Cifras.numeroCifras + 1;
        minCifrasUsadasInexacto = minCifrasUsadas;
        stringBuilderOperacion.delete(0, stringBuilderOperacion.length());

        for (CifraSolucionador cifra: cifrasDisponibles) {
            numCifrasUsadas = 0;
            if (cifra != null && esValorNuevo(cifra))
                combinar(cifra);
        }

    }

    // Realiza todas las combinaciones de operaciones que existen para cifra
    private void combinar(CifraSolucionador cifra) {
        // Sólo si la cifra es de las iniciales, cuenta como usada en el conteo (las que vienen de resultados, no)
        if (esCifraInicial(cifra))
            numCifrasUsadas++;

        if (numCifrasUsadas < minCifrasUsadas) {
            cifra.setUsada(true);

            for (CifraSolucionador siguienteCifra : cifrasDisponibles) {
                if (siguienteCifra != null && !siguienteCifra.isUsada()) {
                    // Sólo importa realizar las operaciones que cumplan la siguiente condición (el resto es redundante)
                    if (cifra.getValor() >= siguienteCifra.getValor()) {
                        if (esCifraInicial(siguienteCifra)) numCifrasUsadas++;

                        if (numCifrasUsadas < minCifrasUsadas) {
                            siguienteCifra.setUsada(true);

                            // Se realizan todas las operaciones posibles entre cifra y siguienteCifra
                            for (Operacion.operacion operador : Operacion.operacion.values()) {
                                if (esOperacionInnecesaria(operador, cifra, siguienteCifra))
                                    continue;

                                CifraSolucionador resultado = operar(cifra, siguienteCifra, operador);
                                if (resultado != null) {
                                    actualizarDiferencias(resultado);
                                    if (resultado.getValor() != cifraObjetivo.getValor()) { // No es solución exacta
                                        combinarIncluyendo(resultado);
                                    }
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

    // Añade resultado a las cifras disponibles y realiza las nuevas combinaciones
    private void combinarIncluyendo(CifraSolucionador resultado) {
        addToCifrasDisponibles(resultado);

        // Se realizan todas las combinaciones posibles habiendo añadido el nuevo
        // resultado al pool de cifras disponibles
        for (CifraSolucionador otraCifra : cifrasDisponibles) {
            if (otraCifra != null && !otraCifra.isUsada())
                combinar(otraCifra);
        }

        borrarDeCifrasDisponibles(resultado);
    }

    // Devuelve si cifra es una de las cifras disponibles al inicio, es decir, no es un resultado
    private boolean esCifraInicial(CifraSolucionador cifra) {
        int i = 0;
        while (i < cifrasDisponibles.length && !cifrasDisponibles[i].equals(cifra))
            i++;

        return i < Cifras.numeroCifras;
    }

    private boolean esOperacionInnecesaria(Operacion.operacion operador, CifraSolucionador cifra1,
                                           CifraSolucionador cifra2) {
        // Cuando se quiere mirar si la/s cifra/s son 1, basta mirar cifra2 ya que cifra2 <= cifra1 siempre
        if (operador.equals(Operacion.operacion.PRODUCTO)) {
            return (cifra2.getValor() == 1) || (cifra1.getValor() == 2 && cifra2.getValor() == 2);
        } else if (operador.equals(Operacion.operacion.DIVISION)) {
            return cifra2.getValor() == 1;
        }
        return false;
    }

    // Devuelve si el valor de cifra no lo tiene una cifra inicial anterior
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
