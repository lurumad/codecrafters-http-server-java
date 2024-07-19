package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final HttpVersion version;
    private final Map<String, String> headers = new LinkedHashMap<>();

    private HttpRequest(
            HttpMethod method,
            String path,
            HttpVersion version,
            Map<String, String> headers
    ) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.headers.putAll(headers);
    }

    public static HttpRequest parse(String data) {
        try (var reader = new BufferedReader(new java.io.StringReader(data))) {
            var requestLine = requestLine(reader);
            var headers = headers(reader);
            body(reader);

            return new HttpRequest(requestLine.method(), requestLine.path(), requestLine.version(), headers);

        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse request: " + ex.getMessage());
        }
    }

    private static void body(BufferedReader reader) throws IOException {
        String line;

        var bodyBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            bodyBuilder.append(line);
        }
        var body = bodyBuilder.toString();
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

    private static RequestLine requestLine(BufferedReader reader) throws IOException {
        var requestLine = reader.readLine().split(" ");
        var method = HttpMethod.valueOf(requestLine[0]);
        var path = requestLine[1];
        var version = HttpVersion.from(requestLine[2]);
        return new RequestLine(method, path, version);
    }

    private record RequestLine(HttpMethod method, String path, HttpVersion version) {
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

    public String getHeader(String header) {
        return headers.get(header);
    }
}
