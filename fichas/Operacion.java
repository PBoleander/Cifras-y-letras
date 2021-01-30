package fichas;

import java.awt.*;

public class Operacion extends Cifra {

    public enum operacion {
        SUMA,
        RESTA,
        PRODUCTO,
        DIVISION;

        @Override
        public String toString() {
            switch (this) {
                case SUMA -> {
                    return "+";
                }
                case RESTA -> {
                    return "-";
                }
                case PRODUCTO -> {
                    return "×";
                }
                case DIVISION -> {
                    return "÷";
                }
                default -> {
                    return "";
                }
            }
        }
    }

    private operacion operador;
    private Cifra operando1, operando2, resultado;

    public Operacion() {
        super();

        setPreferredSize(new Dimension(5 * Ficha.ANCHO, Ficha.ALTO));
    }

    public Operacion(Operacion operacion) {
        super(operacion);

        operando1 = operacion.operando1;
        operando2 = operacion.operando2;
        operador = operacion.operador;
        resultado = operacion.resultado;

        setText(toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (operando1 != null) {
            sb.append(operando1);
            if (operador != null) {
                sb.append(" ").append(operador);
                if (operando2 != null)
                    sb.append(" ").append(operando2).append(" = ").append(resultado);
            }
        }

        return sb.toString();
    }

    public boolean borrarUltimoElemento() {
        if (operando2 != null) {
            resultado = null;
            operando2.setUsada(false);
            operando2 = null;

        } else if (operador != null) {
            operador = null;

        } else if (operando1 != null) {
            operando1.setUsada(false);
            operando1 = null;

        } else return false;

        setText(toString());
        return true;
    }

    public Cifra getOperando1() {
        return operando1;
    }

    public Cifra getResultado() {
        return resultado;
    }

    public boolean setOperador(operacion operador) {
        if (operador != null && operando1 != null && operando2 == null) {
            this.operador = operador;
            setText(toString());
            return true;
        }
        return false;
    }

    public void setOperando(Cifra cifra) {
        if (cifra != null) {
            if (cifra instanceof Operacion) {
                if (((Operacion) cifra).resultado == null)
                    return;
                else
                    cifra = ((Operacion) cifra).resultado;
            }
            cifra.setUsada(true);

            if (operador == null) {
                if (operando1 != null)
                    borrarUltimoElemento();

                this.operando1 = cifra;

            } else {
                this.operando2 = cifra;
                this.resultado = calcular();

                if (resultado == null) {
                    borrarUltimoElemento();
                    return;
                }
            }

            setText(toString());
        }
    }

    private Cifra calcular() {
        return switch (operador) {
            case SUMA -> suma();
            case RESTA -> resta();
            case PRODUCTO -> producto();
            case DIVISION -> division();
        };
    }

    private Cifra division() {
        if (operando1.getValor() % operando2.getValor() == 0) {
            return new Cifra(operando1.getValor() / operando2.getValor(), this);
        }

        return null;
    }

    private Cifra producto() {
        return new Cifra(operando1.getValor() * operando2.getValor(), this);
    }

    private Cifra resta() {
        int resultado = operando1.getValor() - operando2.getValor();
        return (resultado > 0) ? new Cifra(resultado, this) : null;
    }

    private Cifra suma() {
        return new Cifra(operando1.getValor() + operando2.getValor(), this);
    }
}
