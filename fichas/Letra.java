package fichas;

public class Letra extends Ficha {

    public enum Tipo {
        CONSONANTE, VOCAL
    }

    private static final GeneradorValorLetra generador = new GeneradorValorLetra();

    private char valor;

    public Letra() {
        super();
    }

    public char getValor() {
        return valor;
    }

    public void nuevoValor(Tipo tipo) {
        this.valor = generador.getValor(tipo);

        setText(String.valueOf(valor));
    }
}
