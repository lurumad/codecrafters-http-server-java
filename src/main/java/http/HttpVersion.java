package http;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1");

    private final String version;

    HttpVersion(String version) {
        this.version = version;
    }

    public static HttpVersion from(String version) {
        for (HttpVersion httpVersion : values()) {
            if (httpVersion.version.equals(version)) {
                return httpVersion;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return version;
    }
}
