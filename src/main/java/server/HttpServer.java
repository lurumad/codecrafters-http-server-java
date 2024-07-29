package server;

import client.ClientHandler;
import files.PhysicalFileProvider;
import middleware.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

public class HttpServer {
    private final int port;
    private final Path root;

    public HttpServer(int port, Path root) {
        this.port = port;
        this.root = root;
    }

    public void start() {
        var middleware = Middleware.link(
                new CompressionMiddleware(),
                new RootMiddleware(),
                new EchoMiddleware(),
                new UserAgentMiddleware(),
                new FileMiddleware(new PhysicalFileProvider(root))
        );

        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            System.out.println("Server is listening on port " + port);

            while (!serverSocket.isClosed()) {
                new ClientHandler(serverSocket.accept(), middleware).start();
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
        }
    }
}
