package fichas;

import general.Idioma;

import java.util.Random;

public class GeneradorValorLetra {

    private final char[] CONSONANTES_CASTELLANO = "BCDFGHJKLMNÑPQRSTVWXYZ".toCharArray();
    private final char[] CONSONANTES_CATALAN = "BCÇDFGHJKLMNPQRSTVWXYZ".toCharArray();
    private final char[] CONSONANTES_INGLES = "BCDFGHJKLMNPQRSTVWXYZ".toCharArray();
    private final char[] VOCALES = "AEIOU".toCharArray();

    private final int[] FRECUENCIAS_CONSONANTES_CASTELLANO =
            {8647, 30503, 45088, 49632, 57474, 62701, 66456, 66622, 88090, 99695, 121732, 123298, 133573, 135283,
                    175501, 191335, 211058, 215038, 215071, 215773, 216952, 220925};
    private final int[] FRECUENCIAS_VOCALES_CASTELLANO = {64706, 102291, 132533, 175146, 190023};

    private final int[] FRECUENCIAS_CONSONANTES_CATALAN =
            {17635, 51770, 77556, 90829, 114027, 115894, 121201, 121417, 167996, 199877, 259470, 282525, 287429, 359764,
                    404111, 458094, 471448, 471509, 480394, 482716, 484372, 485495};
    private final int[] FRECUENCIAS_VOCALES_CATALAN = {119113, 226839, 302472, 353159, 395728};

    private final int[] FRECUENCIAS_CONSONANTES_INGLES =
            {8455, 22421, 39144, 45231, 57622, 66537, 67424, 72173, 92618, 102820, 126300, 137413, 138163, 166152,
                    201466, 224896, 228773, 233408, 234596, 240816, 242218};
    private final int[] FRECUENCIAS_VOCALES_INGLES = {27851, 73677, 103057, 125249, 138467};

    private final Random RANDOM = new Random();

    private char[] valoresConsonantesPosibles;
    private int[] frecuenciasConsonantes;
    private int[] frecuenciasVocales;

    public GeneradorValorLetra() {}

    public void setIdioma(Idioma idioma) {
        switch (idioma) {
            case CASTELLANO -> {
                valoresConsonantesPosibles = CONSONANTES_CASTELLANO;
                frecuenciasConsonantes = FRECUENCIAS_CONSONANTES_CASTELLANO;
                frecuenciasVocales = FRECUENCIAS_VOCALES_CASTELLANO;
            }
            case CATALAN -> {
                valoresConsonantesPosibles = CONSONANTES_CATALAN;
                frecuenciasConsonantes = FRECUENCIAS_CONSONANTES_CATALAN;
                frecuenciasVocales = FRECUENCIAS_VOCALES_CATALAN;
            }
            case INGLES -> {
                valoresConsonantesPosibles = CONSONANTES_INGLES;
                frecuenciasConsonantes = FRECUENCIAS_CONSONANTES_INGLES;
                frecuenciasVocales = FRECUENCIAS_VOCALES_INGLES;
            }
        }
    }

    char getValor(Letra.Tipo tipo) {
        int indiceAleatorio = obtenerIndiceFrecuencias(tipo);

        if (indiceAleatorio != -1) {
            switch (tipo) {
                case CONSONANTE -> {
                    assert valoresConsonantesPosibles != null;
                    return valoresConsonantesPosibles[indiceAleatorio];
                }

                case VOCAL -> {
                    return VOCALES[indiceAleatorio];
                }
            }
        }

        return '\u0000'; // Aquí no debe llegar
    }

    private int obtenerIndiceFrecuencias(Letra.Tipo tipo) {
        int numAleatorio;

        switch (tipo) {
            case CONSONANTE -> {
                assert frecuenciasConsonantes != null;
                numAleatorio = RANDOM.nextInt(frecuenciasConsonantes[frecuenciasConsonantes.length - 1]);

                for (int i = 0; i < frecuenciasConsonantes.length; i++) {
                    if (numAleatorio < frecuenciasConsonantes[i])
                        return i;
                }
            }

            case VOCAL -> {
                assert frecuenciasVocales != null;
                numAleatorio = RANDOM.nextInt(frecuenciasVocales[frecuenciasVocales.length - 1]);

                for (int i = 0; i < frecuenciasVocales.length; i++) {
                    if (numAleatorio < frecuenciasVocales[i])
                        return i;
                }
            }
        }

        return -1; // Aquí no debe llegar
    }
}
