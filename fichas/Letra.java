package fichas;

public class Letra extends Ficha {

    public enum Tipo {
        CONSONANTE, VOCAL
    }

    public static final GeneradorValorLetra generador = new GeneradorValorLetra();

    private char valor;

    public Letra(Tipo tipo) {
        super();

        nuevoValor(tipo);
    }

    public char getValor() {
        return valor;
    }

    private void nuevoValor(Tipo tipo) {
        this.valor = generador.getValor(tipo);

        setText(String.valueOf(valor));
    }
}
