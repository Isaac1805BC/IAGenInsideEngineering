import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("    VIDEOCLUB DE DON MARIO");
        System.out.println("=================================");

        Scanner scanner = new Scanner(System.in);
        Videoclub videoclub = new Videoclub();
        videoclub.procesarAlquiler(scanner);
        scanner.close();
    }
}
