package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final HttpVersion version;
    private final byte[] body;
    private final Map<String, String> headers = new LinkedHashMap<>();

    private HttpRequest(
            HttpMethod method,
            String path,
            HttpVersion version,
            Map<String, String> headers,
            byte[] body) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.body = body;
        this.headers.putAll(headers);
    }

    public static HttpRequest parse(BufferedReader reader) throws IOException {
        var requestLine = requestLine(reader);
        var headers = headers(reader);
        var body = body(reader, headers);

        return new HttpRequest(
                requestLine.method(),
                requestLine.path(),
                requestLine.version(),
                headers,
                body
        );
    }

    private static RequestLine requestLine(BufferedReader reader) throws IOException {
        var requestLine = reader.readLine().split(" ");
        var method = HttpMethod.valueOf(requestLine[0]);
        var path = requestLine[1];
        var version = HttpVersion.from(requestLine[2]);
        return new RequestLine(method, path, version);
    }

    private static LinkedHashMap<String, String> headers(BufferedReader reader) throws IOException {
        String line;
        var headers = new LinkedHashMap<String, String>();
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            var parts = line.split(": ", 2);
            headers.put(parts[0].trim(), parts[1].trim());
        }
        return headers;
    }

    private static byte[] body(BufferedReader reader, LinkedHashMap<String, String> headers) throws IOException {
        int contentLength = Integer.parseInt(headers.getOrDefault(HttpHeaders.CONTENT_LENGTH, "0"));
        var body = new char[contentLength];
        reader.read(body, 0, contentLength);
        return new String(body).getBytes(StandardCharsets.UTF_8);
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public Optional<String> header(String key) {
        return headers.containsKey(key) ? Optional.of(headers.get(key)) : Optional.empty();
    }

    public byte[] getBody() {
        return body;
    }

    private record RequestLine(HttpMethod method, String path, HttpVersion version) {
    }
}
