package fichas;

public class CifraSolucionador {

    private final int valor;
    private boolean usada;
    private final OperacionSolucionador operacion;

    public CifraSolucionador(int valor) {
        this(valor, null);
    }

    public CifraSolucionador(int valor, OperacionSolucionador operacion) {
        this.usada = false;
        this.valor = valor;
        this.operacion = operacion;
    }

    public OperacionSolucionador getOperacion() {
        return operacion;
    }

    public int getValor() {
        return valor;
    }

    public boolean isUsada() {
        return usada;
    }

    public void setUsada(boolean usada) {
        this.usada = usada;
    }
}
