package server;

import client.ClientHandler;
import middleware.EchoMiddleware;
import middleware.Middleware;
import middleware.RootMiddleware;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() {
        var middleware = Middleware.link(
                new RootMiddleware(),
                new EchoMiddleware()
        );

        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket, middleware).start();
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
        }
    }
}
