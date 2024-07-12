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

    public HttpStatus getStatus() {
        return status;
    }

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }
}
