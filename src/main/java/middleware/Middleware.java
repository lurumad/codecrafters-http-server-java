package middleware;

import http.HttpRequest;
import http.HttpResponse;

public abstract class Middleware {
    private Middleware next;

    public static Middleware link(Middleware first, Middleware... chain) {
        Middleware head = first;
        for (Middleware middleware : chain) {
            head.next = middleware;
            head = middleware;
        }
        return first;
    }

    public abstract HttpResponse handle(HttpRequest request);

    public HttpResponse handleNext(HttpRequest request) {
        if (next == null) {
            return HttpResponse.notFound();
        }
        return next.handle(request);
    }
}
