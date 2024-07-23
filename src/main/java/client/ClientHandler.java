package client;

import http.HttpRequest;
import http.aux.CRLFString;
import middleware.Middleware;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
                response.write(output);
            }
        } catch (Exception ex) {
            System.out.println("client.ClientHandler exception: " + ex.getMessage());
        }
    }

    private String readHttpRequest(BufferedReader input) throws IOException {
        String line;
        Integer contentLength = null;
        var lines = new StringBuilder();
        while (!Objects.equals(line = input.readLine(), "") && !line.isEmpty()) {
            lines.append(new CRLFString(line));

            if (line.toLowerCase().startsWith("content-length")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
        }

        lines.append(new CRLFString());

        if (contentLength != null) {
            var body = new char[contentLength];
            input.read(body, 0, contentLength);
            lines.append(body);
        }

        return lines.toString();
    }
}
