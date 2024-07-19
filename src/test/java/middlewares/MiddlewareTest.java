package middlewares;

import files.PhysicalFileProvider;
import files.ResourcesFileProvider;
import http.HttpRequest;
import middleware.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiddlewareTest {
    @Test
    public void respond_with_ok_for_root_path() throws IOException {
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
    public void respond_with_not_found() throws IOException {
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
    public void echoes_path() throws IOException {
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
    public void read_user_agent_header_and_returns_as_response_body() throws IOException {
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

    @Test
    public void returns_a_file() throws IOException {
        var middleware = Middleware.link(
            new RootMiddleware(),
            new FileMiddleware(new ResourcesFileProvider())
        );
        var request = HttpRequest.parse("GET /files/foo HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.64.1\r\nAccept: */*\r\n\r\n");
        var response = middleware.handle(request);
        var outputStream = new ByteArrayOutputStream();
        response.write(outputStream);
        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: application/octet-stream\r\nContent-Length: 13\r\n\r\nHello, World!", outputStream.toString());
    }

    public void returns_not_found_when_a_file_not_exists() throws IOException {
        var middleware = Middleware.link(
                new RootMiddleware(),
                new FileMiddleware(new ResourcesFileProvider())
        );
        var request = HttpRequest.parse("GET /files/non_existant_file HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.64.1\r\nAccept: */*\r\n\r\n");
        var response = middleware.handle(request);
        var outputStream = new ByteArrayOutputStream();
        response.write(outputStream);
        assertEquals("HTTP/1.1 404 Not Found\\r\\n\\r\\n", outputStream.toString());
    }
}
