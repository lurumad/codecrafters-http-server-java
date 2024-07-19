package files;

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
}
