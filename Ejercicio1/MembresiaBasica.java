/**
 * PATRON STRATEGY - estrategia concreta: membresia sin descuento.
 * SOLID - O (Open/Closed): agregar nuevas membresías no requiere modificar esta clase.
 */
public class MembresiaBasica implements Membresia {

    @Override
    public double aplicarDescuento(double subtotal) {
        return subtotal;
    }

    @Override
    public String getNombre() {
        return "Basica";
    }

    @Override
    public String getDescripcionDescuento() {
        return "Sin descuento";
    }

    @Override
    public double getPorcentajeDescuento() {
        return 0.0;
    }
}
