package http;

import http.aux.CRLFString;

public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final HttpVersion version;

    private HttpRequest(HttpMethod method, String path, HttpVersion version) {
        this.method = method;
        this.path = path;
        this.version = version;
    }

    public static HttpRequest parse(String data) {
        var requestParts = data.split(new CRLFString("").toString());
        var requestLine = requestParts[0].split(" ");
        return new HttpRequest(
                HttpMethod.valueOf(requestLine[0]),
                requestLine[1],
                HttpVersion.from(requestLine[2])
        );
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
}
