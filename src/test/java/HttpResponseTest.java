import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpResponseTest {
    @Test
    public void http_response_contains_status_line() {
        var httpResponse = HttpResponse.ok();
        assertEquals(HttpVersion.HTTP_1_1, httpResponse.getHttpVersion());
        assertEquals(HttpStatus.OK, httpResponse.getStatus());
    }
}
