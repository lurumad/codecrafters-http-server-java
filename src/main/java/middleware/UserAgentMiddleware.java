package middleware;

import http.HttpRequest;
import http.HttpResponse;

public class UserAgentMiddleware extends Middleware {
    @Override
    public HttpResponse handle(HttpRequest request) {
        if (request.getPath().equals("/user-agent")) {
            var response = HttpResponse.ok();
            response.addHeader("Content-Type", "text/plain");
            response.addHeader("Content-Length", Integer.toString(request.getHeader("User-Agent").length()));
            response.body(request.getHeader("User-Agent"));
            return response;
        }

        return handleNext(request);
    }
}
