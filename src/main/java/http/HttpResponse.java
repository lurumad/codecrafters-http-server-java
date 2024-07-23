package http;

import http.aux.CRLFString;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {
    private final HttpVersion httpVersion;
    private final HttpStatus status;
    private final Map<String, String> headers = new LinkedHashMap<>();
    private String body = "";

    private HttpResponse(HttpVersion httpVersion, HttpStatus status) {
        this.httpVersion = httpVersion;
        this.status = status;
    }

    public static HttpResponse ok() {
        return new HttpResponse(HttpVersion.HTTP_1_1, HttpStatus.OK);
    }

    public static HttpResponse notFound() {
        return new HttpResponse(HttpVersion.HTTP_1_1, HttpStatus.NOT_FOUND);
    }

    public static HttpResponse created() {
        return new HttpResponse(HttpVersion.HTTP_1_1, HttpStatus.CREATED);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void body(String body) {
        this.body = body;
    }

    public void write(OutputStream output) throws IOException {
        var response = new StringBuilder(new CRLFString(httpVersion + " " + status).toString());
        for (var entry : headers.entrySet()) {
            response.append(new CRLFString(entry.getKey() + ": " + entry.getValue()));
        }
        response.append(new CRLFString(""));
        response.append(body);
        output.write(response.toString().getBytes());
    }
}
