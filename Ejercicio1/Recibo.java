import java.util.List;

/**
 * ENCAPSULAMIENTO: encapsula toda la logica de presentacion del recibo.
 * SOLID:
 *  - S (Single Responsibility): unica responsabilidad es generar e imprimir el recibo.
 *  - D (Dependency Inversion): recibe Membresia (interfaz) y List<Pelicula> (abstraccion).
 */
public class Recibo {

    private final Membresia membresia;
    private final List<Pelicula> peliculasAlquiladas;

    public Recibo(Membresia membresia, List<Pelicula> peliculasAlquiladas) {
        this.membresia = membresia;
        this.peliculasAlquiladas = peliculasAlquiladas;
    }

    public void imprimir() {
        double subtotal = peliculasAlquiladas.stream()
                .mapToDouble(Pelicula::getPrecio)
                .sum();

        double total     = membresia.aplicarDescuento(subtotal);
        double descuento = subtotal - total;

        System.out.println("\n--- RECIBO DE ALQUILER ---");
        System.out.println("Cliente: " + membresia.getNombre());
        System.out.println("Peliculas:");

        /* POLIMORFISMO: se llama getTipo() sobre referencias Pelicula; cada subclase
           responde con su propio tipo sin condicionales. */
        for (Pelicula p : peliculasAlquiladas) {
            System.out.printf("  - %s (%s) - %s%n",
                    p.getTitulo(), p.getTipo(), Util.formatearPrecio(p.getPrecio()));
        }

        System.out.printf("Subtotal: %s%n", Util.formatearPrecio(subtotal));

        if (descuento > 0) {
            System.out.printf("%s: %s%n",
                    membresia.getDescripcionDescuento(),
                    Util.formatearPrecio(descuento));
        }

        System.out.printf("Total a pagar: %s%n", Util.formatearPrecio(total));
        System.out.println("--------------------------");
        System.out.println("¡Disfrute su pelicula!");
    }
}
