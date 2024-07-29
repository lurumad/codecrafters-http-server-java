package files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class PhysicalFileProvider implements FileProvider {

    private final Path root;

    public PhysicalFileProvider(Path root) {
        this.root = root;
    }

    @Override
    public Optional<String> read(String path) {
        var directoryForFiles = Paths.get(root.toString(), path);
        try {
            if (!directoryForFiles.toFile().exists()) {
                return Optional.empty();
            }

            return Optional.of(Files.readString(directoryForFiles));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void write(String path, byte[] body) {
        var directoryForFiles = Paths.get(root.toString(), path);
        try {
            Files.write(directoryForFiles, body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
