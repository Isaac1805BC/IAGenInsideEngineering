/**
 * PATRON STRATEGY - estrategia concreta: membresia con 20% de descuento.
 * SOLID - O (Open/Closed): el porcentaje de descuento esta encapsulado aqui;
 *   cambiar el porcentaje solo afecta esta clase.
 */
public class MembresiaPremium implements Membresia {

    private static final double DESCUENTO = 0.20;

    @Override
    public double aplicarDescuento(double subtotal) {
        return subtotal * (1 - DESCUENTO);
    }

    @Override
    public String getNombre() {
        return "Premium";
    }

    @Override
    public String getDescripcionDescuento() {
        return "Descuento (20%)";
    }

    @Override
    public double getPorcentajeDescuento() {
        return DESCUENTO;
    }
}
