package http;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpResponseTest {
    @Test
    public void http_response_contains_status_line() throws IOException {
        var httpResponse = HttpResponse.ok();
        var outputStream = new ByteArrayOutputStream();
        httpResponse.write(outputStream);
        assertEquals("HTTP/1.1 200 OK\r\n\r\n", outputStream.toString());
    }
}
