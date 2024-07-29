package http;

public enum HttpStatus {
    OK(200, "OK"), NOT_FOUND(404, "Not Found"), CREATED(201, "Created"), INTERNAL_SERVER_ERROR(500, "Internal Server Error");

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
