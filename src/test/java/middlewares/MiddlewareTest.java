package middlewares;

import files.FileProvider;
import files.ResourcesFileProvider;
import http.HttpRequest;
import middleware.*;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MiddlewareTest {
    @Test
    public void respond_with_ok_for_root_path() throws IOException {
        var middleware = Middleware.link(
            new RootMiddleware()
        );
        var request = createRequest("GET / HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.64.1\r\nAccept: */*\r\n\r\n");
        var response = middleware.handle(request);
        var outputStream = new ByteArrayOutputStream();
        response.write(outputStream);
        assertEquals("HTTP/1.1 200 OK\r\n\r\n", outputStream.toString());
    }

    @Test
    public void echoes_path() throws IOException {
        var middleware = Middleware.link(
            new RootMiddleware(),
            new EchoMiddleware()
        );
        var request = createRequest("GET /echo/abc HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.64.1\r\nAccept: */*\r\n\r\n");
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
        var request = createRequest("GET /user-agent HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: foobar/1.2.3\r\nAccept: */*\r\n\r\n");
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
        var request = createRequest("GET /files/foo HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.64.1\r\nAccept: */*\r\n\r\n");
        var response = middleware.handle(request);
        var outputStream = new ByteArrayOutputStream();
        response.write(outputStream);
        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: application/octet-stream\r\nContent-Length: 13\r\n\r\nHello, World!", outputStream.toString());
    }

    @Test
    public void returns_not_found_when_a_file_not_exists() throws IOException {
        var middleware = Middleware.link(
                new RootMiddleware(),
                new FileMiddleware(new ResourcesFileProvider())
        );
        var request = createRequest("GET /files/non_existant_file HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.64.1\r\nAccept: */*\r\n\r\n");
        var response = middleware.handle(request);
        var outputStream = new ByteArrayOutputStream();
        response.write(outputStream);
        assertEquals("HTTP/1.1 404 Not Found\r\n\r\n", outputStream.toString());
    }

    @Test
    public void creates_a_new_file_when_post_request() throws IOException {
        var fileProvider = mock(FileProvider.class);;
        var middleware = Middleware.link(
            new RootMiddleware(),
            new FileMiddleware(fileProvider)
        );
        var request = createRequest("POST /files/number HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.64.1\r\nAccept: */*\r\nContent-Type: application/octet-stream\r\nContent-Length: 5\r\n\r\n12345\n");
        var response = middleware.handle(request);
        var outputStream = new ByteArrayOutputStream();
        response.write(outputStream);
        assertEquals("HTTP/1.1 201 Created\r\n\r\n", outputStream.toString());
        verify(fileProvider).write("number", "12345".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void respond_with_not_found() throws IOException {
        var middleware = Middleware.link(
                new RootMiddleware(),
                new EchoMiddleware(),
                new UserAgentMiddleware(),
                new FileMiddleware(new ResourcesFileProvider())
        );
        var request = createRequest("GET /not-found HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.64.1\r\nAccept: */*\r\n\r\n");
        var response = middleware.handle(request);
        var outputStream = new ByteArrayOutputStream();
        response.write(outputStream);
        assertEquals("HTTP/1.1 404 Not Found\r\n\r\n", outputStream.toString());
    }

    //@Test()
    public void respond_with_with_the_compressed_body() throws IOException {
        var middleware = Middleware.link(
                new CompressionMiddleware(),
                new RootMiddleware(),
                new EchoMiddleware()
        );
        var request = createRequest("GET /echo/abc HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.64.1\r\nAccept: */*\r\nAccept-Encoding: gzip\r\n\r\n");
        var response = middleware.handle(request);
        var outputStream = new ByteArrayOutputStream();
        response.write(outputStream);
        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: 20\r\nContent-Encoding: gzip\r\n\r\n1f8b08000000000000ff03000000000000000000", outputStream.toString());
    }

    private HttpRequest createRequest(String content) {
        try (BufferedReader reader = new BufferedReader(new java.io.StringReader(content))) {
            return HttpRequest.from(reader);
        } catch (Exception ex) {
            return null;
        }
    }
}
