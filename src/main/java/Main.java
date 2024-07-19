import server.HttpServer;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            var server = new HttpServer(4221, Path.of("/tmp"));
            server.start();
        }

        var root = args[1];
        var server = new HttpServer(4221, Path.of(root));
        server.start();
    }
}
