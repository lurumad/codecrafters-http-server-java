package middleware;

import files.FileProvider;
import http.HttpRequest;
import http.HttpResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileMiddleware extends Middleware {
    private static final String FILE_PATTERN = "^/files/(.+)$";
    private static final Pattern compiledPattern = Pattern.compile(FILE_PATTERN);
    private final FileProvider fileProvider;

    public FileMiddleware(FileProvider fileProvider) {
        this.fileProvider = fileProvider;
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        var matcher = compiledPattern.matcher(request.getPath());

        if (!matcher.matches()) {
            return handleNext(request);
        }

        return switch (request.getMethod()) {
            case GET -> readFile(matcher);
            case POST -> writeToFile(request, matcher);
        };
    }

    private HttpResponse writeToFile(HttpRequest request, Matcher matcher) {
        var value = matcher.group(1);
        fileProvider.write(value, request.getBody());
        return HttpResponse.created();
    }

    private HttpResponse readFile(Matcher matcher) {
        var value = matcher.group(1);
        var file = fileProvider.read(value);

        if (file.isEmpty()) {
            return HttpResponse.notFound();
        }

        return HttpResponse.ok()
                .contentType("application/octet-stream")
                .contentLength(file.get().length())
                .body(file.get().getBytes());
    }
}
