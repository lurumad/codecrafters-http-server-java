package middleware;

import http.HttpHeaders;
import http.HttpRequest;
import http.HttpResponse;

public class UserAgentMiddleware extends Middleware {
    @Override
    public HttpResponse handle(HttpRequest request) {
        if (request.getPath().equals("/user-agent")) {
            var userAgent = request.header(HttpHeaders.USER_AGENT).orElse("");
            return HttpResponse.ok()
                    .contentType("text/plain")
                    .contentLength(userAgent.length())
                    .body(userAgent.getBytes());
        }

        return handleNext(request);
    }
}
