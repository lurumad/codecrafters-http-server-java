package middleware;

import http.HttpRequest;
import http.HttpResponse;

import java.util.Set;

public class CompressionMiddleware extends Middleware {
    private static final Set<String> AVAILABLE_ENCODINGS = Set.of("gzip");

    @Override
    public HttpResponse handle(HttpRequest request) {
        var response = handleNext(request);

        if (!response.isSuccessful()) {
            return response;
        }

        if (request.getHeaders().containsKey("Accept-Encoding") &&
                AVAILABLE_ENCODINGS.contains(request.getHeaders().get("Accept-Encoding"))) {
            response.addHeader("Content-Encoding", request.getHeaders().get("Accept-Encoding"));
        }

        return response;
    }
}
