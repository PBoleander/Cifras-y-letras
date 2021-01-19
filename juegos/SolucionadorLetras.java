package juegos;

import fichas.Letra;
import general.Idioma;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

class SolucionadorLetras implements Runnable {

    private final ArrayList<String> listaCastellano, listaCatalan, listaIngles;
    private final DefaultListModel<String> listaSolucion;
    private final Letra[] letrasDisponibles;

    private ArrayList<String> listaPalabras;
    private int maxLongitudPosible;

    SolucionadorLetras() {
        this.listaCastellano = new ArrayList<>();
        this.listaCatalan = new ArrayList<>();
        this.listaIngles = new ArrayList<>();

        this.listaSolucion = new DefaultListModel<>();

        this.letrasDisponibles = new Letra[Letras.numeroLetras];
    }

    @Override
    public void run() {
        if (!listaSolucion.isEmpty())
            listaSolucion.clear();

        llenarListaSolucion();
    }

    DefaultListModel<String> getListaSolucion() {
        return listaSolucion;
    }

    int getMaxLongitudPosible() {
        return maxLongitudPosible;
    }

    int getNumLongitudesMejores(int longitud) {
        int n = 0;

        while (longitud < maxLongitudPosible) {
            if (contiene(String.valueOf(++longitud)))
                n++;
        }

        return n;
    }

    void setIdioma(Idioma idioma) {
        switch (idioma) {
            case CASTELLANO -> listaPalabras = listaCastellano;
            case CATALAN -> listaPalabras = listaCatalan;
            case INGLES -> listaPalabras = listaIngles;
        }

        if (listaPalabras.isEmpty()) cargarDiccionario(idioma);
    }

    void setLetrasDisponibles(Letra[] letrasDisponibles) {
        for (int i = 0; i < Letras.numeroLetras; i++) {
            this.letrasDisponibles[i] = new Letra(letrasDisponibles[i]);
        }
    }

    boolean contiene(String palabra) {
        return listaSolucion.contains(palabra);
    }

    private boolean cambiaLongitud(String palabra) {
        return listaSolucion.isEmpty() || palabra.length() != listaSolucion.lastElement().length();
    }

    private void cargarDiccionario(Idioma idioma) {
        String ruta = "/diccionarios/";
        switch (idioma) {
            case CASTELLANO -> ruta += "dicc_es.txt";
            case CATALAN -> ruta += "dicc_ca.txt";
            case INGLES -> ruta += "dicc_en.txt";
        }

        InputStream in = getClass().getResourceAsStream(ruta);
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        String palabra;

        try {
            while ((palabra = bf.readLine()) != null) {
                listaPalabras.add(palabra.toUpperCase());
            }

            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void llenarListaSolucion() {
        maxLongitudPosible = 0;

        for (String palabra: listaPalabras) {
            if (palabra.length() > 1 && sePuedeFormar(palabra)) {
                if (cambiaLongitud(palabra)) {
                    if (maxLongitudPosible == 0) maxLongitudPosible = palabra.length();

                    listaSolucion.addElement(String.valueOf(palabra.length()));
                }
                listaSolucion.addElement(palabra);
            }
        }
    }

    private boolean sePuedeFormar(String palabra) {
        for (Letra letra: letrasDisponibles)
            if (letra.isUsada()) letra.setUsada(false);

        char[] charsDePalabra = palabra.toCharArray();

        for (char c: charsDePalabra) {
            int i = 0;
            while (i < Letras.numeroLetras && (letrasDisponibles[i].isUsada() || letrasDisponibles[i].getValor() != c))
                i++;

            if (i == Letras.numeroLetras) return false;
            else letrasDisponibles[i].setUsada(true);
        }

        return true;
    }
}
