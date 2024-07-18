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
            var response = HttpResponse.ok();
            response.addHeader("Content-Type", "text/plain");
            response.addHeader("Content-Length", Integer.toString(value.length()));
            response.body(value);
            return response;
        }

        return handleNext(request);
    }
}
