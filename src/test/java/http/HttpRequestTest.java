package http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpRequestTest {
    @Test
    public void parse_request() {
        try (BufferedReader reader = new BufferedReader(new java.io.StringReader("GET /index.html HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.64.1\r\nAccept: */*\r\n\r\n"))) {
            var request = HttpRequest.from(reader);
            assertEquals(HttpMethod.GET, request.getMethod());
            assertEquals("/index.html", request.getPath());
            assertEquals(HttpVersion.HTTP_1_1, request.getVersion());
            assertTrue(request.header(HttpHeaders.USER_AGENT).isPresent());
            assertEquals("curl/7.64.1", request.header(HttpHeaders.USER_AGENT).get());
        } catch (Exception ex) {
            Assertions.fail("Failed to parse request: " + ex.getMessage());
        }
    }
}
