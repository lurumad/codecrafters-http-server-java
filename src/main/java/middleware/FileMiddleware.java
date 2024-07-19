package middleware;

import files.FileProvider;
import http.HttpRequest;
import http.HttpResponse;

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

        if (matcher.matches()) {
            var value = matcher.group(1);
            var file = fileProvider.read(value);

            if (file.isEmpty()) {
                return HttpResponse.notFound();
            }

            var response = HttpResponse.ok();
            response.addHeader("Content-Type", "application/octet-stream");
            response.addHeader("Content-Length", Integer.toString(file.get().length()));
            response.body(file.get());
            return response;
        }

        return handleNext(request);
    }
}
