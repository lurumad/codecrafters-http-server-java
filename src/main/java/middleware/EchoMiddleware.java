package middleware;

import http.HttpRequest;
import http.HttpResponse;

import java.util.regex.Pattern;

public class EchoMiddleware extends Middleware {
    private static final String ECHO_PATTERN = "^/echo/(.+)$";
    private static final Pattern compiledPattern = Pattern.compile(ECHO_PATTERN);

    @Override
    public HttpResponse handle(HttpRequest request) {
        var matcher = compiledPattern.matcher(request.getPath());

        if (matcher.matches()) {
            var value = matcher.group(1);
            return HttpResponse.ok()
                    .contentType("text/plain")
                    .contentLength(value.length())
                    .body(value.getBytes());
        }

        return handleNext(request);
    }
}
