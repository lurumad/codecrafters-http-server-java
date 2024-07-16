package http;

public enum HttpStatus {
    OK(200, "OK"), NOT_FOUND(404, "Not Found");

    private final int code;
    private final String reason;

    HttpStatus(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    @Override
    public String toString() {
        return code + " " + reason;
    }
}
