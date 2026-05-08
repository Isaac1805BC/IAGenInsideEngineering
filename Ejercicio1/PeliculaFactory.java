/**
 * PATRON FACTORY METHOD: interfaz de fabrica que declara el metodo de creacion
 * de peliculas. Cada fabrica concreta decide que tipo de Pelicula instanciar.
 *
 * SOLID:
 *  - O (Open/Closed): anadir nuevos tipos de pelicula implica crear una nueva
 *    fabrica, sin modificar las existentes.
 *  - D (Dependency Inversion): Videoclub depende de esta abstraccion para
 *    crear peliculas, no de las clases concretas.
 */
public interface PeliculaFactory {
    Pelicula crearPelicula(String titulo, double precio, boolean disponible);
}
