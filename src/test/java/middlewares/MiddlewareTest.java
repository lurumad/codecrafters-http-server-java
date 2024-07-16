package middlewares;

import http.HttpRequest;
import middleware.Middleware;
import middleware.RootMiddleware;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiddlewareTest {
    @Test
    public void handle_root_path() {
        var middleware = Middleware.link(
            new RootMiddleware()
        );
        var request = HttpRequest.parse("GET / HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.64.1\r\nAccept: */*\r\n\r\n");
        var response = middleware.handle(request);
        assertEquals("HTTP/1.1 200 OK\r\n\r\n", response.toString());
    }

    @Test
    public void handle_not_found() {
        var middleware = Middleware.link(
            new RootMiddleware()
        );
        var request = HttpRequest.parse("GET /not-found HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.64.1\r\nAccept: */*\r\n\r\n");
        var response = middleware.handle(request);
        assertEquals("HTTP/1.1 404 Not Found\r\n\r\n", response.toString());
    }
}
