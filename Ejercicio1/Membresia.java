/**
 * PATRON STRATEGY: interfaz que define el algoritmo de descuento intercambiable.
 * Cada implementacion encapsula una estrategia de precio diferente.
 *
 * SOLID:
 *  - S (Single Responsibility): solo se ocupa de la logica de descuento.
 *  - I (Interface Segregation): interfaz cohesiva y minima; no obliga a implementar
 *    metodos innecesarios.
 *  - D (Dependency Inversion): Videoclub y Recibo dependen de esta abstraccion,
 *    no de clases concretas.
 */
public interface Membresia {

    /** Aplica el descuento correspondiente al subtotal dado. */
    double aplicarDescuento(double subtotal);

    String getNombre();

    /** Texto descriptivo del descuento para imprimir en el recibo. */
    String getDescripcionDescuento();

    double getPorcentajeDescuento();
}
