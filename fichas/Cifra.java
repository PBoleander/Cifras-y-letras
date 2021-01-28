package fichas;

import java.awt.*;
import java.util.Random;

public class Cifra extends Ficha {

    public static final Random RANDOM = new Random();

    private static final int[] VALORES_POSIBLES = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 25, 50, 75, 100 };

    private int valor;
    private Operacion operacion;

    public Cifra() {
        super();

        nuevoValor();
    }

    public Cifra(int valor) {
        this(valor, null);
    }

    public Cifra (int valor, Operacion operacion) {
        super();

        this.valor = valor;
        this.operacion = operacion;
        setText(String.valueOf(valor));
    }

    @Override
    public String toString() {
        return String.valueOf(valor);
    }

    public Operacion getOperacion() {
        return operacion;
    }

    public int getValor() {
        return valor;
    }

    @Override
    public void setUsada(boolean usada) {
        super.setUsada(usada);

        if (isUsada()) setBackground(Color.GRAY);
        else setBackground(Ficha.BACKGROUND);

        if (operacion != null)
            operacion.setUsada(usada);
    }

    private void nuevoValor() {
        int indiceAleatorio = RANDOM.nextInt(VALORES_POSIBLES.length);
        this.valor = VALORES_POSIBLES[indiceAleatorio];

        setText(String.valueOf(valor));
    }
}
