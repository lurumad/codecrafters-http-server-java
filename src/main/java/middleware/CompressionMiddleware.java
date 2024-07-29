package middleware;

import http.HttpHeaders;
import http.HttpRequest;
import http.HttpResponse;

import java.util.Set;

public class CompressionMiddleware extends Middleware {
    @Override
    public HttpResponse handle(HttpRequest request) {
        var response = handleNext(request);

        if (!response.isSuccessful()) {
            return response;
        }

        if (request.getHeaders().containsKey(HttpHeaders.ACCEPT_ENCODING)) {
            var encodings = request.getHeaders().get(HttpHeaders.ACCEPT_ENCODING).split(",\\s*");
            if (Set.of(encodings).contains("gzip")) {
                return response.gzipped();
            }
        }

        return response;
    }
}
