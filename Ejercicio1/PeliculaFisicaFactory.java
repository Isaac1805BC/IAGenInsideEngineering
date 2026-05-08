/**
 * PATRON FACTORY METHOD - fabrica concreta para peliculas fisicas.
 */
public class PeliculaFisicaFactory implements PeliculaFactory {

    @Override
    public Pelicula crearPelicula(String titulo, double precio, boolean disponible) {
        return new PeliculaFisica(titulo, precio, disponible);
    }
}
