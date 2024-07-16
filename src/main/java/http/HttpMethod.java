package http;

public enum HttpMethod {
    GET("GET");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return method;
    }
}
