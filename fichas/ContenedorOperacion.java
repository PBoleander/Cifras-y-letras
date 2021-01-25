package fichas;

import java.awt.*;

public class ContenedorOperacion extends ContenedorFicha {

    public ContenedorOperacion(Operacion ficha) {
        super(ficha);

        setPreferredSize(new Dimension(5 * Ficha.ANCHO, Ficha.ALTO));
    }

    public boolean estaEmpezado() {
        Operacion operacion = (Operacion) getFicha();

        return operacion != null && operacion.getOperando1() != null;
    }

    public boolean estaCompleto() {
        Operacion operacion = (Operacion) getFicha();

        return operacion != null && operacion.getResultado() != null;
    }
}
