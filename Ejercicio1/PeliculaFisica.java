/**
 * POLIMORFISMO: extiende Pelicula e implementa getTipo() con comportamiento especifico.
 * SOLID - L (Liskov): puede usarse en cualquier lugar donde se espera una Pelicula.
 */
public class PeliculaFisica extends Pelicula {

    public PeliculaFisica(String titulo, double precio, boolean disponible) {
        super(titulo, precio, disponible);
    }

    @Override
    public String getTipo() {
        return "Fisica";
    }
}
