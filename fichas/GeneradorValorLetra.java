package fichas;

import general.Idioma;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GeneradorValorLetra {
    private final HashMap<Character, Integer> frecuenciasConsonantesCastellano = new HashMap<>();
    private final HashMap<Character, Integer> frecuenciasVocalesCastellano = new HashMap<>();

    private final HashMap<Character, Integer> frecuenciasConsonantesCatalan = new HashMap<>();
    private final HashMap<Character, Integer> frecuenciasVocalesCatalan = new HashMap<>();

    private final HashMap<Character, Integer> frecuenciasConsonantesIngles = new HashMap<>();
    private final HashMap<Character, Integer> frecuenciasVocalesIngles = new HashMap<>();

    private final Random random = new Random();

    private HashMap<Character, Integer> frecuenciasConsonantes;
    private HashMap<Character, Integer> frecuenciasVocales;

    public GeneradorValorLetra() {
        rellenarFrecuenciasConsonantesCastellano();
        rellenarFrecuenciasConsonantesCatalan();
        rellenarFrecuenciasConsonantesIngles();
        rellenarFrecuenciasVocalesCastellano();
        rellenarFrecuenciasVocalesCatalan();
        rellenarFrecuenciasVocalesIngles();
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PÚBLICOS **************************************************//
    //***************************************************************************************************************//

    public void setIdioma(Idioma idioma) {
        switch (idioma) {
            case CASTELLANO -> {
                frecuenciasConsonantes = frecuenciasConsonantesCastellano;
                frecuenciasVocales = frecuenciasVocalesCastellano;
            }
            case CATALAN -> {
                frecuenciasConsonantes = frecuenciasConsonantesCatalan;
                frecuenciasVocales = frecuenciasVocalesCatalan;
            }
            case INGLES -> {
                frecuenciasConsonantes = frecuenciasConsonantesIngles;
                frecuenciasVocales = frecuenciasVocalesIngles;
            }
        }
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PACKAGE ***************************************************//
    //***************************************************************************************************************//

    // Devuelve un carácter aleatorio según el tipo especificado
    char getValor(Letra.Tipo tipo) {
        char caracter = '\u0000';
        int numAleatorio = 0, minFrecuencia = 0;
        Set<Map.Entry<Character, Integer>> set = null;

        switch (tipo) {
            case CONSONANTE -> {
                assert frecuenciasConsonantes != null;
                minFrecuencia = frecuenciasConsonantes.get('Z');
                numAleatorio = random.nextInt(minFrecuencia);
                set = frecuenciasConsonantes.entrySet();
            }

            case VOCAL -> {
                assert frecuenciasVocales != null;
                minFrecuencia = frecuenciasVocales.get('U');
                numAleatorio = random.nextInt(minFrecuencia);
                set = frecuenciasVocales.entrySet();
            }
        }

        // Al usarse HashMaps el orden de las entradas no está definido, con lo cual se tienen que recorrer todas las
        // del set buscando la que cumple que el numAleatorio es menor que ella pero no de ninguna otra, por ese
        // motivo se usa minFrecuencia: para guardar el mínimo valor encontrado hasta el momento que cumple lo dicho.
        for (Map.Entry<Character, Integer> entry: set) {
            if (entry.getValue() <= minFrecuencia && numAleatorio < entry.getValue()) {
                minFrecuencia = entry.getValue();
                caracter = entry.getKey();
            }
        }

        return caracter;
    }

    //***************************************************************************************************************//
    //******************************************* MÉTODOS PRIVADOS **************************************************//
    //***************************************************************************************************************//

    private void rellenarFrecuenciasConsonantesCastellano() {
        frecuenciasConsonantesCastellano.put('B', 8647);
        frecuenciasConsonantesCastellano.put('C', 30503);
        frecuenciasConsonantesCastellano.put('D', 45088);
        frecuenciasConsonantesCastellano.put('F', 49632);
        frecuenciasConsonantesCastellano.put('G', 57474);
        frecuenciasConsonantesCastellano.put('H', 62701);
        frecuenciasConsonantesCastellano.put('J', 66456);
        frecuenciasConsonantesCastellano.put('K', 66622);
        frecuenciasConsonantesCastellano.put('L', 88090);
        frecuenciasConsonantesCastellano.put('M', 99695);
        frecuenciasConsonantesCastellano.put('N', 121732);
        frecuenciasConsonantesCastellano.put('Ñ', 123298);
        frecuenciasConsonantesCastellano.put('P', 133573);
        frecuenciasConsonantesCastellano.put('Q', 135283);
        frecuenciasConsonantesCastellano.put('R', 175501);
        frecuenciasConsonantesCastellano.put('S', 191335);
        frecuenciasConsonantesCastellano.put('T', 211058);
        frecuenciasConsonantesCastellano.put('V', 215038);
        frecuenciasConsonantesCastellano.put('W', 215071);
        frecuenciasConsonantesCastellano.put('X', 215773);
        frecuenciasConsonantesCastellano.put('Y', 216952);
        frecuenciasConsonantesCastellano.put('Z', 220925);
    }

    private void rellenarFrecuenciasConsonantesCatalan() {
        frecuenciasConsonantesCatalan.put('B', 17635);
        frecuenciasConsonantesCatalan.put('C', 51770);
        frecuenciasConsonantesCatalan.put('Ç', 52893);
        frecuenciasConsonantesCatalan.put('D', 78679);
        frecuenciasConsonantesCatalan.put('F', 91952);
        frecuenciasConsonantesCatalan.put('G', 115150);
        frecuenciasConsonantesCatalan.put('H', 117017);
        frecuenciasConsonantesCatalan.put('J', 122324);
        frecuenciasConsonantesCatalan.put('K', 122540);
        frecuenciasConsonantesCatalan.put('L', 169119);
        frecuenciasConsonantesCatalan.put('M', 201000);
        frecuenciasConsonantesCatalan.put('N', 260593);
        frecuenciasConsonantesCatalan.put('P', 283648);
        frecuenciasConsonantesCatalan.put('Q', 288552);
        frecuenciasConsonantesCatalan.put('R', 360887);
        frecuenciasConsonantesCatalan.put('S', 405234);
        frecuenciasConsonantesCatalan.put('T', 459217);
        frecuenciasConsonantesCatalan.put('V', 472571);
        frecuenciasConsonantesCatalan.put('W', 472632);
        frecuenciasConsonantesCatalan.put('X', 481517);
        frecuenciasConsonantesCatalan.put('Y', 483839);
        frecuenciasConsonantesCatalan.put('Z', 485495);
    }

    private void rellenarFrecuenciasConsonantesIngles() {
        frecuenciasConsonantesIngles.put('B', 8455);
        frecuenciasConsonantesIngles.put('C', 22421);
        frecuenciasConsonantesIngles.put('D', 39144);
        frecuenciasConsonantesIngles.put('F', 45231);
        frecuenciasConsonantesIngles.put('G', 57622);
        frecuenciasConsonantesIngles.put('H', 66537);
        frecuenciasConsonantesIngles.put('J', 67424);
        frecuenciasConsonantesIngles.put('K', 72173);
        frecuenciasConsonantesIngles.put('L', 92618);
        frecuenciasConsonantesIngles.put('M', 102820);
        frecuenciasConsonantesIngles.put('N', 126300);
        frecuenciasConsonantesIngles.put('P', 137413);
        frecuenciasConsonantesIngles.put('Q', 138163);
        frecuenciasConsonantesIngles.put('R', 166152);
        frecuenciasConsonantesIngles.put('S', 201466);
        frecuenciasConsonantesIngles.put('T', 224896);
        frecuenciasConsonantesIngles.put('V', 228773);
        frecuenciasConsonantesIngles.put('W', 233408);
        frecuenciasConsonantesIngles.put('X', 234596);
        frecuenciasConsonantesIngles.put('Y', 240816);
        frecuenciasConsonantesIngles.put('Z', 242218);
    }

    private void rellenarFrecuenciasVocalesCastellano() {
        frecuenciasVocalesCastellano.put('A', 64706);
        frecuenciasVocalesCastellano.put('E', 102291);
        frecuenciasVocalesCastellano.put('I', 132533);
        frecuenciasVocalesCastellano.put('O', 175146);
        frecuenciasVocalesCastellano.put('U', 190023);
    }

    private void rellenarFrecuenciasVocalesCatalan() {
        frecuenciasVocalesCatalan.put('A', 119113);
        frecuenciasVocalesCatalan.put('E', 226839);
        frecuenciasVocalesCatalan.put('I', 302472);
        frecuenciasVocalesCatalan.put('O', 353159);
        frecuenciasVocalesCatalan.put('U', 395728);
    }

    private void rellenarFrecuenciasVocalesIngles() {
        frecuenciasVocalesIngles.put('A', 27851);
        frecuenciasVocalesIngles.put('E', 73677);
        frecuenciasVocalesIngles.put('I', 103057);
        frecuenciasVocalesIngles.put('O', 125249);
        frecuenciasVocalesIngles.put('U', 138467);
    }
}
