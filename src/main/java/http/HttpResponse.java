package http;

import http.aux.CRLFString;

public class HttpResponse {
    private final HttpVersion httpVersion;
    private final HttpStatus status;

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

    public String toString() {
        return new CRLFString(new CRLFString(httpVersion + " " + status).toString()).toString();
    }
}
