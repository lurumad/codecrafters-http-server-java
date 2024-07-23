package files;

import java.util.Optional;

public class ResourcesFileProvider implements FileProvider {

    @Override
    public Optional<String> read(String path) {
        try (var stream = ResourcesFileProvider.class.getResourceAsStream("/" + path)) {
            if (stream == null) {
                return Optional.empty();
            }
            return Optional.of(new String(stream.readAllBytes()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void write(String path, String body) {
        throw new UnsupportedOperationException("Cannot write to resources");
    }
}
