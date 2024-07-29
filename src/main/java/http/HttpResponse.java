package http;

import http.aux.CRLFString;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public class HttpResponse {
    private final HttpVersion httpVersion;
    private final HttpStatus status;
    private final Map<String, String> headers = new LinkedHashMap<>();
    private byte[] body = new byte[0];

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

    public static HttpResponse created() {
        return new HttpResponse(HttpVersion.HTTP_1_1, HttpStatus.CREATED);
    }

    public static HttpResponse internalServerError() {
        return new HttpResponse(HttpVersion.HTTP_1_1, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void body(byte[] body) {
        this.body = body;
    }

    public void write(OutputStream output) throws IOException {
        output.write(new CRLFString(httpVersion + " " + status).toString().getBytes(StandardCharsets.UTF_8));
        for (var entry : headers.entrySet()) {
            output.write(new CRLFString(entry.getKey() + ": " + entry.getValue()).toString().getBytes(StandardCharsets.UTF_8));
        }
        output.write(new CRLFString("").toString().getBytes(StandardCharsets.UTF_8));
        output.write(body);
    }

    public boolean isSuccessful() {
        return status == HttpStatus.OK || status == HttpStatus.CREATED;
    }

    public HttpResponse gzipped() {
        var response = HttpResponse.ok();
        response.addHeader(HttpHeaders.CONTENT_TYPE, "plain/text");
        response.addHeader(HttpHeaders.CONTENT_ENCODING, "gzip");
        var bytesStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(bytesStream)) {
            gzipOutputStream.write(body);
        } catch (Exception e) {
            return HttpResponse.internalServerError();
        }
        var gzipData = bytesStream.toByteArray();
        response.addHeader(HttpHeaders.CONTENT_LENGTH, Integer.toString(gzipData.length));
        response.body(gzipData);
        return response;
    }
}
