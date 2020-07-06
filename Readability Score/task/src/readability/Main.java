package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        Readability readability = new Readability(Files.readString(Paths.get(args[0])));
        readability.print();
    }
}
