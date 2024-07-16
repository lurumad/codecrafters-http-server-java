package client;

import http.HttpRequest;
import http.HttpResponse;
import http.aux.CRLFString;
import middleware.Middleware;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final Middleware middleware;

    public ClientHandler(Socket socket, Middleware middleware) {
        this.socket = socket;
        this.middleware = middleware;
    }

    public void run() {
        try (var input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            var request = HttpRequest.parse(readHttpRequest(input));
            try (var output = socket.getOutputStream()) {
                var response = middleware.handle(request);
                output.write(response.toString().getBytes());
            }
        } catch (Exception ex) {
            System.out.println("client.ClientHandler exception: " + ex.getMessage());
        }
    }

    private String readHttpRequest(BufferedReader input) throws IOException {
        String line;
        var lines = new StringBuilder();
        while (!Objects.equals(line = input.readLine(), "")) {
            lines.append(new CRLFString(line));
        }
        return lines.toString();
    }
}
