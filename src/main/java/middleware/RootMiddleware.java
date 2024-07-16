package middleware;

import http.HttpRequest;
import http.HttpResponse;

public class RootMiddleware extends Middleware{
    @Override
    public HttpResponse handle(HttpRequest request) {
        if (request.getPath().equals("/")) {
            return HttpResponse.ok();
        }

        return handleNext(request);
    }
}
