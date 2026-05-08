/**
 * POLIMORFISMO: clase abstracta base que define el contrato para todos los tipos de pelicula.
 * ENCAPSULAMIENTO: atributos privados expuestos unicamente mediante getters/setters controlados.
 *
 * SOLID:
 *  - S (Single Responsibility): solo representa los datos y comportamiento de una pelicula.
 *  - O (Open/Closed): cerrada a modificacion; abierta para extension (PeliculaFisica, PeliculaDigital).
 *  - L (Liskov Substitution): cualquier subclase puede reemplazar a Pelicula sin alterar el sistema.
 */
public abstract class Pelicula {

    private String titulo;
    private double precio;
    private boolean disponible;

    public Pelicula(String titulo, double precio, boolean disponible) {
        this.titulo = titulo;
        this.precio = precio;
        this.disponible = disponible;
    }

    /** Metodo abstracto: obliga a cada subclase a declarar su tipo (polimorfismo). */
    public abstract String getTipo();

    public String getTitulo()           { return titulo; }
    public double getPrecio()           { return precio; }
    public boolean isDisponible()       { return disponible; }
    public void setDisponible(boolean d){ this.disponible = d; }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s - %s",
                getTipo(), titulo,
                Util.formatearPrecio(precio),
                disponible ? "Disponible" : "No disponible");
    }
}
