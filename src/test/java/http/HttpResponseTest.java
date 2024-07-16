package http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpResponseTest {
    @Test
    public void http_response_contains_status_line() {
        var httpResponse = HttpResponse.ok();
        assertEquals("HTTP/1.1 200 OK\r\n\r\n", httpResponse.toString());
    }
}
