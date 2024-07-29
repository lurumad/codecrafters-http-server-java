package middleware;

import http.HttpHeaders;
import http.HttpRequest;
import http.HttpResponse;

public class UserAgentMiddleware extends Middleware {
    @Override
    public HttpResponse handle(HttpRequest request) {
        if (request.getPath().equals("/user-agent")) {
            var response = HttpResponse.ok();
            response.addHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
            response.addHeader(
                    HttpHeaders.CONTENT_LENGTH,
                    Integer.toString(request.getHeaders().get(HttpHeaders.USER_AGENT).length())
            );
            response.body(request.getHeaders().get(HttpHeaders.USER_AGENT).getBytes());
            return response;
        }

        return handleNext(request);
    }
}
