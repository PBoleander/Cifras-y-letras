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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        OperacionSolucionador operacion1 = operando1.getOperacion();
        OperacionSolucionador operacion2 = operando2.getOperacion();

        if (operacion1 != null) stringOperacion(stringBuilder, operacion1);
        else stringBuilder.append(operando1.getValor());

        stringBuilder.append(" ").append(operador).append(" ");

        if (operacion2 != null) stringOperacion(stringBuilder, operacion2);
        else stringBuilder.append(operando2.getValor());

        return stringBuilder.toString();
    }

    public Operacion.operacion getOperador() {
        return operador;
    }

    public CifraSolucionador getResultado() {
        return resultado;
    }

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

    private CifraSolucionador producto() {
        return new CifraSolucionador(operando1.getValor() * operando2.getValor(), this);
    }

    private CifraSolucionador resta() {
        int resultado = operando1.getValor() - operando2.getValor();
        return (resultado > 0) ? new CifraSolucionador(resultado, this) : null;
    }

    private void stringOperacion(StringBuilder stringBuilder, OperacionSolucionador operacion) {
        if (!operador.equals(Operacion.operacion.SUMA)) {
            if (operacion.getOperador().equals(Operacion.operacion.SUMA) ||
                    operacion.getOperador().equals(Operacion.operacion.RESTA)) {

                stringBuilder.append("(").append(operacion).append(")");
            } else {
                stringBuilder.append(operacion);
            }
        } else {
            stringBuilder.append(operacion);
        }
    }

    private CifraSolucionador suma() {
        return new CifraSolucionador(operando1.getValor() + operando2.getValor(), this);
    }
}
