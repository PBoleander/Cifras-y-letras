package fichas;

import java.util.Random;

public class Cifra extends Ficha {

    private static final int[] VALORES_POSIBLES = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 25, 50, 75, 100 };
    private static final Random RANDOM = new Random();

    private int valor;

    public Cifra() {
        super();

        nuevoValor();
    }

    public Cifra(Cifra cifra) {
        super();

        valor = cifra.getValor();
    }

    public Cifra(int valor) {
        super();

        this.valor = valor;
        setText(String.valueOf(valor));
    }

    public int getValor() {
        return valor;
    }

    private void nuevoValor() {
        int indiceAleatorio = RANDOM.nextInt(VALORES_POSIBLES.length);
        this.valor = VALORES_POSIBLES[indiceAleatorio];

        setText(String.valueOf(valor));
    }
}
