import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * SOLID - S (Single Responsibility): utilidad de formato de precios aislada
 * para que Recibo y Pelicula no dupliquen esta logica.
 */
public class Util {

    private Util() {}

    /** Formatea un precio con punto como separador de miles (estilo colombiano). */
    public static String formatearPrecio(double precio) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,###", symbols);
        return "$" + df.format((long) precio);
    }
}
