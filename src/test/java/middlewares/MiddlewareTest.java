package middlewares;

import http.HttpRequest;
import middleware.EchoMiddleware;
import middleware.Middleware;
import middleware.RootMiddleware;
import middleware.UserAgentMiddleware;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiddlewareTest {
    @Test
    public void handle_root_path() throws IOException {
        var middleware = Middleware.link(
            new RootMiddleware()
        );
        var request = HttpRequest.parse("GET / HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.64.1\r\nAccept: */*\r\n\r\n");
        var response = middleware.handle(request);
        var outputStream = new ByteArrayOutputStream();
        response.write(outputStream);
        assertEquals("HTTP/1.1 200 OK\r\n\r\n", outputStream.toString());
    }

    @Test
    public void handle_not_found() throws IOException {
        var middleware = Middleware.link(
            new RootMiddleware()
        );
        var request = HttpRequest.parse("GET /not-found HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.64.1\r\nAccept: */*\r\n\r\n");
        var response = middleware.handle(request);
        var outputStream = new ByteArrayOutputStream();
        response.write(outputStream);
        assertEquals("HTTP/1.1 404 Not Found\r\n\r\n", outputStream.toString());
    }

    @Test
    public void handle_with_body() throws IOException {
        var middleware = Middleware.link(
            new RootMiddleware(),
            new EchoMiddleware()
        );
        var request = HttpRequest.parse("GET /echo/abc HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.64.1\r\nAccept: */*\r\n\r\n");
        var response = middleware.handle(request);
        var outputStream = new ByteArrayOutputStream();
        response.write(outputStream);
        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: 3\r\n\r\nabc", outputStream.toString());
    }

    @Test
    public void handle_user_agent_header() throws IOException {
        var middleware = Middleware.link(
            new RootMiddleware(),
            new UserAgentMiddleware()
        );
        var request = HttpRequest.parse("GET /user-agent HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: foobar/1.2.3\r\nAccept: */*\r\n\r\n");
        var response = middleware.handle(request);
        var outputStream = new ByteArrayOutputStream();
        response.write(outputStream);
        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: 12\r\n\r\nfoobar/1.2.3", outputStream.toString());
    }
}
