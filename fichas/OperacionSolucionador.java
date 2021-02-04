package fichas;

public class OperacionSolucionador {

    private final CifraSolucionador operando1, operando2, resultado;
    private final Operacion.operacion operador;

    public OperacionSolucionador(CifraSolucionador operando1, CifraSolucionador operando2,
                                 Operacion.operacion operador) {
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.operador = operador;
        this.resultado = calcular();
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PÚBLICOS **************************************************//
    //***************************************************************************************************************//

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        OperacionSolucionador operacion1 = operando1.getOperacion();
        OperacionSolucionador operacion2 = operando2.getOperacion();

        /* Si alguno de los operandos viene de una operación anterior, se escribirá ésta */

        if (operacion1 != null) stringOperacion(stringBuilder, operacion1, false);
        else stringBuilder.append(operando1.getValor());

        stringBuilder.append(" ").append(operador).append(" ");

        if (operacion2 != null) stringOperacion(stringBuilder, operacion2, true);
        else stringBuilder.append(operando2.getValor());

        return stringBuilder.toString();
    }

    public Operacion.operacion getOperador() {
        return operador;
    }

    public CifraSolucionador getResultado() {
        return resultado;
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PRIVADOS **************************************************//
    //***************************************************************************************************************//

    private CifraSolucionador calcular() {
        return switch (operador) {
            case SUMA -> suma();
            case RESTA -> resta();
            case PRODUCTO -> producto();
            case DIVISION -> division();
        };
    }

    private CifraSolucionador division() {
        if (operando1.getValor() % operando2.getValor() == 0) {
            return new CifraSolucionador(operando1.getValor() / operando2.getValor(), this);
        }

        return null;
    }

    // Indica si la operación ha de ir entre paréntesis
    private boolean llevaParentesis(Operacion.operacion elOtroOperador, boolean segundoOperando) {
        if (!this.operador.equals(Operacion.operacion.SUMA)) {
            if (segundoOperando || !this.operador.equals(Operacion.operacion.RESTA)) {
                return elOtroOperador.equals(Operacion.operacion.SUMA) ||
                        elOtroOperador.equals(Operacion.operacion.RESTA);
            }
        }
        return false;
    }

    private CifraSolucionador producto() {
        return new CifraSolucionador(operando1.getValor() * operando2.getValor(), this);
    }

    private CifraSolucionador resta() {
        int resultado = operando1.getValor() - operando2.getValor();
        return (resultado > 0) ? new CifraSolucionador(resultado, this) : null;
    }

    // Añade al stringBuilder la operación
    private void stringOperacion(StringBuilder stringBuilder, OperacionSolucionador operacion, boolean segundoOperando)
    {
        if (llevaParentesis(operacion.getOperador(), segundoOperando))
            stringBuilder.append("(").append(operacion).append(")");
        else
            stringBuilder.append(operacion);
    }

    private CifraSolucionador suma() {
        return new CifraSolucionador(operando1.getValor() + operando2.getValor(), this);
    }
}
