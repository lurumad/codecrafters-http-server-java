package http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpRequestTest {
    @Test
    public void parse_request() {
        var request = HttpRequest.parse("GET /index.html HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.64.1\r\nAccept: */*\r\n\r\n");
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals("/index.html", request.getPath());
        assertEquals(HttpVersion.HTTP_1_1, request.getVersion());
        assertEquals("curl/7.64.1", request.getHeader("User-Agent"));
    }
}
