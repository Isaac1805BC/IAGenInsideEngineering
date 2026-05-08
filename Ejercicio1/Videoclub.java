import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Orquesta el flujo de alquiler del videoclub.
 *
 * SOLID:
 *  - S (Single Responsibility): gestiona el catalogo y el proceso de alquiler.
 *  - D (Dependency Inversion): trabaja con Membresia y PeliculaFactory como
 *    abstracciones; no depende de clases concretas.
 *
 * ENCAPSULAMIENTO: el catalogo interno es privado; solo se expone mediante
 * metodos controlados.
 */
public class Videoclub {

    private final List<Pelicula> catalogo = new ArrayList<>();

    public Videoclub() {
        cargarCatalogo();
    }

    private void cargarCatalogo() {
        PeliculaFactory fisica   = new PeliculaFisicaFactory();
        PeliculaFactory digital  = new PeliculaDigitalFactory();

        catalogo.add(fisica .crearPelicula("Interestellar", 8000, true));
        catalogo.add(fisica .crearPelicula("El Padrino",    7000, false));
        catalogo.add(digital.crearPelicula("Inception",     5000, true));
        catalogo.add(digital.crearPelicula("Matrix",        6000, true));
    }

    public void mostrarCatalogo() {
        System.out.println("\n=== CATALOGO DE PELICULAS ===");
        for (int i = 0; i < catalogo.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, catalogo.get(i));
        }
        System.out.println("=============================");
    }

    /** PATRON STRATEGY: el cliente elige la estrategia de precio (membresia). */
    public Membresia elegirMembresia(Scanner sc) {
        System.out.println("\nTipo de membresia:");
        System.out.println("  1. Basica  (precio normal)");
        System.out.println("  2. Premium (20% de descuento)");
        System.out.print("Seleccione membresia (1/2): ");

        String opcion = sc.nextLine().trim();
        return "2".equals(opcion) ? new MembresiaPremium() : new MembresiaBasica();
    }

    public List<Pelicula> elegirPeliculas(Scanner sc) {
        System.out.print("\nSeleccione peliculas (numeros separados por coma): ");
        String[] tokens = sc.nextLine().trim().split(",");

        List<Pelicula> seleccion = new ArrayList<>();
        for (String token : tokens) {
            try {
                int idx = Integer.parseInt(token.trim()) - 1;
                if (idx < 0 || idx >= catalogo.size()) {
                    System.out.printf("  [!] Numero %d fuera de rango, ignorado.%n", idx + 1);
                    continue;
                }
                Pelicula p = catalogo.get(idx);
                if (!p.isDisponible()) {
                    System.out.printf("  [!] '%s' no esta disponible y no se incluye.%n",
                            p.getTitulo());
                    continue;
                }
                seleccion.add(p);
            } catch (NumberFormatException e) {
                System.out.printf("  [!] Valor '%s' no es un numero valido, ignorado.%n",
                        token.trim());
            }
        }
        return seleccion;
    }

    public void procesarAlquiler(Scanner sc) {
        mostrarCatalogo();
        Membresia membresia = elegirMembresia(sc);
        List<Pelicula> seleccion = elegirPeliculas(sc);

        if (seleccion.isEmpty()) {
            System.out.println("\nNo hay peliculas disponibles en la seleccion. Hasta pronto.");
            return;
        }

        new Recibo(membresia, seleccion).imprimir();
    }
}
