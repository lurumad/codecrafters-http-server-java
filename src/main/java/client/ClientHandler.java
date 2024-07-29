package client;

import http.HttpRequest;
import middleware.Middleware;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final Middleware middleware;

    public ClientHandler(Socket socket, Middleware middleware) {
        this.socket = socket;
        this.middleware = middleware;
    }

    public void run() {
        try (var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            var request = HttpRequest.parse(reader);
            try (var output = socket.getOutputStream()) {
                var response = middleware.handle(request);
                response.write(output);
            }
        } catch (Exception ex) {
            System.out.println("client.ClientHandler exception: " + ex.getMessage());
        }
    }
}
