package http.aux;

public class CRLFString {
    private final String source;

    public CRLFString(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return String.format("%s\r\n", source);
    }
}
