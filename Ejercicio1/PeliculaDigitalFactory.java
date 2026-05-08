/**
 * PATRON FACTORY METHOD - fabrica concreta para peliculas digitales.
 */
public class PeliculaDigitalFactory implements PeliculaFactory {

    @Override
    public Pelicula crearPelicula(String titulo, double precio, boolean disponible) {
        return new PeliculaDigital(titulo, precio, disponible);
    }
}
