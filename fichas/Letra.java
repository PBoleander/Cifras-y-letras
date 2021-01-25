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

    public Letra(Letra letra) {
        super();

        valor = letra.getValor();
    }

    public Letra(char caracter) {
        super();

        valor = caracter;
        setText(String.valueOf(valor));
    }

    @Override
    public String toString() {
        return String.valueOf(valor);
    }

    public char getValor() {
        return valor;
    }

    private void nuevoValor(Tipo tipo) {
        this.valor = generador.getValor(tipo);

        setText(String.valueOf(valor));
    }
}
