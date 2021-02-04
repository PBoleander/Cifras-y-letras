package controlTiempo;

import general.Colores;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MostradorTiempo extends JProgressBar implements PropertyChangeListener {

    private final Cronometro cronometro;

    private boolean empezado;
    private LectorTiempo lectorTiempo;

    public MostradorTiempo() {
        super();

        this.cronometro = new Cronometro();
        this.empezado = false;

        setStringPainted(true);
        setString(cronometro.toString());
    }

    @Override
    // El progreso de lectorTiempo cambia
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String tRestante = cronometro.toString();
        int progress = lectorTiempo.getProgress();

        if (tRestante.equals("00:10")) setForeground(Color.RED);
        else if (progress <= 50 && getForeground().equals(Colores.VERDE)) setForeground(Colores.NARANJA);

        setValue(progress);
        setString(tRestante);

        if (lectorTiempo.isDone()) empezado = false;
    }

    public boolean haEmpezado() {
        return empezado;
    }

    public void iniciar(boolean contrarreloj) {
        empezado = true;
        setForeground(Colores.VERDE);

        if (contrarreloj) {
            lectorTiempo = new LectorTiempo(cronometro);
            lectorTiempo.addPropertyChangeListener(this);
            lectorTiempo.execute(); // Ejecuta su doInBackground()
        } else {
            setString("Infinito");
            setValue(100);
        }
    }

    public void parar(boolean contrarreloj) {
        if (contrarreloj) {
            cronometro.parar();
            lectorTiempo.cancel(true);
        }

        empezado = false;
    }

    public void setPausa(boolean pausa) {
        cronometro.setPausa(pausa);
    }

    public void setSegundosIniciales(int segundosIniciales) {
        cronometro.setSegundosIniciales(segundosIniciales);
    }
}
